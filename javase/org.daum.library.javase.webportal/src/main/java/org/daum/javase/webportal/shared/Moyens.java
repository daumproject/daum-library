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

    private List<Agent> listeAgent;
    private List<Materiel> listeMateriel;

    public Moyens(){

    }

    public List<Agent> getListeAgent() {
        return listeAgent;
    }

    public void setListeAgent(List<Agent> listeAgent) {
        this.listeAgent = listeAgent;
    }

    public List<Materiel> getListeMateriel() {
        return listeMateriel;
    }

    public void setListeMateriel(List<Materiel> listeMateriel) {
        this.listeMateriel = listeMateriel;
    }
}
