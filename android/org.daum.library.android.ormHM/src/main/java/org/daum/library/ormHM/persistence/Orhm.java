package org.daum.library.ormHM.persistence;

import java.io.Serializable;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 17:58
 */
public class Orhm implements Serializable {

    private  String cacheName ="";
    private Object id;
    private int generatedType;

    public Orhm(String cacheName,int generatedType,Object id)
    {
        this.cacheName = cacheName;
        this.generatedType = generatedType;
        this.id = id;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }


    public int getGeneratedType() {
        return generatedType;
    }

    public void setGeneratedType(int generatedType) {
        this.generatedType = generatedType;
    }
}
