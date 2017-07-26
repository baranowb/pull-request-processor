package org.jboss.set.pull.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jboss.set.aphrodite.domain.PullRequest;
import org.jboss.set.pull.processor.data.EvaluatorData;
import org.jboss.set.pull.processor.data.PullRequestReference;

/**
 * Abstract class. Base for processors. Provide basic code to allow simpler processor dev.
 *
 * @author baranowb
 *
 */
public abstract class AbstractProcessor implements Processor {

    protected static final Logger logger = Logger.getLogger("org.jboss.pull.processor.processes");

    protected ProcessorConfig processorConfig;
    // protected Collection<Codebase> permitedBranches;
    protected final String simpleName;

    public AbstractProcessor() {
        super();
        this.simpleName = getClass().getSimpleName();
    }

    public void init(final ProcessorConfig processorConfig) {
        assert processorConfig != null;
        this.processorConfig = processorConfig;
        // this.permitedBranches = new HashSet<>();
        // for (StreamDefinition streamDefinition : this.processorConfig.getStreamDefinition()) {
        // for (StreamComponentDefinition streamComponentDefinition : streamDefinition.getStreamComponents()) {
        // if (streamComponentDefinition.isFound()) {
        // permitedBranches.add(streamComponentDefinition.getStreamComponent().getCodebase());
        // }
        // }
        // }
    }

    /**
     * Returh phase for which implementation will work. This is used to pick proper evaluators and actions
     *
     * @return
     */
    public abstract ProcessorPhase getPhase();

    /**
     * Processor can fetch PRs based on any criteria it sees fit and by any means. {@link AbstractProcessor#fetchPullRequests}
     * will call it and perform some filtering.
     *
     * @return
     */
    protected abstract List<PullRequestReference> fetchPullRequestsRaw();

    /**
     * Provide basic filtering of existing PRs and matching codebase to one present in jboss streams.
     */
    private List<PullRequestReference> fetchPullRequests() {
        //NOTE1: check if we dont leak PRs this way?
        //NOTE2: do we even care if we leak them?
        return fetchPullRequestsRaw().stream().filter(pr -> {
            if (pr.getComponentDefinition().isFound()
                    && pr.getComponentDefinition().getStreamComponent().getCodebase().equals(pr.getPullRequest().getCodebase()))
                return true;
            else
                return false;
        }).collect(Collectors.toList());
    }

    public void process() throws ProcessorException {
        try {
            final List<EvaluatorData> processedPullRequests = new ArrayList<>();
            final List<PullRequestReference> pullRequests = fetchPullRequests();
            log(Level.INFO, " processing: " + pullRequests.size() + " PRs");
            List<Future<EvaluatorData>> results = this.processorConfig.getExecutorService()
                    .invokeAll(pullRequests.stream().map(e -> new PullRequestEvaluatorTask(e.getPullRequest(),e.getComponentDefinition())).collect(Collectors.toList()));

            for (Future<EvaluatorData> result : results) {
                try {
                    processedPullRequests.add(result.get());
                } catch (Exception ex) {
                    log(Level.SEVERE, "ouch !", ex);
                }
            }

            log(Level.INFO, "executing actions:");
            List<Action> actions = this.processorConfig.getActions();
            ActionContext actionContext = new ActionContext(this.processorConfig);
            for (Action action : actions) {
                log(Level.INFO, "...." + action.getClass().getName());
                // This means that every processor will have its own set of actions
                // ie. report write will be per processor
                action.execute(actionContext, processedPullRequests);
            }
        } catch (InterruptedException ex) {
            throw new ProcessorException("processor execution failed", ex);
        }
    }

    protected void log(final Level level, final String msg) {
        this.logger.log(level, this.simpleName + " " + msg);
    }

    protected void log(final Level level, final String msg, final Throwable t) {
        this.logger.log(level, this.simpleName + " " + msg, t);
    }

    private class PullRequestEvaluatorTask implements Callable<EvaluatorData> {

        private final PullRequest pullRequest;
        private final StreamComponentDefinition streamComponentDefinition;

        public PullRequestEvaluatorTask(final PullRequest e, final StreamComponentDefinition streamComponentDefinition) {
            this.pullRequest = e;
            this.streamComponentDefinition = streamComponentDefinition;
        }

        @Override
        public EvaluatorData call() throws Exception {
            try {
                log(Level.FINE, "processing " + this.pullRequest.getURL().toString());

                EvaluatorContext context = new EvaluatorContext(processorConfig.getAphrodite(),
                        pullRequest.getRepository(), this.pullRequest,this.streamComponentDefinition);
                EvaluatorData data = new EvaluatorData();
                for (Evaluator rule : processorConfig.getEvaluators()) {
                    logger.fine("repository " + pullRequest.getRepository().getURL()
                            + "applying evaluator " + rule.name() + " to "
                            + this.pullRequest.getId());
                    rule.eval(context, data);
                }
                return data;
            } catch (Throwable th) {
                log(Level.SEVERE, "failed to " + this.pullRequest.getURL(), th);
                throw new Exception(th);
            }
        }

    }
}
