package org.jboss.set.pull.processor.impl.evaluator;

import org.jboss.set.pull.processor.Evaluator;
import org.jboss.set.pull.processor.EvaluatorContext;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.data.EvaluatorData;

/**
 * Setup everything that does not fit any category?
 * @author baranowb
 *
 */
public class TriviaEvaluator implements Evaluator {

    @Override
    public void eval(EvaluatorContext context, EvaluatorData data) {
       //TODO:
    }

    @Override
    public boolean support(ProcessorPhase processorPhase) {
        return true;
    }

}
