package org.glassfishsamples.ee8additions;

import javax.enterprise.context.RequestScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("websocketBean")
@RequestScoped
public class WebsocketBean {
    
    @Inject
    @Push
    private PushContext myChannel;
    
    public void send() {
        myChannel.send("This is an alert!");
    }
}
