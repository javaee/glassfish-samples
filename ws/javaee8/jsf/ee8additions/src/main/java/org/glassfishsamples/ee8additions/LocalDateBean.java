package org.glassfishsamples.ee8additions;

import java.time.LocalDate;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("localDateBean")
@RequestScoped
public class LocalDateBean {
    
    public LocalDate getLocalDate() {
        return LocalDate.now();
    }
}
