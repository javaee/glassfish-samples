package org.glassfishsamples.ee8additions;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.ApplicationMap;
import javax.faces.annotation.HeaderMap;
import javax.faces.annotation.HeaderValuesMap;
import javax.faces.annotation.InitParameterMap;
import javax.faces.annotation.RequestCookieMap;
import javax.faces.annotation.RequestMap;
import javax.faces.annotation.RequestParameterMap;
import javax.faces.annotation.RequestParameterValuesMap;
import javax.faces.annotation.SessionMap;
import javax.faces.annotation.ViewMap;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("injectBean")
@RequestScoped
public class InjectBean {

    @Inject
    @ApplicationMap
    Map<String, Object> applicationMap;

    @Inject
    ExternalContext externalContext;

    @Inject
    FacesContext facesContext;

    @Inject
    @HeaderMap
    Map<String, String> headerMap;

    @Inject
    @HeaderValuesMap
    Map<String, String[]> headerValuesMap;

    @Inject
    @InitParameterMap
    Map<String, String> initParameterMap;

    @Inject
    @RequestCookieMap
    Map<String, Object> requestCookieMap;

    @Inject
    @RequestMap
    Map<String, Object> requestMap;

    @Inject
    @RequestParameterMap
    Map<String, String> requestParameterMap;

    @Inject
    @RequestParameterValuesMap
    Map<String, String[]> requestParameterValuesMap;
    
    @Inject
    @SessionMap
    Map<String, Object> sessionMap;
    
    @Inject
    UIViewRoot view;
    
    @Inject
    @ViewMap
    Map<String, Object> viewMap;

    Map<String, Object> injectedArtifacts;

    @PostConstruct
    public void initialize() {
        injectedArtifacts = new HashMap<>();
        injectedArtifacts.put("applicationMap", applicationMap);
        injectedArtifacts.put("externalContext", externalContext);
        injectedArtifacts.put("facesContext", facesContext);
        injectedArtifacts.put("headerMap", headerMap);
        injectedArtifacts.put("headerValuesMap", headerValuesMap);
        injectedArtifacts.put("initParameterMap", initParameterMap);
        injectedArtifacts.put("requestCookieMap", requestCookieMap);
        injectedArtifacts.put("requestMap", requestMap);
        injectedArtifacts.put("requestParameterMap", requestParameterMap);
        injectedArtifacts.put("requestParameterValuesMap", requestParameterValuesMap);
        injectedArtifacts.put("sessionMap", sessionMap);
        injectedArtifacts.put("view", view);
        injectedArtifacts.put("viewMap", viewMap);
    }

    public Map<String, Object> getInjectedArtifacts() {
        return injectedArtifacts;
    }
}
