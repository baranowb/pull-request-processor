package org.jboss.set.pull.processor.impl.evaluator;

import org.jboss.set.pull.processor.EvaluatorContext;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.data.EvaluatorData;
import org.jboss.set.pull.processor.data.EvaluatorData.Attribute;
import org.jboss.set.pull.processor.data.IssueData;
import org.jboss.set.pull.processor.data.LabelData;
import org.jboss.set.pull.processor.data.LabelItem;

/**
 * Set labels based on presence of issues.
 * 
 * @author baranowb
 *
 */
public class IssuePresentLabelEvaluator extends AbstractLabelEvaluator {

    @Override
    public void eval(EvaluatorContext context, EvaluatorData data) {
        processPresenceLabel(EvaluatorData.Attributes.ISSUE_CURRENT, EvaluatorData.Attributes.LABELS_CURRENT,
                LabelItem.LabelContent.Missing_issue, data);
        processPresenceLabel(EvaluatorData.Attributes.ISSUE_UPSTREAM, EvaluatorData.Attributes.LABELS_UPSTREAM,
                LabelItem.LabelContent.Missing_upstream_issue, data);
    }

    protected void processPresenceLabel(final Attribute<IssueData> issueKey, final Attribute<LabelData> labelsKey,
            final LabelItem.LabelContent expectoPatronum, final EvaluatorData data) {
        LabelData labelData = super.getLabelData(labelsKey,data);
        final IssueData issueToProcess = data.getAttributeValue(issueKey);
        if (issueToProcess.isDefined()) {
            LabelItem li = new LabelItem(expectoPatronum, LabelItem.LabelAction.REMOVE, LabelItem.LabelSeverity.OK);
            labelData.addLabelItem(li);
        } else {
            LabelItem li = new LabelItem(expectoPatronum, LabelItem.LabelAction.SET, LabelItem.LabelSeverity.BAD);
            labelData.addLabelItem(li);
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
