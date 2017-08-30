package org.glassfishsamples.ee8additions;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "delegatedConverter", managed = true)
public class DelegatedConverter implements Converter {

    public DelegatedConverter() {
    }
    
    @Inject
    private FacesContext facesContext;
    
    @Inject
    private ExternalContext externalContext;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        return facesContext.toString();
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object t) {
        return externalContext.toString();
    }
}
