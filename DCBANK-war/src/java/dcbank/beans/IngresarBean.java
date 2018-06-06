/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.beans;

import dcbank.ejb.CuentaFacade;
import dcbank.ejb.TransferenciaFacade;
import dcbank.entity.Cuenta;
import dcbank.entity.Transferencia;
import dcbank.entity.Usuario;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Gonzalo 
 */
@Named(value = "ingresarBean")
@RequestScoped
public class IngresarBean implements Serializable{
    
    @Inject
    private LoginBean loginBean;
    
    @Inject
    private UsuarioPrincipalBean clientePrincipalBean;
    
    @Inject
    private UsuarioPrincipalBean usuarioPrincipalBean;
    
    @EJB
    private TransferenciaFacade transferenciaFacade;

    @EJB
    private CuentaFacade cuentaFacade;
    
    private String enlace;
    
    protected Usuario loggerUser,userTransf;
    protected Cuenta cuentaOrigen;
    protected String ibanOrigen,importe,concepto,error;
    
    
    /**
     * Creates a new instance of IngresarBean
     */
    public IngresarBean() {
    }
    
    @PostConstruct
    public void init(){
        
        loggerUser = loginBean.getLoggedUser(); //obtenemos quien se ha conectado
        
        if(loggerUser.getRol() == 1){ //si vale 1 accede un empleado
            userTransf = usuarioPrincipalBean.getCliente();
            cuentaOrigen = usuarioPrincipalBean.getCuenta();
        }else{ //accede al cliente
            userTransf = loggerUser;
            cuentaOrigen = loggerUser.getCuentaList().get(0);
        }
        
    }
    
    public String Operacion(){
        int importes = Integer.parseInt(this.importe);
        if(importes > 0){ //comprobamos que hemos introducido saldo
            cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() + importes); //añadimos el importe
            cuentaFacade.edit(cuentaOrigen);
            
            //creamos una Transferencia
            Transferencia t = new Transferencia();
            t.setBeneficiario(cuentaOrigen.getPropietario().getNombre() + " " + cuentaOrigen.getPropietario().getApellidos());
            t.setCantidad(importes);
            t.setConcepto("Ingreso- " + concepto);
            t.setCuenta(cuentaOrigen);
            t.setCuentaDestino(cuentaOrigen);
            t.setFecha(new Date());
            
            transferenciaFacade.create(t);
            
            if(loggerUser.getRol() != 1){
                clientePrincipalBean.init();
                enlace = "clientePrincipal";
            }else{
                usuarioPrincipalBean.reLoader();
                enlace = "empleadoPrincipal";
            }
        }else{ //el importe es negativo
            error = "Importe negativo";
            enlace = "";
        }
        return enlace;
    }
    
    /*--------- GET ----------*/
    public Usuario getLoggerUser() {return loggerUser;}
    public Usuario getUserTransf() {return userTransf;}
    public Cuenta getCuentaOrigen() {return cuentaOrigen;}
    public String getIbanOrigen() {return ibanOrigen;}
    public String getImporte() {return importe;}
    public String getConcepto() {return concepto;}
    public String getError() {return error;}
    /*--------- SET ----------*/
    public void setLoggerUser(Usuario loggerUser) {this.loggerUser = loggerUser;}
    public void setUserTransf(Usuario userTransf) {this.userTransf = userTransf;}
    public void setCuentaOrigen(Cuenta cuentaOrigen) {this.cuentaOrigen = cuentaOrigen;}
    public void setIbanOrigen(String ibanOrigen) {this.ibanOrigen = ibanOrigen;}
    public void setImporte(String importe) {this.importe = importe;}
    public void setConcepto(String concepto) {this.concepto = concepto;}
    public void setError(String error) {this.error = error;}
    /*------------------------*/
}
