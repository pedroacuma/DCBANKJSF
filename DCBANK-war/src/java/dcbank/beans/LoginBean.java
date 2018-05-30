/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.beans;

import dcbank.ejb.UsuarioFacade;
import dcbank.entity.Usuario;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Pedro Avila
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private UsuarioFacade usuarioFacade;
     
    
    protected Usuario loggedUser;
    protected String dni,pwd,error; 

    public Usuario getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Usuario loggedUser) {
        this.loggedUser = loggedUser;
    }
    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }
    
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    public String doLogin(){
        Usuario aux = this.usuarioFacade.find(dni);
        
        if(aux==null){
            error = "DNI no registrado";
            return (null);
        }
        else if( pwd.equals(aux.getPassword()) ){
    
            loggedUser = aux;
            boolean empleado = loggedUser.getRol() == 1 ;
            if(empleado){
                    return "empleadoPrincipal?faces-redirect=true";
                    
            }else{
                return "clientePrincipal?faces-redirect=true";
            }            
        }
        else{
            error = "Contraseña errónea";
            return (null);
        }
        
    }
    
    
}
