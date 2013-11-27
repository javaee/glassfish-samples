package customconverter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "customConverterBean")
@RequestScoped
public class CustomConverterBean {

    private Custom custom = new Custom();

    public Custom getCustom() {
        return custom;
    }

    public void setCustom(Custom custom) {
        this.custom = custom;
    }
}
