package org.glassfishsamples.ee8additions;

import java.time.LocalDateTime;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("localDateTimeBean")
@RequestScoped
public class LocalDateTimeBean {
    
    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }
}
