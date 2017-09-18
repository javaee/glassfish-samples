package org.glassfishsamples.ee8additions;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;

@Named("managedPropertyBean")
@RequestScoped
public class ManagedPropertyBean {

    @Inject
    @ManagedProperty("#{injectBean.injectedArtifacts['applicationMap'].size()}")
    Object value;

    public Object getValue() {
        return value;
    }
}
