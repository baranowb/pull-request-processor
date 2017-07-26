package org.jboss.set.pull.processor;

import org.jboss.set.aphrodite.domain.StreamComponent;

public class StreamComponentDefinition {

    private final String name;
    private final StreamDefinition streamDefinition;
    private StreamComponent streamComponent;

    public StreamComponentDefinition(String name, StreamDefinition streamDefinition) {
        super();
        this.name = name;
        this.streamDefinition = streamDefinition;
    }

    public String getName() {
        return name;
    }

    public boolean isFound() {
        return this.streamComponent != null;
    }

    public StreamComponent getStreamComponent() {
        return streamComponent;
    }

    public void setStreamComponent(StreamComponent streamComponent) {
        this.streamComponent = streamComponent;
    }

    public StreamDefinition getStreamDefinition() {
        return streamDefinition;
    }

    @Override
    public String toString() {
        return "StreamComponentDefinition [name=" + name + ", found=" + isFound() + "]";
    }

}
