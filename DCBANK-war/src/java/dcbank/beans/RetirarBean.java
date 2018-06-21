/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.beans;

import dcbank.ejb.CuentaFacade;
import dcbank.ejb.TransferenciaFacade;
import dcbank.ejb.UsuarioFacade;
import dcbank.entity.Cuenta;
import dcbank.entity.Transferencia;
import dcbank.entity.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Javier
 */
@Named(value = "retirarBean")
@RequestScoped
public class RetirarBean implements Serializable {
    
    @Inject
    private UsuarioPrincipalBean usuarioPrincipalBean;
    
    @Inject
    private LoginBean login;
    
    @EJB
    private UsuarioFacade uf;
    @EJB
    private CuentaFacade cf;

    @EJB
    private TransferenciaFacade tf;

    protected Usuario user;

    protected Cuenta cuenta;
    protected String iban,importe,concepto,error,enlace;
    
    

    public Usuario getLoggerUser() {
        return user;
    }

    public void setLoggerUser(Usuario loggerUser) {
        this.user = loggerUser;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuentaOrigen) {
        this.cuenta = cuentaOrigen;
    }

    

    public String getIban() {
        return iban;
    }

    public void setIban(String ibanDestino) {
        this.iban = ibanDestino;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }
    
    /**
     * Creates a new instance of RetirarBean
     */
    public RetirarBean() {
    }
    @PostConstruct
    public void init(){
        
        if(login.getLoggedUser().getRol() != 1){
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("faces/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(RegistrarUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        cuenta=usuarioPrincipalBean.getCuenta();

    }
    
    public String operacion(){
        Integer impt = Integer.parseInt(importe);
        
             
        cuenta.setSaldo(cuenta.getSaldo() - impt);
        cf.edit(cuenta);
                 
        Transferencia t1 = new Transferencia();
        t1.setBeneficiario(cuenta.getPropietario().getNombre() + " " + cuenta.getPropietario().getApellidos());
        t1.setCantidad(-impt);
        t1.setConcepto("Retirada-" + concepto);
        t1.setCuenta(cuenta);
        t1.setFecha(new  Date());
                   
        tf.create(t1);
        usuarioPrincipalBean.reLoader();
        return "empleadoPrincipal?faces-redirect=true"; 

    }
}

