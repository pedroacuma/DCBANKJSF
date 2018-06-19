/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.beans;

import dcbank.ejb.UsuarioFacade;
import dcbank.entity.Usuario;
import dcbank.utils.Md5Hasher;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Pedro Avila
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable{

    @EJB
    private UsuarioFacade usuarioFacade;
   
    private Usuario loggedUser;
    private String dni,pwd;
    private boolean validDNI;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }
    
 
    /**
     * Realiza el login del usuario una vez se han validado sus credenciales.
     * El usuario logueado queda guardado en @loggedUser y se redirige a la zona correspondiente.
     * @return Zona correspondiente al rol del usuario
     */
    public String doLogin(){
        Usuario aux = this.usuarioFacade.find(dni);

        loggedUser = aux;
        boolean empleado = loggedUser.getRol() == 1 ;
        if(empleado){
            return "empleadoPrincipal?faces-redirect=true";  
        }
        else{
            return "clientePrincipal?faces-redirect=true";
        }            

    }


    /**
     * Valida que el DNI introducido se encuentre en la BD. Se utiliza con ajax (event=blur).
     * Lanza ValidatorException sin msg puesto que se define en el input utilizando i18n
     * @param context
     * @param component
     * @param value DNI introducido en el form
     * @throws ValidatorException
     */

    public void validateDNI(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Usuario aux = this.usuarioFacade.find(value);
        
        if(aux==null){
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }else{
            validDNI=true;
        }
        
    }
    
    /**
     * Valida, en caso de que se haya introducido un DNI válido, que la contraseña introducida es correcta para él.
     * Se utiliza con ajax (event=blur). Si no se ha introducido DNI, no realiza nada, el validador required para DNI
     * se encargará de avisar al usuario de que es necesario. Lanza ValidatorException sin msg puesto que se define en 
     * el input utilizando i18n
     * @param context
     * @param component
     * @param value Contraseña introducida en el formulario
     * @throws ValidatorException
     */
    public void validatePwd(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(validDNI){
        
            Usuario aux = this.usuarioFacade.find(dni);
            String pwd = (String) value;
            String pwdHash = "";
            try {
                pwdHash = Md5Hasher.MD5(pwd);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(pwdHash);
            
            if(!pwdHash.equals(aux.getPassword())){
                FacesMessage msg = new FacesMessage();
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
            
        }
    }
    
    
    
    //Getters y setters varios
    
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Usuario getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Usuario loggedUser) {
        this.loggedUser = loggedUser;
    }
    
}
