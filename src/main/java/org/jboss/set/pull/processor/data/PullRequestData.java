package org.jboss.set.pull.processor.data;

import org.jboss.set.aphrodite.domain.PullRequest;
import org.jboss.set.pull.processor.StreamComponentDefinition;

public class PullRequestData {
    private final PullRequest pullRequest;
    private final StreamComponentDefinition streamComponentDefinition;
    private boolean required = true;

    public PullRequestData(PullRequest pullRequest, StreamComponentDefinition streamComponentDefinition) {
        this.pullRequest = pullRequest;
        this.streamComponentDefinition = streamComponentDefinition;
    }

    public void notRequiered() {
        this.required = false;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isDefined() {
        return this.pullRequest != null;
    }

    public PullRequest getPullRequest() {
        return pullRequest;
    }

    public StreamComponentDefinition getStreamComponentDefinition() {
        return streamComponentDefinition;
    }

    public boolean isMerged() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isUpgrade() {
        // TODO Auto-generated method stub
        return false;
    }

}
