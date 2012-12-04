package org.daum.library.sensors.pojo;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/11/12
 * Time: 17:45
 * To change this template use File | Settings | File Templates.
 */
public class Victime {


    private String _id;
    private String _rev;

    public String nom;
    public String prenom;
    public Date date;


    public  STATES state;

    public  enum STATES {
        ALIVE,
        DEAD,
        DYING
    }


    public String get_id() {
        return _id;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }


    @Override
    public String toString() {
        return "Victime [_id=" + _id + ", _rev=" + _rev + "]";
    }
}
