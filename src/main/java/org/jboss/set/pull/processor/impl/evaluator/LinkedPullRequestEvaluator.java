package org.jboss.set.pull.processor.impl.evaluator;

import org.jboss.set.aphrodite.domain.PullRequest;
import org.jboss.set.pull.processor.Evaluator;
import org.jboss.set.pull.processor.EvaluatorContext;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.StreamComponentDefinition;
import org.jboss.set.pull.processor.data.EvaluatorData;
import org.jboss.set.pull.processor.data.IssueData;
import org.jboss.set.pull.processor.data.PullRequestData;

public class LinkedPullRequestEvaluator implements Evaluator {

    @Override
    public void eval(EvaluatorContext context, EvaluatorData data) {
        final PullRequestData currentPullRequestData = convert(context.getPullRequest(),context.getStreamComponentDefinition());
        data.setAttributeValue(EvaluatorData.Attributes.PULL_REQUEST_CURRENT, currentPullRequestData);
        //final PullRequest upstreamPullRequest = context.getAphrodite().getUpstreamPullRequest(context.getPullRequest());
        final PullRequest upstreamPullRequest = null; //XXX
        final PullRequestData upstreamPullRequestData = convert(upstreamPullRequest, null); //TODO: check if we can fetch null for this?
        final IssueData upstreamIssue = data.getAttributeValue(EvaluatorData.Attributes.ISSUE_UPSTREAM);
        if(!upstreamIssue.isRequired())
            upstreamPullRequestData.notRequiered();
        data.setAttributeValue(EvaluatorData.Attributes.PULL_REQUEST_UPSTREAM, upstreamPullRequestData);
    }
    
    protected PullRequestData convert(PullRequest pullRequest, StreamComponentDefinition streamComponentDefinition){
        //simple for now
        return new PullRequestData(pullRequest,streamComponentDefinition);
    }

    @Override
    public boolean support(ProcessorPhase processorPhase) {
        if (processorPhase == ProcessorPhase.OPEN) {
            return true;
        }
        return false;
    }

}
