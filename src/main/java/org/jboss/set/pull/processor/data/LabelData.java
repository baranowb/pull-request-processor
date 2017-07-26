package org.jboss.set.pull.processor.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class LabelData {
    // TODO: split per action?
    private Set<LabelItem> labels = new TreeSet<>(new Comparator<LabelItem>() {

        @Override
        public int compare(LabelItem o1, LabelItem o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            if (o1.equals(o2)) {
                return 0;
            }
            return o1.getLabel().compareTo(o2.getLabel());

        }
    });

    public void addLabelItem(final LabelItem li) {
        if (this.labels.contains(li)) {
            throw new IllegalArgumentException(li.toString());
        }
        this.labels.add(li);
    }

    public List<LabelItem> getLabels(LabelItem.LabelAction act) {
        return labels.stream().filter(l -> l.getAction().equals(act)).collect(Collectors.toList());
    }

    public Collection<LabelItem> getLabels() {
        return new ArrayList<LabelItem>(labels);
    }
}
