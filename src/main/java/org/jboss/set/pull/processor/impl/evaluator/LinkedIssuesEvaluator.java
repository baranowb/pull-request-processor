package org.jboss.set.pull.processor.impl.evaluator;

import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jboss.set.aphrodite.domain.Flag;
import org.jboss.set.aphrodite.domain.FlagStatus;
import org.jboss.set.aphrodite.domain.Issue;
import org.jboss.set.pull.processor.Evaluator;
import org.jboss.set.pull.processor.EvaluatorContext;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.data.EvaluatorData;
import org.jboss.set.pull.processor.data.IssueData;

/**
 * Simply retrieve issues from PR and process so other evaluators or action items can do some magic. This requires aphro to have
 * mentioned ops.
 *
 * @author baranowb
 *
 */
public class LinkedIssuesEvaluator implements Evaluator {
    // TODO: move this to aphrodite?
    private Pattern UPSTREAM_NOT_REQUIRED = Pattern.compile(".*no.*upstream.*required.*", Pattern.CASE_INSENSITIVE);

    @Override
    public void eval(EvaluatorContext context, EvaluatorData data) {
        // TODO: handle exception on get op?
        //XXX
        final Issue currentIssue = null; // context.getAphrodite().getIssue(context.getPullRequestReference().getPullRequest());
        final Issue upstreamIssue = null; // context.getAphrodite().getUpstreamIssue(context.getPullRequestReference().getPullRequest());
        final List<Issue> relatedIssues = null; // context.getAphrodite().getRelatedIssues(context.getPullRequestReference().getPullRequest());

        final IssueData currentIssueData = convert(currentIssue);
        final IssueData upstreamIssueData = convert(currentIssue);
        final List<IssueData> relatedIssiesData = new ArrayList<>();
        if (relatedIssues != null) {
            for (Issue relatedIssue : relatedIssues) {
                relatedIssiesData.add(convert(relatedIssue));
            }
        }
        if (upstreamIssue == null
                && UPSTREAM_NOT_REQUIRED.matcher(context.getPullRequest().getBody()).find()) {
            upstreamIssueData.notRequired();
        }
        // TODO: what if no upstream required but its there?
        data.setAttributeValue(EvaluatorData.Attributes.ISSUE_CURRENT, currentIssueData);
        data.setAttributeValue(EvaluatorData.Attributes.ISSUE_UPSTREAM, upstreamIssueData);
        data.setAttributeValue(EvaluatorData.Attributes.ISSUES_RELATED, relatedIssiesData);
    }

    protected IssueData convert(final Issue issue) {
        if (issue == null) {
            return new IssueData();// default, we should return more than null;
        }
        final IssueData issueData = new IssueData(issue, Util.getStreams(issue));
        return issueData;
    }

    @Override
    public boolean support(ProcessorPhase processorPhase) {
        return true; // if its in meta, we can use it at any stage?
    }

}
