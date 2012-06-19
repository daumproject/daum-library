package org.daum.library.ormH.store;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 15/06/12
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class Cache extends HashMap<Object,Object> {
    private String name = "";

    public Cache(String cachename)
    {
      this.name = cachename;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}