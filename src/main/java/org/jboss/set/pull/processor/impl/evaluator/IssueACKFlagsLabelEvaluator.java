package org.jboss.set.pull.processor.impl.evaluator;

import org.jboss.set.aphrodite.domain.FlagStatus;
import org.jboss.set.pull.processor.Evaluator;
import org.jboss.set.pull.processor.EvaluatorContext;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.data.EvaluatorData;
import org.jboss.set.pull.processor.data.EvaluatorData.Attribute;
import org.jboss.set.pull.processor.data.IssueData;
import org.jboss.set.pull.processor.data.LabelData;
import org.jboss.set.pull.processor.data.LabelItem;

/**
 * Check what ACK flags we have and set up labels accordingly(or clear).
 * 
 * @author baranowb
 *
 */
public class IssueACKFlagsLabelEvaluator extends AbstractLabelEvaluator {

    @Override
    public void eval(EvaluatorContext context, EvaluatorData data) {
        processAckLabels(EvaluatorData.Attributes.ISSUE_CURRENT, EvaluatorData.Attributes.LABELS_CURRENT, data);
        processAckLabels(EvaluatorData.Attributes.ISSUE_UPSTREAM, EvaluatorData.Attributes.LABELS_UPSTREAM, data);
    }

    protected void processAckLabels(final Attribute<IssueData> issueKey, final Attribute<LabelData> labelsKey, final EvaluatorData data) {
        final IssueData issueToProcess = data.getAttributeValue(issueKey);
        if (issueToProcess.isDefined()) {
            LabelData labelData = super.getLabelData(labelsKey,data);
            boolean hasAllAcks = true;
            hasAllAcks = hasAllAcks
                    && processFlagStatus(labelData, issueToProcess.getPmAckStatus(), LabelItem.LabelContent.Needs_pm_ack);
            hasAllAcks = hasAllAcks
                    && processFlagStatus(labelData, issueToProcess.getDevAckStatus(), LabelItem.LabelContent.Needs_devel_ack);
            hasAllAcks = hasAllAcks
                    && processFlagStatus(labelData, issueToProcess.getQeAckStatus(), LabelItem.LabelContent.Needs_qe_ack);

            if (hasAllAcks) {
                LabelItem li = new LabelItem(LabelItem.LabelContent.Has_All_Acks, LabelItem.LabelAction.SET,
                        LabelItem.LabelSeverity.OK);
                labelData.addLabelItem(li);
            } else {
                LabelItem li = new LabelItem(LabelItem.LabelContent.Has_All_Acks, LabelItem.LabelAction.REMOVE,
                        LabelItem.LabelSeverity.BAD);
                labelData.addLabelItem(li);
            }
        }
    }

    protected boolean processFlagStatus(final LabelData labelData, final FlagStatus status,
            final LabelItem.LabelContent label) {
        if (status.equals(FlagStatus.ACCEPTED)) {
            LabelItem li = new LabelItem(label, LabelItem.LabelAction.REMOVE, LabelItem.LabelSeverity.OK);
            labelData.addLabelItem(li);
            return true;
        } else {
            LabelItem li = new LabelItem(label, LabelItem.LabelAction.SET, LabelItem.LabelSeverity.BAD);
            labelData.addLabelItem(li);
            return false;
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
