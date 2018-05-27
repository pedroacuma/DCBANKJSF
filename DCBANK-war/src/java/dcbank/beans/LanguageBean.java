/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.beans;

import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
 
@Named(value="language")
@Dependent
public class LanguageBean implements Serializable{
	public LanguageBean(){
        }
        public void setEspañol(){
            FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es","ES"));
        }
        public void setEnglish(){
            FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("en","GB"));
        }
	

}