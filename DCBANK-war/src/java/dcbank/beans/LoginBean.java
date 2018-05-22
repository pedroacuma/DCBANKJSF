/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.beans;

import dcbank.entity.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author ofviak
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {
     
    private Usuario loggedUser;
    
    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }
    
    
}
