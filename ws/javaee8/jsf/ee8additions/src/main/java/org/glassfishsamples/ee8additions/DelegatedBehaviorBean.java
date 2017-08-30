package org.glassfishsamples.ee8additions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("behaviorBean")
@RequestScoped
public class DelegatedBehaviorBean {
    
    public String submit() {
        return "";
    }
}
