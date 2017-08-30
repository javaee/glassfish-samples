package org.glassfishsamples.ee8additions;

import java.time.OffsetTime;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("offsetTimeBean")
@RequestScoped
public class OffsetTimeBean {
    
    public OffsetTime getOffsetTime() {
        return OffsetTime.now();
    }
}
