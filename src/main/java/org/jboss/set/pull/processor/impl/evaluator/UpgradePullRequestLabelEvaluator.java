package org.jboss.set.pull.processor.impl.evaluator;

import org.jboss.set.pull.processor.EvaluatorContext;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.data.EvaluatorData;
import org.jboss.set.pull.processor.data.EvaluatorData.Attribute;
import org.jboss.set.pull.processor.data.LabelData;
import org.jboss.set.pull.processor.data.LabelItem;
import org.jboss.set.pull.processor.data.PullRequestData;
import org.jboss.set.pull.processor.data.UpgradeMetaData;

/**
 * CHeck if PR is upgrade if so, check if we have upgrade metadata.
 *
 * @author baranowb
 *
 */
public class UpgradePullRequestLabelEvaluator extends AbstractLabelEvaluator {

    @Override
    public void eval(EvaluatorContext context, EvaluatorData data) {
        processIfPullIsUpgrade(EvaluatorData.Attributes.PULL_REQUEST_CURRENT, EvaluatorData.Attributes.PULL_REQUEST_CURRENT_UPGRADE,EvaluatorData.Attributes.LABELS_CURRENT, data);
        processIfPullIsUpgrade(EvaluatorData.Attributes.PULL_REQUEST_UPSTREAM, EvaluatorData.Attributes.PULL_REQUEST_CURRENT_UPGRADE, EvaluatorData.Attributes.LABELS_UPSTREAM, data);
    }

    private void processIfPullIsUpgrade(final Attribute<PullRequestData> pullRequestDataKey,
            Attribute<UpgradeMetaData> pullRequestCurrentUpgradeKey, final Attribute<LabelData> labelsKey, final EvaluatorData data) {
        final PullRequestData pullRequestData = data.getAttributeValue(pullRequestDataKey);
        if(!pullRequestData.isDefined() || !pullRequestData.isUpgrade() ){
            return;
        }
        
        final UpgradeMetaData upgradeMetaData = data.getAttributeValue(pullRequestCurrentUpgradeKey);
        //check for no def or corrupted, minimum
        if(!upgradeMetaData.isDefined() || !upgradeMetaData.hasEssentials() ){
            //TODO: split this?
            final LabelData labels = super.getLabelData(labelsKey, data);
            labels.addLabelItem(new LabelItem(LabelItem.LabelContent.Corrupted_upgrade_meta, LabelItem.LabelAction.SET, LabelItem.LabelSeverity.BAD));
        } else {
            final LabelData labels = super.getLabelData(labelsKey, data);
            labels.addLabelItem(new LabelItem(LabelItem.LabelContent.Corrupted_upgrade_meta, LabelItem.LabelAction.REMOVE, LabelItem.LabelSeverity.OK));
        }
        //TODO: add check against streams to verify id/gav?
    }

    @Override
    public boolean support(ProcessorPhase processorPhase) {
        if (processorPhase == ProcessorPhase.OPEN || processorPhase == ProcessorPhase.EVENTS_CLOSED_UPGRADE) {
            return true;
        }
        return false;
    }

}
