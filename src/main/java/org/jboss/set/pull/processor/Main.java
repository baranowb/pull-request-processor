package org.jboss.set.pull.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jboss.set.aphrodite.Aphrodite;
import org.jboss.set.aphrodite.domain.Stream;
import org.jboss.set.aphrodite.domain.StreamComponent;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class Main {

    public static Logger logger = Logger.getLogger("org.jboss.pull.processor");

    public void start(List<StreamDefinition> parsedStreams, String rootDir, Boolean performWriteOperations) throws Exception {
        logger.info("initializing....");
        try (Aphrodite aphrodite = Aphrodite.instance();
                ClosableHackForExecutor executor = new ClosableHackForExecutor(Executors.newFixedThreadPool(12));) {
            if (parsedStreams.isEmpty()) {
                // this shouldnt happen
                logger.info("No streams specified, can't work like that, make up your mind");
                return;
            } else {
                // XXX: this is a bit scechy, but should do for first iteration
                for (StreamDefinition streamDefinition : parsedStreams) {
                    logger.info("finding all repositories for stream " + streamDefinition);
                    Stream stream = aphrodite.getStream(streamDefinition.getName());
                    if (stream == null) {
                        logger.warning("No stream present for " + streamDefinition);
                        continue;
                    } else {
                        streamDefinition.setStream(stream);
                        for (StreamComponentDefinition streamComponentDefinition : streamDefinition.getStreamComponents()) {
                            final StreamComponent streamComponent = stream.getComponent(streamComponentDefinition.getName());
                            if (streamComponent == null) {
                                logger.warning("No component for stream '" + streamDefinition.getName() + "' under '"
                                        + streamComponentDefinition + "'");
                                continue;
                            } else {
                                streamComponentDefinition.setStreamComponent(streamComponent);
                            }
                        }
                    }
                }
            }

            logger.info("loading evaluators:");
            ServiceLoader<Evaluator> evaluatorsServiceLoader = ServiceLoader.load(Evaluator.class);
            final List<Evaluator> evaluatorServices = new ArrayList<Evaluator>();

            for (Evaluator evaluator : evaluatorsServiceLoader) {
                evaluatorServices.add(evaluator);
                logger.info("...." + evaluator.getClass().getSimpleName());
            }

            logger.info("loading actions:");
            ServiceLoader<Action> actionServiceLoader = ServiceLoader.load(Action.class);
            final List<Action> actionServices = new ArrayList<Action>();
            for (Action action : actionServiceLoader) {
                actionServices.add(action);
                logger.info("...." + action.getClass().getSimpleName());
            }

            ServiceLoader<Processor> processors = ServiceLoader.load(Processor.class);
            logger.info("configuring processors:");
            // yeah, two loops, could be done in one go, but KISS is life.
            for (Processor processor : processors) {
                logger.info("...." + processor.getClass().getName());
                final ProcessorPhase processorPhase = processor.getPhase();
                // find actions and evaluators that can be run by processor
                final List<Action> filteredActions = actionServices.stream().filter(a -> a.support(processorPhase))
                        .collect(Collectors.toList());
                final List<Evaluator> filteredEvaluators = evaluatorServices.stream().filter(a -> a.support(processorPhase))
                        .collect(Collectors.toList());
                final ProcessorConfig processorConfig = new ProcessorConfig(filteredEvaluators, filteredActions, parsedStreams,
                        aphrodite, executor.executorService, rootDir, performWriteOperations);
                processor.init(processorConfig);
            }

            logger.info("executing processors: ");
            for (Processor processor : processors) {
                logger.info("...." + processor.getClass().getName());
                processor.process();
            }
        } finally {
            logger.info("finalizing.");
        }
    }

    private static class ClosableHackForExecutor implements AutoCloseable {
        private ExecutorService executorService;

        public ClosableHackForExecutor(ExecutorService executorService) {
            this.executorService = executorService;
        }

        @Override
        public void close() throws Exception {
            if (this.executorService != null && !this.executorService.isShutdown()) {
                this.executorService.shutdown();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("pull processor");
        parser.addArgument("-s", "--streams").nargs("*").required(true).help(
                "Specify streams to be processed. Format of entry: stream[component,component],stream[component,component]");
        parser.addArgument("-r", "--root").required(true).help("File where save the feed report");
        parser.addArgument("-w", "--write").setDefault(Boolean.FALSE).type(Boolean.class)
                .help("Determine if processors should perform write operation on resources or run locally only. ");
        // parser.addArgument("-as", "--allowed-streams").nargs("*").required(true).help("jira allowed to be tagged in the
        // repos");
        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
            // stream[component,component],stream[component,component]
            List<String> streams = ns.getList("streams");
            List<StreamDefinition> parsedStreams = streams.stream().map(e -> new StreamDefinition(e))
                    .collect(Collectors.toList());
            String directoryRoot = ns.getString("root");
            Boolean performWriteOperations = ns.getBoolean("write");
            // List<String> allowedStreams = ns.getList("allowed_streams");
            new Main().start(parsedStreams, directoryRoot, performWriteOperations);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }
}
