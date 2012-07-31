package org.daum.javase.webportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/07/12
 * Time: 12:02
 * To change this template use File | Settings | File Templates.
 */
public class Materiel implements IsSerializable {

    private String id;

    public Materiel(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
