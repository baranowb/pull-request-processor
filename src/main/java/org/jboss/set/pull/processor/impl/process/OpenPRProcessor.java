package org.jboss.set.pull.processor.impl.process;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.jboss.set.aphrodite.domain.PullRequest;
import org.jboss.set.aphrodite.domain.PullRequestState;
import org.jboss.set.aphrodite.domain.Repository;
import org.jboss.set.aphrodite.spi.NotFoundException;
import org.jboss.set.pull.processor.AbstractProcessor;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.StreamComponentDefinition;
import org.jboss.set.pull.processor.StreamDefinition;
import org.jboss.set.pull.processor.data.PullRequestReference;

public class OpenPRProcessor extends AbstractProcessor {

    public OpenPRProcessor() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public ProcessorPhase getPhase() {
        return ProcessorPhase.OPEN;
    }

    @Override
    protected List<PullRequestReference> fetchPullRequestsRaw() {
        //TODO: this might be bad idea but we will process all repos here and try to make sense out of it
        final List<PullRequestReference> pullRequests = new ArrayList<>();
        for(StreamDefinition streamDefinition: super.processorConfig.getStreamDefinition()){
            if(streamDefinition.isFound()){
                for(StreamComponentDefinition streamComponentDefinition:streamDefinition.getStreamComponents()){
                    if(streamDefinition.isFound()){
                    try {
                        final Repository repository = super.processorConfig.getAphrodite().getRepository(streamComponentDefinition.getStreamComponent().getRepositoryURL().toURL());
                        if(repository != null){
                            final List<PullRequest> componentPullRequests = super.processorConfig.getAphrodite().getPullRequestsByState(repository, PullRequestState.OPEN);
                            //translate it into refs, add to ret val
                            pullRequests.addAll(componentPullRequests.stream().map(p->{
                                PullRequestReference pullRequestReference = new PullRequestReference();
                                pullRequestReference.setComponentDefinition(streamComponentDefinition);
                                pullRequestReference.setPullRequest(p);
                                return pullRequestReference;
                            }).collect(Collectors.toList()));
                        } else {
                            super.logger.warning("Did not find repository: "+streamComponentDefinition.getStreamComponent().getRepositoryURL());
                        }
                    } catch (MalformedURLException e) {
                        super.log(Level.WARNING, "Did not find repo",e);
                    } catch (NotFoundException e) {
                        super.log(Level.WARNING, "Did not find repo",e);
                    }
                    } else {
                        super.log(Level.WARNING, "Component not found, ignoring: "+streamComponentDefinition);
                    }
                }
            }
        }

        return pullRequests;
    }

}
