package flow;

import java.io.Serializable;
import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.faces.flow.builder.FlowDefinition;

public class EnterFlow implements Serializable {

    private static final long serialVersionUID = -7623501087369765218L;

    @FlowDefinition
    @Produces
    private Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {
        FlowBuilder builder = flowBuilder.id("", "flow");
        builder.viewNode("enter", "/enterflow/stepA.xhtml").markAsStartNode();
        Flow flow = builder.getFlow();
        return flow;
    }
}
