package org.daum.library.replica.msg;

import org.daum.library.replica.cache.VersionedValue;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/03/13
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */
public class Revision  extends AMessage {

    public Object key;
    public VersionedValue versionedValue;
    public String cache;

    public Object getKey()
    {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public VersionedValue getVersionedValue() {
        return versionedValue;
    }

    public void setVersionedValue(VersionedValue versionedValue) {
        this.versionedValue = versionedValue;
    }
}
