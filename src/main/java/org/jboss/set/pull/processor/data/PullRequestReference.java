package org.jboss.set.pull.processor.data;

import org.jboss.set.aphrodite.domain.PullRequest;
import org.jboss.set.pull.processor.StreamComponentDefinition;

//simple class to hold some usefull stuff around
public class PullRequestReference {

    // PR
    private PullRequest pullRequest;
    // stream that this one belongs to
    private StreamComponentDefinition componentDefinition;


    public PullRequest getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
    }

    public StreamComponentDefinition getComponentDefinition() {
        return componentDefinition;
    }

    public void setComponentDefinition(StreamComponentDefinition componentDefinition) {
        this.componentDefinition = componentDefinition;
    }

}
