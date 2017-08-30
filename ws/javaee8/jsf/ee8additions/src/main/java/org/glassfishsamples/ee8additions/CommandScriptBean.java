package org.glassfishsamples.ee8additions;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("commandScriptBean")
@SessionScoped
public class CommandScriptBean implements Serializable {

    private String value;

    public String getValue() {
        return value;
    }

    public void execute(String input) {
        value = input.toUpperCase();
    }
}
