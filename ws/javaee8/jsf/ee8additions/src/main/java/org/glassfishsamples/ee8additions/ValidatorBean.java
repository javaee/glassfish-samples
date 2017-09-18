package org.glassfishsamples.ee8additions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("validatorBean")
@RequestScoped
public class ValidatorBean {

    String value;
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String submit() {
        return null;
    }
}
