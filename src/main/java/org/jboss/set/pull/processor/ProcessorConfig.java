package org.jboss.set.pull.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.jboss.set.aphrodite.Aphrodite;

/**
 * Class which holds all the bits required for processors. Otherwise we would have to to mutate
 *
 * @author baranowb
 */
public class ProcessorConfig {

    // list of evaluators. Those classes should process given input
    // so actions can do some stuff based on what evaluators gather.
    private List<Evaluator> evaluators;

    // Actual "do something" thingies, like write-label, update stream etc,
    private List<Action> actions;

    private Aphrodite aphrodite;

    private ExecutorService executorService;

    //stream defs that were cross checked between user input and streams.json
    //still it contain both valid and invalid versions.
    private List<StreamDefinition> streamDefinition;

    //root dir where we can work.
    private String rootDirectory;

    private boolean write = false;

    public ProcessorConfig(List<Evaluator> evaluators, List<Action> actions, List<StreamDefinition> streamDefinition,
            Aphrodite aphrodite, ExecutorService executorService, String rootDirectory, boolean write) {
        super();
        this.evaluators = evaluators;
        this.actions = actions;
        this.aphrodite = aphrodite;
        this.executorService = executorService;
        this.streamDefinition = new ArrayList<>(streamDefinition);
        this.rootDirectory = rootDirectory;
        this.write = write;
    }

    public List<Evaluator> getEvaluators() {
        return evaluators;
    }

    public List<Action> getActions() {
        return actions;
    }

    public Aphrodite getAphrodite() {
        return aphrodite;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public List<StreamDefinition> getStreamDefinition() {
        return streamDefinition;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public boolean isWrite() {
        return write;
    }

}
