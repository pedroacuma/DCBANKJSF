/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.beans;

import dcbank.ejb.TransferenciaFacade;
import dcbank.entity.Cuenta;
import dcbank.entity.Transferencia;
import dcbank.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author Sr. Cuevas
 */
@Named(value = "clientePrincipalBean")
@SessionScoped
public class ClientePrincipalBean implements Serializable {
    @EJB
    private TransferenciaFacade tf;
    @Inject
    private LoginBean loginBean;
    
    //-----------------------------------------------------------------------//
    
    protected Usuario loggerUser;
    protected Cuenta cuenta;
    protected List<Transferencia> listaMovimientos;
    protected String movimientoBuscado;
    protected String criterioDeBusqueda;
    
    public ClientePrincipalBean() {
    }

    public Usuario getLoggerUser() {
        return loggerUser;
    }

    public void setLoggerUser(Usuario loggerUser) {
        this.loggerUser = loggerUser;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public List<Transferencia> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<Transferencia> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public String getMovimientoBuscado() {
        return movimientoBuscado;
    }

    public void setMovimientoBuscado(String movimientoBuscado) {
        this.movimientoBuscado = movimientoBuscado;
    }

    public String getCriterioDeBusqueda() {
        return criterioDeBusqueda;
    }

    public void setCriterioDeBusqueda(String criterioDeBusqueda) {
        this.criterioDeBusqueda = criterioDeBusqueda;
    }
    
    /**
     *
     */
    @PostConstruct
    public void init(){
        loggerUser = loginBean.getLoggedUser();
        if(loggerUser != null && loggerUser.getRol()!= 1){
            cuenta = loggerUser.getCuentaList().get(0);
            listaMovimientos = tf.buscarPorCuenta(cuenta);
        }
    }
   
    public String doBuscarMovimiento(){
        
        listaMovimientos = cuenta.getTransferenciaList();
        
        if(!this.movimientoBuscado.equals("")){
       
            List<Transferencia> listaM = new ArrayList<>();
            if (this.criterioDeBusqueda.equals("A")){
                //Concepto
                for (Transferencia m : listaMovimientos){
                    if (m.getConcepto().contains(this.movimientoBuscado)){
                        listaM.add(m);
                    }
                }
                listaMovimientos = listaM;
            }else if (this.criterioDeBusqueda.equals("B")){
                //Cuenta Destino
                for(Transferencia m : listaMovimientos){
                    if (m.getCuentaDestino() != null){
                        if (m.getCuentaDestino().getIban().contains(this.movimientoBuscado)){
                        listaM.add(m);
                        }
                    }
                }
                listaMovimientos = listaM;
            }else if (this.criterioDeBusqueda.equals("C")){
                //Beneficiario
                for (Transferencia m : listaMovimientos){
                    if (m.getBeneficiario().contains(this.movimientoBuscado)){
                        listaM.add(m);
                    }
                }
                listaMovimientos = listaM;
            } 
        }
        return "";
    }
}