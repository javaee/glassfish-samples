package org.glassfishsamples.ee8additions;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

@FacesValidator(value = "delegatedValidator", isDefault = false, managed = true)
public class DelegatedValidator implements Validator {
    
    @Inject
    private ExternalContext externalContext;
    
    public DelegatedValidator() {
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object t) throws ValidatorException {
        throw new ValidatorException(new FacesMessage(externalContext.toString()));
    }
}
