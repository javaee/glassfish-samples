package ezcomp;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


@ManagedBean(name="navigation")
@RequestScoped
public class NavigationHandler {

    private String target = "index";

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String goNav() {
        return target;
    }

    public String goNav1() {
        return "nav1";
    }

    public String goNav2() {
        return "nav2";
    }
}
