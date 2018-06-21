/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.beans;

import dcbank.ejb.CuentaFacade;
import dcbank.ejb.UsuarioFacade;
import dcbank.entity.Cuenta;
import dcbank.entity.Usuario;
import dcbank.utils.IbanGenerator;
import dcbank.utils.Md5Hasher;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

/**
 *
 * @author Pedro Avila
 */
@Named(value = "registrarUsuarioBean")
@RequestScoped
public class RegistrarUsuarioBean {

    @EJB
    private CuentaFacade cuentaFacade;

    @EJB
    private UsuarioFacade usuarioFacade;
    
    @Inject
    private LoginBean loginBean;
    
    @Inject
    private UsuarioPrincipalBean upb;
    
    private Usuario usuario;
    /**
     * Creates a new instance of RegistrarUsuarioBean
     */
    public RegistrarUsuarioBean() {
    }
    
    
    /**
     * Comprueba que accede un admin. En otro caso redirige a index.
    */
    @PostConstruct
    public void init(){
        if(loginBean.getLoggedUser().getRol() != 1) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("faces/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(RegistrarUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.usuario = new Usuario();
    }
    
    
    /**
     * Valida el formato del DNI introducido y que este no esté registrado
     * @param context
     * @param component
     * @param value DNI introducido
     * @throws ValidatorException con el error correspondiente
     */
    
    
    public void validateDNI(FacesContext context, UIComponent component, Object value) throws ValidatorException{
        String dni = (String) value;
        
        boolean ok = false;
        Pattern patron = Pattern.compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
        Matcher matcher = patron.matcher(dni);

        if (matcher.matches()) {

            String letra = matcher.group(2);
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";

            int index = Integer.parseInt(matcher.group(1));
            index = index % 23;

            String reference = letras.substring(index, index + 1);
            if (!reference.equalsIgnoreCase(letra)) {
                FacesMessage msg = new FacesMessage();
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            } 
        } 
        else {
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
        if(usuarioFacade.find(dni) != null){
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg); 
        }
        
    }
    
    
    /**
     * Registra el usuario como rol 0 y con el hash de la clave introducida.
     * Se le crea una cuenta nueva por defecto. 
     * @return página con información de registro exitoso
     */
    
    public String doRegistrar(){
        usuario.setRol((short) 0);
        try {
            String pwdHash = Md5Hasher.MD5(usuario.getPassword());
            usuario.setPassword(pwdHash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(RegistrarUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.usuarioFacade.create(usuario);
        
        Cuenta c = new Cuenta();
        c.setPropietario(usuario);
        c.setSaldo(0);
        c.setIban(IbanGenerator.generarIBAN());
        this.cuentaFacade.create(c);
        
        List<Cuenta> cuentaList = new ArrayList<>();
        cuentaList.add(c);
        usuario.setCuentaList(cuentaList);
        this.usuarioFacade.edit(usuario);
        
        upb.setCliente(usuario);
        upb.reLoader();
        
        return "registroExitoso?dni=" + usuario.getDni() + "&iban=" + c.getIban() + "&faces-redirect=true";
    }
    
    


    //Getters y setters varios.
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
}
