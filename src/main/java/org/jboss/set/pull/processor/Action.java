package org.jboss.set.pull.processor;

import java.util.List;

import org.jboss.set.pull.processor.data.EvaluatorData;

/**
 * Action which will be executed once evaluators are done with preping all the data.
 *
 * @author baranowb
 *
 */
public interface Action {

    void execute(ActionContext actionContext, List<EvaluatorData> data);

    boolean support(ProcessorPhase processorPhase);

}
