package org.ebayopensource.stub.fido.uaf.msg;

import org.ebayopensource.fido.uaf.msg.Version;

public class TrustedFacets {
    private Version version;
    private String[] ids;

    public TrustedFacets(){

    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }
}
