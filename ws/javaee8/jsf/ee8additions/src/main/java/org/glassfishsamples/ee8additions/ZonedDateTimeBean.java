package org.glassfishsamples.ee8additions;

import java.time.ZonedDateTime;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("zonedDateTimeBean")
@RequestScoped
public class ZonedDateTimeBean {
    
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.now();
    }
}
