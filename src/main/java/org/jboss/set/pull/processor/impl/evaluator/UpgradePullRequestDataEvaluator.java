package org.jboss.set.pull.processor.impl.evaluator;

import org.jboss.set.pull.processor.Evaluator;
import org.jboss.set.pull.processor.EvaluatorContext;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.data.EvaluatorData;
import org.jboss.set.pull.processor.data.EvaluatorData.Attribute;
import org.jboss.set.pull.processor.data.PullRequestData;
import org.jboss.set.pull.processor.data.UpgradeMetaData;

/**
 * Fetch upgrade meta from any source we see fit.
 *
 * @author baranowb
 *
 */
public class UpgradePullRequestDataEvaluator implements Evaluator {

    @Override
    public void eval(EvaluatorContext context, EvaluatorData data) {
        // split so we dont care where it comes from
        processMetaIfUpgrade(context, EvaluatorData.Attributes.PULL_REQUEST_CURRENT,
                EvaluatorData.Attributes.PULL_REQUEST_CURRENT_UPGRADE, data);
        processMetaIfUpgrade(context, EvaluatorData.Attributes.PULL_REQUEST_UPSTREAM,
                EvaluatorData.Attributes.PULL_REQUEST_UPSTREAM_UPGRADE, data);
    }

    private void processMetaIfUpgrade(final EvaluatorContext context, final Attribute<PullRequestData> pullRequestKey,
            final Attribute<UpgradeMetaData> upgradeMetaDataKey, final EvaluatorData data) {
        final PullRequestData pullRequestData = data.getAttributeValue(pullRequestKey);
        if (!pullRequestData.isDefined() || pullRequestData.isUpgrade()) {
            return;
        }
        // TODO: XXX add in aphro
        // final UpgradeMeta upgradeMeta = context.getAphrodite().getPullRequestUpgradeMeta(pullRequestData.getPullRequest());
        final UpgradeMeta upgradeMeta = pullRequestData.getPullRequest().getUpgradeMeta();
        UpgradeMetaData upgradeMetaData = null;
        if (upgradeMeta == null) {
            upgradeMetaData = new UpgradeMetaData();
        } else {
            upgradeMetaData = new UpgradeMetaData(upgradeMeta);
        }
        data.setAttributeValue(upgradeMetaDataKey, upgradeMetaData);
    }

    @Override
    public boolean support(ProcessorPhase processorPhase) {
        if (processorPhase == ProcessorPhase.OPEN || processorPhase == ProcessorPhase.EVENTS_CLOSED_UPGRADE) {
            return true;
        }
        return false;
    }

}
