package org.glassfishsamples.ee8additions;

import java.time.LocalTime;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("localTimeBean")
@RequestScoped
public class LocalTimeBean {
    
    public LocalTime getLocalTime() {
        return LocalTime.now();
    }
}
