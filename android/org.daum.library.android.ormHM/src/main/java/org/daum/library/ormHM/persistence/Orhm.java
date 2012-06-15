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
    private  String idName = "";
    private Object id;
    private GenerationType generationType;

    public Orhm(String cacheName,GenerationType generationType, String idname,Object id)
    {
        this.cacheName = cacheName;
        this.generationType = generationType;
        this.id = id;
        this.idName = idname;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
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


    public GenerationType getGenerationType() {
        return generationType;
    }

    public void setGenerationType(GenerationType generationType) {
        this.generationType = generationType;
    }
}
