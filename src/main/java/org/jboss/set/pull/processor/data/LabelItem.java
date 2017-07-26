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
package org.jboss.set.pull.processor.data;

/**
 * Represent available label, action and if it breaks something.
 *
 * @author baranowb
 *
 */
public class LabelItem {

    private LabelAction action;
    private LabelSeverity severity;
    private LabelContent label;

    public LabelItem(final LabelContent label, final LabelAction action, final LabelSeverity severity) {
        super();
        this.action = action;
        this.severity = severity;
        this.label = label;
    }

    public LabelAction getAction() {
        return action;
    }

    public LabelSeverity getSeverity() {
        return severity;
    }

    public LabelContent getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "LabelItem [ label=" + label + ", action=" + action + ", severity=" + severity + "]";
    }

    public static enum LabelAction {
        SET, REMOVE; // remove should be used lightly?
    }

    public static enum LabelSeverity {
        OK, BAD;
    }

    public static enum LabelContent {
        Needs_devel_ack("Needs devel_ack"),
        Needs_pm_ack("Needs pm_ack"),
        Needs_qe_ack("Needs qe_ack"),
        Has_All_Acks("Has All Acks"),
        Upstream_merged("upstream merged"),
        Missing_upstream_issue("Missing upstream issue"),
        Missing_issue("Missing issue"),
        Missing_upstream_PR("Missing upstream PR"),
        Corrupted_upgrade_meta("Corrupted upgrade meta");

        private String label;

        LabelContent(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }
}