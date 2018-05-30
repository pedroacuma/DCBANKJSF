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
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author Javier
 */
@Named(value = "retirarBean")
@RequestScoped
public class RetirarBean implements Serializable {
    
    @Inject
    private EmpleadoPrincipalBean empleadoPrincipalBean;
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
        
        cuenta=empleadoPrincipalBean.getCuentaUsuario();
        
    }
    
    public String operacion(){
        if(Integer.parseInt(importe)>0){
             if(cuenta.getSaldo()>= Integer.parseInt(importe)){
                 cuenta.setSaldo(cuenta.getSaldo() - Integer.parseInt(importe));
                 cf.edit(cuenta);
                 
                    Transferencia t1 = new Transferencia();
                    t1.setBeneficiario(cuenta.getPropietario().getNombre() + " " + cuenta.getPropietario().getApellidos());
                    t1.setCantidad(Integer.parseInt(importe) * (-1));
                    t1.setConcepto("Retirada-" + concepto);
                    t1.setCuenta(cuenta);
                    t1.setFecha(new  Date());
                    
                    tf.create(t1);
                    empleadoPrincipalBean.reLoader();
                    return "empleadoPrincipal"; 
             } else {
                 error = "Saldo de cuenta insuficiente.";
                 enlace="";
             }
            
        } else {
            error = "Importe cero o negativo.";
            enlace="";
        }
        return enlace;
    }
}

