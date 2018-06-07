/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

/**
 *
 * @author Pedro Avila
 */
@Named(value = "languageBean")
@SessionScoped
public class LanguageBean implements Serializable {
    
    private Locale locale;
    /**
     * Creates a new instance of LanguageBean
     */
    public LanguageBean() {
    }
    
    @PostConstruct
    public void init(){
       locale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
    }
    
    public Locale getLocale(){
        return locale;
    }
    
    public String getLanguage(){
        return locale.getLanguage();
    }
    
    public void changeLanguage(String language){
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
   
    
}
