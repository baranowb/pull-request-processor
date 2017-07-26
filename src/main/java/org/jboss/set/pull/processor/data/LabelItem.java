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