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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
    protected Date fechaMov;
    String criterioDeBusqueda;


    public Date getFechaMov() {
        return fechaMov;
    }

    public void setFechaMov(Date fechaMov) {
        this.fechaMov = fechaMov;
    }
    
    
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
            System.out.println(this.getCriterioDeBusqueda());
        }
        
    }
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        loginBean.setLoggedUser(null);
        return "index.xhtml?faces-redirect=true";
    }
   
    public String doBuscarMovimiento(){
       listaMovimientos = cuenta.getTransferenciaList();
        if(!this.movimientoBuscado.equals("")){
  
            
        }
        return "";
    }
    public void nombreChangeListener(AjaxBehaviorEvent e){
        if(movimientoBuscado!=null && !movimientoBuscado.equals("") && !movimientoBuscado.equals("-")){
            switch (this.criterioDeBusqueda) {
                case "B":
                    listaMovimientos = tf.buscardorCuentaDestino(movimientoBuscado,cuenta);
                    break;
                case "C":
                    listaMovimientos = tf.buscardorCantidad(movimientoBuscado,cuenta);
                    break;
                case "D":
                    listaMovimientos = tf.buscardorBeneficiario(movimientoBuscado,cuenta);
                    break;
                case "E":
                    listaMovimientos = tf.buscardorConcepto(movimientoBuscado,cuenta);
                    break;
                default:
                    listaMovimientos = cuenta.getTransferenciaList();
                    break;
            }
        }else{
            listaMovimientos = cuenta.getTransferenciaList();
        }
            
    }
    
    public void dateSelect(){
        this.listaMovimientos = this.tf.buscardorFecha(fechaMov, cuenta);
        System.out.println(fechaMov);
        System.out.println(cuenta);
        System.out.println(listaMovimientos.size());
    }
}