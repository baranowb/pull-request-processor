package org.jboss.set.pull.processor.impl.evaluator;

import org.jboss.set.pull.processor.EvaluatorContext;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.data.EvaluatorData;
import org.jboss.set.pull.processor.data.LabelData;
import org.jboss.set.pull.processor.data.LabelItem;
import org.jboss.set.pull.processor.data.PullRequestData;

/**
 * Check status of PR and if we need it.
 * 
 * @author baranowb
 *
 */
public class UpstreamPullRequestLabelEvaluator extends AbstractLabelEvaluator {

    @Override
    public void eval(EvaluatorContext context, EvaluatorData data) {
        final PullRequestData upstreamPullRequestData = data.getAttributeValue(EvaluatorData.Attributes.PULL_REQUEST_UPSTREAM);
        LabelData labelData = super.getLabelData(EvaluatorData.Attributes.LABELS_UPSTREAM,data);

        if (upstreamPullRequestData.isDefined()) {
            if(upstreamPullRequestData.isMerged()){
                labelData.addLabelItem(new LabelItem(LabelItem.LabelContent.Upstream_merged, LabelItem.LabelAction.SET, LabelItem.LabelSeverity.OK));
            } else {
                //TODO: check if this can happen
                labelData.addLabelItem(new LabelItem(LabelItem.LabelContent.Upstream_merged, LabelItem.LabelAction.REMOVE, LabelItem.LabelSeverity.BAD));
            }
        } else {
            //not defined, check if it is required
            if(upstreamPullRequestData.isRequired()){
                labelData.addLabelItem(new LabelItem(LabelItem.LabelContent.Missing_upstream_PR, LabelItem.LabelAction.REMOVE, LabelItem.LabelSeverity.BAD));
            }
        }
    }

    @Override
    public boolean support(ProcessorPhase processorPhase) {
        if (processorPhase == ProcessorPhase.OPEN) {
            return true;
        }
        return false;
    }

}
