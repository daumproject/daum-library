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

    public String id;
    public Date date;

    public  STATES state;

    public  enum STATES {
        ALIVE,
        DEAD,
        DYING
    }

}
