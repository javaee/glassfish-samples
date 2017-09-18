package org.glassfishsamples.ee8additions;

import java.time.OffsetDateTime;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("offsetDateTimeBean")
@RequestScoped
public class OffsetDateTimeBean {
    
    public OffsetDateTime getOffsetDateTime() {
        return OffsetDateTime.now();
    }
}
