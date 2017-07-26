package org.jboss.set.pull.processor;

import org.jboss.set.aphrodite.Aphrodite;
import org.jboss.set.aphrodite.domain.PullRequest;
import org.jboss.set.aphrodite.domain.Repository;

public class EvaluatorContext {

    private final Aphrodite aphrodite;

    private final PullRequest pullRequest;
    
    private final StreamComponentDefinition streamComponentDefinition;

    public EvaluatorContext(final Aphrodite aphrodite, final Repository repository, final PullRequest pullrequest, final StreamComponentDefinition streamComponentDefinition) {
        this.aphrodite = aphrodite;
        this.pullRequest = pullrequest;
        this.streamComponentDefinition = streamComponentDefinition;
    }

    public Aphrodite getAphrodite() {
        return aphrodite;
    }

    public PullRequest getPullRequest() {
        return this.pullRequest;
    }

    public String getBranch() {
        return this.pullRequest.getCodebase().getName();
    }

    public StreamComponentDefinition getStreamComponentDefinition() {
        return streamComponentDefinition;
    }

    
}
