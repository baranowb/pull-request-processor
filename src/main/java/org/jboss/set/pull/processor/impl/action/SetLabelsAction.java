package org.jboss.set.pull.processor.impl.action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jboss.set.aphrodite.domain.Label;
import org.jboss.set.aphrodite.domain.PullRequest;
import org.jboss.set.aphrodite.spi.NotFoundException;
import org.jboss.set.pull.processor.Action;
import org.jboss.set.pull.processor.ActionContext;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.data.EvaluatorData;
import org.jboss.set.pull.processor.data.IssueData;
import org.jboss.set.pull.processor.data.LabelData;
import org.jboss.set.pull.processor.data.LabelItem;
import org.jboss.set.pull.processor.data.LabelItem.LabelAction;
import org.jboss.set.pull.processor.data.PullRequestData;

public class SetLabelsAction implements Action {

    private static final Logger LOG = Logger.getLogger(SetLabelsAction.class.getName());
    @Override
    public void execute(final ActionContext actionContext, final List<EvaluatorData> data) {
        try {
            actionContext.getExecutors()
                    .invokeAll(data.stream().map(e -> new EvaluatorProcessingTask(actionContext,e)).collect(Collectors.toList()));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean support(ProcessorPhase processorPhase) {
        if (processorPhase == ProcessorPhase.OPEN) {// true only for OPEN, in close we just post process?
            return true;
        } else {
            return false;
        }
    }

    // we can process all PRs concurently, no connection between them should exist
    private class EvaluatorProcessingTask implements Callable<Void> {

        private final EvaluatorData data;
        private final ActionContext actionContext;
        public EvaluatorProcessingTask(final ActionContext actionContext, final EvaluatorData data) {
            super();
            this.data = data;
            this.actionContext = actionContext;
        }

        @Override
        public Void call() throws Exception {
            final PullRequestData pullRequestData = data.getAttributeValue(EvaluatorData.Attributes.PULL_REQUEST_CURRENT);
            final IssueData issueData = data.getAttributeValue(EvaluatorData.Attributes.ISSUE_CURRENT);
            final LabelData labelsData = data.getAttributeValue(EvaluatorData.Attributes.LABELS_CURRENT);
            final PullRequestData upstreamPullRequestData = data.getAttributeValue(EvaluatorData.Attributes.PULL_REQUEST_UPSTREAM);
            final IssueData upstreamIssueData = data.getAttributeValue(EvaluatorData.Attributes.ISSUE_UPSTREAM);
            final LabelData upstreamLabelsData = data.getAttributeValue(EvaluatorData.Attributes.LABELS_UPSTREAM);
            final Set<Label> currentLabels = new TreeSet(new LabelComparator());
            currentLabels.addAll(actionContext.getAphrodite().getLabelsFromPullRequest(pullRequestData.getPullRequest()));
            
            final List<LabelItem> addList = labelsData.getLabels(LabelAction.SET);
            final List<LabelItem> removeList = labelsData.getLabels(LabelAction.REMOVE);
            LOG.log(Level.INFO, "... "+pullRequestData.getPullRequest());
            LOG.log(Level.INFO, "   |... "+ issueData.getIssue());
            LOG.log(Level.INFO, "   |... C:"+ currentLabels);
            LOG.log(Level.INFO, "   |... S:"+ addList);
            LOG.log(Level.INFO, "   |... R:"+ removeList);
            
            if(upstreamPullRequestData.isDefined()){
                final Set<Label> upstreamLabels = new TreeSet(new LabelComparator());
                currentLabels.addAll(actionContext.getAphrodite().getLabelsFromPullRequest(upstreamPullRequestData.getPullRequest()));
                
                final List<LabelItem> upstreamAddList = upstreamLabelsData.getLabels(LabelAction.SET);
                final List<LabelItem> upstreamRemoveList = upstreamLabelsData.getLabels(LabelAction.REMOVE);
                //just for info ?
                LOG.log(Level.INFO, "   |... Upstream ");
                LOG.log(Level.INFO, "       |... "+upstreamIssueData.getIssue());
                LOG.log(Level.INFO, "       |... C:"+upstreamLabels);
                LOG.log(Level.INFO, "       |... S:"+upstreamAddList);
                LOG.log(Level.INFO, "       |... R:"+upstreamRemoveList);
            }
            if(!actionContext.isWritePermitted()){
                LOG.log(Level.INFO, "   |... Write: <<Skipped>>");
                return null;
            }
            //TODO: XXX check actionContext.getAllowedStreams() || PRData.StreamComponent vs write
            for(LabelItem labelItem:removeList){
                Label label = convertToLabel(labelItem.getLabel().toString(), pullRequestData.getPullRequest());
                if(label != null){
                    actionContext.getAphrodite().removeLabelFromPullRequest(pullRequestData.getPullRequest(), label.getName());
                } else {
                    LOG.log(Level.WARNING, "Failed to convert: "+labelItem +" into proper repo label.");
                }
            }
            List<Label> toSet = new ArrayList<>();
            for(LabelItem labelItem:addList){
                Label label = convertToLabel(labelItem.getLabel().toString(), pullRequestData.getPullRequest());
                if(label != null){
                    toSet.add(label);
                } else {
                    LOG.log(Level.WARNING, "Failed to convert: "+labelItem +" into proper repo label.");
                }
            }
            actionContext.getAphrodite().setLabelsToPullRequest(pullRequestData.getPullRequest(), toSet);
            return null;
        }

        //TODO: XXX change thos once aphrodite has been updated. #142
        private Label convertToLabel(final String labelContent, final PullRequest pullRequest){
            //yes, this is bad, many requests
            try {
                //find first matching label
                List<Label> repoLabels = actionContext.getAphrodite().getLabelsFromRepository(pullRequest.getRepository());
                return repoLabels.stream().filter(l -> l.getName().equals(labelContent)).findFirst().get();
            } catch (NotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }
    
    private static class LabelComparator implements Comparator<Label>{

        @Override
        public int compare(Label o1, Label o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            if (o1.equals(o2)) {
                return 0;
            }
            return o1.getName().compareTo(o2.getName());
        }}
}
