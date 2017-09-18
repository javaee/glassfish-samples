package org.glassfishsamples.ee8additions;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Named("wholeBean")
@RequestScoped
@Whole
public class WholeBean implements Serializable {
    
    @NotNull
    private String value1;
    
    @Min(2)
    private String value2;

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }
    
    public String submit() {
        return "";
    }
}
