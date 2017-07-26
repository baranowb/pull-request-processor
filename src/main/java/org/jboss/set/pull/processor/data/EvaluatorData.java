package org.jboss.set.pull.processor.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluatorData {

    private Map<String, Object> data;

    public EvaluatorData(Map<String, Object> data) {
        this.data = data;
    }

    public EvaluatorData() {
        this.data = new HashMap<>();
    }

    public Map<String, Object> getData() {
        return data;
    }

    public <T> T getAttributeValue(Attribute<T> attr) {
        return (T) data.get(attr.name());
    }

    public <T> void setAttributeValue(Attribute<T> attr, T value) {
        data.put(attr.name(), value);
    }

    public static final class Attribute<T> {

        private String name;

        public Attribute(String name) {
            this.name = name;
        }

        public String name() {
            return name;
        }

    }

    public static final class Attributes {
        public static final Attribute<Boolean> WRITE_PERMISSION = new Attribute<>("write");
        public static final Attribute<IssueData> ISSUE_CURRENT = new Attribute<>("issue_current");
        public static final Attribute<IssueData> ISSUE_UPSTREAM = new Attribute<>("issue_upstream");
        public static final Attribute<List<IssueData>> ISSUES_RELATED = new Attribute<>("issues_related");
        public static final Attribute<PullRequestData> PULL_REQUEST_CURRENT = new Attribute<>("pr_current");
        public static final Attribute<PullRequestData> PULL_REQUEST_UPSTREAM = new Attribute<>("pr_upstream");
        public static final Attribute<LabelData> LABELS_CURRENT = new Attribute<>("labels_current");
        public static final Attribute<LabelData> LABELS_UPSTREAM = new Attribute<>("labels_upstream");
        public static final Attribute<UpgradeMetaData> PULL_REQUEST_CURRENT_UPGRADE = new Attribute<>("pr_current_upgrade");
        public static final Attribute<UpgradeMetaData> PULL_REQUEST_UPSTREAM_UPGRADE = new Attribute<>("pr_upstream_upgrade");
        
    }
}