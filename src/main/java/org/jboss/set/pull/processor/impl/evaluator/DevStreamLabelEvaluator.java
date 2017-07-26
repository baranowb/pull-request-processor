package org.jboss.set.pull.processor.impl.evaluator;

import org.jboss.set.pull.processor.EvaluatorContext;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.data.EvaluatorData;

public class DevStreamLabelEvaluator extends AbstractLabelEvaluator {

    @Override
    public void eval(EvaluatorContext context, EvaluatorData data) {
        //TODO: apply proper stream label - 7.0.z.GA and 7.0.x/7.x depending on something?
    }

    @Override
    public boolean support(ProcessorPhase processorPhase) {
        // TODO Auto-generated method stub
        return false;
    }

}
