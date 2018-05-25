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
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.inject.Inject;

/**
 *
 * @author Sr.Cuevas
 */
@Named(value = "transferenciaBean")
@RequestScoped
public class TransferenciaBean implements Serializable{
    
@Inject
private LoginBean loginBean;

@Inject
private ClientePrincipalBean clientePrincipalBean;

@EJB
private CuentaFacade cf;

@EJB
private TransferenciaFacade tf;

protected Usuario loggerUser;
protected Cuenta cuentaOrigen,cuentaDestino;
protected String ibanDestino,importe,concepto,error,enlace;
    
    public TransferenciaBean() {
    }

    public Usuario getLoggerUser() {
        return loggerUser;
    }

    public void setLoggerUser(Usuario loggerUser) {
        this.loggerUser = loggerUser;
    }

    public Cuenta getCuenta() {
        return cuentaOrigen;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuentaOrigen = cuenta;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public String getIbanDestino() {
        return ibanDestino;
    }

    public void setIbanDestino(String ibanDestino) {
        this.ibanDestino = ibanDestino;
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

    @PostConstruct
    public void init(){
        loggerUser = loginBean.getLoggedUser();
        cuentaOrigen=loggerUser.getCuentaList().get(0);
    }
    
    public String operacion(){
        cuentaDestino= cf.findByIBAN(ibanDestino);
        if(cuentaDestino != null){
            if(Integer.parseInt(importe)>0){
                if(cuentaOrigen.getSaldo()>= Integer.parseInt(importe)){
                    // quitamos de una cuenta
                    cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - Integer.parseInt(importe));
                    cf.edit(cuentaOrigen);
                    
                    //ponemos en otra cuenta
                    cuentaDestino.setSaldo(cuentaDestino.getSaldo() + Integer.parseInt(importe));
                    cf.edit(cuentaDestino);
                    
                    //creamos las transferencias
                    
                    Transferencia t1 = new Transferencia();
                    t1.setBeneficiario(cuentaDestino.getPropietario().getNombre() + " " + cuentaDestino.getPropietario().getApellidos());
                    t1.setCantidad(Integer.parseInt(importe) * (-1));
                    t1.setConcepto("Trasferencia-" + concepto);
                    t1.setCuenta(cuentaOrigen);
                    t1.setCuentaDestino(cuentaDestino);
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
                    Date fechaAct = new  Date();
                    String fecha = formato.format(fechaAct);
                    t1.setFecha(fecha);
                    
                    tf.create(t1);
                    
                    Transferencia t2 = new Transferencia();
                    t2.setBeneficiario(cuentaDestino.getPropietario().getNombre() + " " + cuentaDestino.getPropietario().getApellidos());
                    t2.setConcepto("Trasferencia-" + concepto);
                    t2.setCantidad(Integer.parseInt(importe));
                    t2.setCuenta(cuentaDestino);
                    t2.setCuentaDestino(cuentaOrigen);
                    t2.setFecha(fecha);
                    
                    tf.create(t2);
                       clientePrincipalBean.init();
                       return "clientePrincipal";
                } else {
                    error = "Saldo de cuenta origen insuficiente";
                    enlace="";
                }
            }else{
                    error = "Importe negativo";
                    enlace="";
            }   
        }else{
            error = "La cuenta destino no existe";
            enlace="";
        }
        return enlace;
    }  
}