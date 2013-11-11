package customvalidator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "customValidatorBean")
@RequestScoped
public class CustomValidatorBean {

    private Custom custom = new Custom();

    public Custom getCustom() {
        return custom;
    }

    public void setCustom(Custom custom) {
        this.custom = custom;
    }
}
