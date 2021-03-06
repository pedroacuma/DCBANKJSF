/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Pedro Avila
 */

@FacesValidator("pwd2Validator")
public class Pwd2Validator implements Validator{
     
    /**
     * Comprueba que la contraseña sea equal a la confirmación de contraseña
     * @param context
     * @param component
     * @param value Confirmación de la contraseña
     * @throws ValidatorException 
     */
   
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
     
        String confirm = (String) value;
        
        UIInput uiiPwd = (UIInput) component.getAttributes().get("pwd");
        String password = (String) uiiPwd.getValue();
        
        if (password == null || confirm == null) {
            return;
        }

        if (!password.equals(confirm)) {
            throw new ValidatorException(new FacesMessage());
        }
    
    }
    
}
