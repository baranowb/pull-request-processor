/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.set.pull.processor;

import org.jboss.set.aphrodite.Aphrodite;
import org.jboss.set.aphrodite.domain.PullRequest;

public class EvaluatorContext {

    private final Aphrodite aphrodite;

    private final PullRequest pullRequest;

    private final StreamComponentDefinition streamComponentDefinition;

    private final ProcessorPhase processorPhase;

    public EvaluatorContext(final Aphrodite aphrodite, final PullRequest pullrequest,
            final StreamComponentDefinition streamComponentDefinition, final ProcessorPhase processorPhase) {
        this.aphrodite = aphrodite;
        this.pullRequest = pullrequest;
        this.streamComponentDefinition = streamComponentDefinition;
        this.processorPhase = processorPhase;
    }

    public Aphrodite getAphrodite() {
        return aphrodite;
    }

    public PullRequest getPullRequest() {
        return this.pullRequest;
    }

    public String getBranch() {
        return this.pullRequest.getCodebase().getName();
    }

    public StreamComponentDefinition getStreamComponentDefinition() {
        return streamComponentDefinition;
    }

    public ProcessorPhase getProcessorPhase() {
        return this.processorPhase;
    }
}
