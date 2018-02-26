/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2018, Red Hat, Inc., and individual contributors
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
package org.jboss.set.pull.processor.impl.evaluator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import org.jboss.set.pull.processor.EvaluatorContext;
import org.jboss.set.pull.processor.ProcessorPhase;
import org.jboss.set.pull.processor.data.DefinedLabelItem;
import org.jboss.set.pull.processor.data.EvaluatorData;
import org.jboss.set.pull.processor.data.LabelData;
import org.jboss.set.pull.processor.data.LabelItem;
import org.jboss.set.pull.processor.data.PullRequestData;

/**
 * Check if PR repository and branch match those defined in aphrodite.
 *
 * @author baranowb
 *
 */
public class PullRequestVsStreamComponentLabelEvaluator extends AbstractLabelEvaluator {

    @Override
    public void eval(EvaluatorContext context, EvaluatorData data) {
        final LabelData labelData = super.getLabelData(EvaluatorData.Attributes.LABELS_CURRENT, data);
        final PullRequestData pullRequestData = data.getAttributeValue(EvaluatorData.Attributes.PULL_REQUEST_CURRENT);
        final LabelData labelDataUpstream = super.getLabelData(EvaluatorData.Attributes.LABELS_UPSTREAM, data);
        final PullRequestData pullRequestDataUpstream = data.getAttributeValue(EvaluatorData.Attributes.PULL_REQUEST_UPSTREAM);
        scrutinize(labelData, pullRequestData);
        scrutinize(labelDataUpstream, pullRequestDataUpstream);
    }

    protected void scrutinize(final LabelData labelData, final PullRequestData pullRequestData) {
        try {
            //this might happen in case of upstream either not required or
            if (!pullRequestData.isRequired() || pullRequestData.getPullRequest() == null) {
                return;
            }

            final URI pullRequestRepositoryURI = pullRequestData.getPullRequest().getRepository().getURL().toURI();
            final URI componentRepositoryURI = pullRequestData.getStreamComponentDefinition().getStreamComponent().getRepositoryURL();
            if(pullRequestRepositoryURI.equals(componentRepositoryURI)) {
                LabelItem<?> li = new DefinedLabelItem(DefinedLabelItem.LabelContent.Component_Repository_Mismatch, LabelItem.LabelAction.REMOVE, LabelItem.LabelSeverity.OK);
                labelData.addLabelItem(li);
            } else {
                LabelItem<?> li = new DefinedLabelItem(DefinedLabelItem.LabelContent.Component_Repository_Mismatch, LabelItem.LabelAction.SET, LabelItem.LabelSeverity.BAD);
                labelData.addLabelItem(li);
            }

            if(pullRequestData.getPullRequest().getCodebase().equals(pullRequestData.getStreamComponentDefinition().getStreamComponent().getCodebase())) {
                LabelItem<?> li = new DefinedLabelItem(DefinedLabelItem.LabelContent.Component_Branch_Mismatch, LabelItem.LabelAction.REMOVE, LabelItem.LabelSeverity.OK);
                labelData.addLabelItem(li);
            } else {
                LabelItem<?> li = new DefinedLabelItem(DefinedLabelItem.LabelContent.Component_Branch_Mismatch, LabelItem.LabelAction.SET, LabelItem.LabelSeverity.BAD);
                labelData.addLabelItem(li);
            }
        } catch (URISyntaxException e) {
            super.LOG.log(Level.SEVERE, "Failed to assess repository/PR URI", e);
        }
    }
    @Override
    public boolean support(ProcessorPhase processorPhase) {
        if (processorPhase == ProcessorPhase.OPEN) {
            return true;
        }
        return false;
    }

}
