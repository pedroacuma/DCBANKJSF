/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.beans;

import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
 
@Named(value="language")
@SessionScoped
public class LanguageBean implements Serializable{
  private final static Locale ENGLISH = new Locale("en","GB");
  private final static Locale SPANISH = new Locale("es","ES");
	public LanguageBean(){
        }
        public void setEspañol(){
            FacesContext.getCurrentInstance().getViewRoot().setLocale(SPANISH);
        }
        public void setEnglish(){
             FacesContext.getCurrentInstance().getViewRoot().setLocale(ENGLISH);
        }
  }

