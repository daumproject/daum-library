package org.daum.javase.webportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/07/12
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class Moyens implements IsSerializable {

    private String id;
    private List<String> listeIdAgent;
    private List<String> listeIdMateriel;

    public Moyens(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getListeIdAgent() {
        return listeIdAgent;
    }

    public void setListeIdAgent(List<String> listeIdAgent) {
        this.listeIdAgent = listeIdAgent;
    }

    public List<String> getListeIdMateriel() {
        return listeIdMateriel;
    }

    public void setListeIdMateriel(List<String> listeIdMateriel) {
        this.listeIdMateriel = listeIdMateriel;
    }
}
