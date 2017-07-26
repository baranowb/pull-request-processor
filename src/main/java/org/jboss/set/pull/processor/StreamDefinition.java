package org.jboss.set.pull.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.set.aphrodite.domain.Stream;

/**
 * Class which represents stream definition with components to process. Expected input:<br>
 * 'streamName[comp1,comp2]'.<br>
 * Example: <br>
 * 'jboss-eap-7.0.z[jbossas-jboss-eap7,jbossas-wildfly-core-eap]'<br>
 * stream name and component must match entries in streams file/resource.
 *
 * @author baranowb
 *
 */
public class StreamDefinition {
    private final String name;
    private Stream stream;

    private final List<StreamComponentDefinition> streamComponents;

    public StreamDefinition(final String def) {
        int index = def.indexOf('[');
        if (index == -1) {
            this.name = def;
            this.streamComponents = new ArrayList<>();
        } else {
            this.name = def.substring(0, index);
            // split components into list and lambda the hell out of it into wrapper
            this.streamComponents = Arrays.asList(def.substring(index + 1).replace("]", "").split(",")).stream()
                    .map(s -> new StreamComponentDefinition(s, this)).collect(Collectors.toList());
        }
    }

    public String getName() {
        return name;
    }

    public List<StreamComponentDefinition> getStreamComponents() {
        return streamComponents;
    }

    public boolean isFound() {
        return this.stream != null;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    @Override
    public String toString() {
        return "StreamDefinition [name=" + name + ", found=" + isFound() + ", streamComponents=" + streamComponents + "]";
    }

}