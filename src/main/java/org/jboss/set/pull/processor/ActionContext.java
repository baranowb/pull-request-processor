package org.jboss.set.pull.processor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.jboss.set.aphrodite.Aphrodite;

public class ActionContext {

    private final List<StreamDefinition> allowedStreams;

    private final ProcessorConfig processorConfig;

    public ActionContext(final ProcessorConfig processorConfig) {
        this.processorConfig = processorConfig;
        this.allowedStreams = Collections.unmodifiableList(this.processorConfig.getStreamDefinition());
    }

    public Aphrodite getAphrodite() {
        return this.processorConfig.getAphrodite();
    }

    public String getRoot() {
        return this.processorConfig.getRootDirectory();
    }

    public Boolean isWritePermitted() {
        return this.processorConfig.isWrite();
    }

    public List<StreamDefinition> getAllowedStreams() {
        return allowedStreams;
    }

    public ExecutorService getExecutors(){
        return this.processorConfig.getExecutorService();
    }
}
