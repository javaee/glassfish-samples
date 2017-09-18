package org.glassfishsamples.ee8additions;

import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;

@FacesBehavior(value = "injectedBehavior", managed = true)
public class DelegatedBehavior extends ClientBehaviorBase {
    
    @Inject
    private ExternalContext externalContext;
    
    public DelegatedBehavior() {
    }
    
    @Override
    public String getScript(ClientBehaviorContext behaviorContext) {
        return "<!-- " + externalContext.toString() + " -->";
    }
}
