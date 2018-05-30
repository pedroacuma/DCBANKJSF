/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcbank.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Pedro Avila
 */
@FacesValidator("dniValidator")
public class DniValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        
        String dni = (String) value;
        
        boolean ok = false;
        Pattern patron = Pattern.compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
        Matcher matcher = patron.matcher(dni);

        if (matcher.matches()) {

            String letra = matcher.group(2);
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";

            int index = Integer.parseInt(matcher.group(1));
            index = index % 23;

            String reference = letras.substring(index, index + 1);
            if (!reference.equalsIgnoreCase(letra)) {
                FacesMessage msg = new FacesMessage(" (DNI inválido)");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            } 
        } 
        else {
            FacesMessage msg = new FacesMessage(" (DNI inválido)");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }


    }
    
}
