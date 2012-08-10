package org.daum.library.replica.msg;

import org.daum.library.replica.cache.VersionedValue;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 22/05/12
 * Time: 17:51
 */

public class Update extends AMessage {
    private static final long serialVersionUID = 1520L;
    public Object key;
    public VersionedValue versionedValue;
    public String cache;

    public Object getKey() {
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

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String toString(){
        return "Update "+event+" "+cache+" from "+getSourceNode();
    }
}