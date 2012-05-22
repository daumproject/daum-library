package org.daum.library.ormHM.persistence;

import java.io.Serializable;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 17:58
 */
public class OrhmID implements Serializable {

    private  String attachToCache ="";
    private Object id;

    public OrhmID(String attachTO, Object id) {
        this.attachToCache = attachTO;
        this.id = id;

    }
    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getAttachToCache() {
        return attachToCache;
    }

    public void setAttachToCache(String attachToCache) {
        this.attachToCache = attachToCache;
    }
}
