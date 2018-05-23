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
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author Jairo
 */
@Named(value = "empleadoPrincipalBean")
@SessionScoped
public class EmpleadoPrincipalBean implements Serializable {

    /**
     * Creates a new instance of EmpleadoPrincipal
     */
    /*
        ===================
    */
    @EJB
    UsuarioFacade uf;
    
    @EJB
    TransferenciaFacade tf;
    
    @EJB
    CuentaFacade cf;
    
    @Inject
    private LoginBean loginBean;
    
    /*
        ===================
    */
    
    protected Usuario loggedUser;
    protected Usuario usuarioBuscado;
    protected List<Transferencia> listaMovimientos;
    protected Cuenta cuentaUsuario;
    
    protected String dniBuscado;
    protected String movimientoBuscado;
    protected String criterioDeBusqueda;
    
    public EmpleadoPrincipalBean() {
    }

    public Usuario getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Usuario loggedUser) {
        this.loggedUser = loggedUser;
    }

    public Usuario getUsuarioBuscado() {
        return usuarioBuscado;
    }

    public void setUsuarioBuscado(Usuario usuarioBuscado) {
        this.usuarioBuscado = usuarioBuscado;
    }

    public List<Transferencia> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<Transferencia> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public Cuenta getCuentaUsuario() {
        return cuentaUsuario;
    }

    public void setCuentaUsuario(Cuenta cuentaUsuario) {
        this.cuentaUsuario = cuentaUsuario;
    }

    public String getDniBuscado() {
        return dniBuscado;
    }

    public void setDniBuscado(String dniBuscado) {
        this.dniBuscado = dniBuscado;
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
    
    
    
    
    @PostConstruct
    public void init(){
        List<Usuario> listaUsuarios = uf.findAll();
        loggedUser = loginBean.loggedUser;
        if (loggedUser == null){
            doSalir();
        }else if (loggedUser.getRol() == 0){
            doSalir();
        }
        
    }
    
    public String doBuscarUsuario(){
        usuarioBuscado = uf.find(dniBuscado);
        if (usuarioBuscado != null){
            cuentaUsuario = usuarioBuscado.getCuentaList().get(0);
            listaMovimientos = cuentaUsuario.getTransferenciaList();
        }else{
            cuentaUsuario = null;
            listaMovimientos = null;
        }
        
        return "";
    }
    
    public String doBuscarMovimiento(){
        
        listaMovimientos = cuentaUsuario.getTransferenciaList();
        
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
                for (Transferencia m : listaMovimientos){
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
            
            /*
            if (listaM.isEmpty()){
                listaMovimientos = cuentaUsuario.getTransferenciaList();  
            }
            */
            
        }
 
        return "";
    }

    private String doSalir() {
        System.out.println("La hemos liao");
        return "index";
    }
}
