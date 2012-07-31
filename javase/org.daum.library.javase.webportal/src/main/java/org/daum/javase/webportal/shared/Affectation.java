package org.daum.javase.webportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/07/12
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class Affectation implements IsSerializable {
    private String id;
    private Moyens moyens;

    public Affectation(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Moyens getMoyens() {
        return moyens;
    }

    public void setMoyens(Moyens moyens) {
        this.moyens = moyens;
    }
}
