package org.jboss.set.pull.processor.data;

public class UpgradeMetaData {
    // TODO: move this into PullRequestData?
    // TODO: add more ?
    // required
    private UpgradeMeta upgradeMeta;

    public UpgradeMetaData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UpgradeMetaData(final UpgradeMeta upgradeMeta) {
        this.upgradeMeta = upgradeMeta;
    }

    public boolean isDefined() {
        return this.upgradeMeta != null;
    }

    public String getId() {
        if (this.isDefined())
            return this.upgradeMeta.getId();
        return null;
    }

    public String getVersion() {
        if (this.isDefined())
            return this.upgradeMeta.getVersion();
        return null;
    }

    public String getTag() {
        if (this.isDefined())
            return this.upgradeMeta.getTag();
        else
            return null;
    }

    public String getCodebase() {
        if (this.isDefined())
            return this.upgradeMeta.getCodebase();
        else
            return null;
    }

    public boolean hasEssentials() {
        return this.getId() != null && this.getVersion() != null && this.getTag() != null;
    }

}
