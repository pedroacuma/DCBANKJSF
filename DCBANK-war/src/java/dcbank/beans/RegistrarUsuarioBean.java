/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.beans;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author ofviak
 */
@Named(value = "registrarUsuarioBean")
@RequestScoped
public class RegistrarUsuarioBean {

    @Inject
    private LoginBean loginBean;
    
    /**
     * Creates a new instance of RegistrarUsuarioBean
     */
    public RegistrarUsuarioBean() {
    }
    
    @PostConstruct
    public void init(){
        if(loginBean.getLoggedUser().getRol() != 1) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("faces/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(RegistrarUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

}
