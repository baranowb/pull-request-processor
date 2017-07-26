package org.jboss.set.pull.processor.impl.evaluator;

import org.jboss.set.pull.processor.Evaluator;
import org.jboss.set.pull.processor.data.EvaluatorData;
import org.jboss.set.pull.processor.data.EvaluatorData.Attribute;
import org.jboss.set.pull.processor.data.LabelData;

public abstract class AbstractLabelEvaluator implements Evaluator {

    protected LabelData getLabelData(Attribute<LabelData> labelsIssueUpstream, EvaluatorData data) {
        LabelData labelData = data.getAttributeValue(EvaluatorData.Attributes.LABELS_UPSTREAM);
        if (labelData == null) {
            labelData = new LabelData();
            data.setAttributeValue(EvaluatorData.Attributes.LABELS_UPSTREAM, labelData);
        }
        return labelData;
    }
}
