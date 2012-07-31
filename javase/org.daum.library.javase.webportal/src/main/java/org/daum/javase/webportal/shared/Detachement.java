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
public class Detachement implements IsSerializable {

    private String id;
    private List<Affectation> listeAffectation;
    private Agent chef;

    public Detachement(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Agent getChef() {
        return chef;
    }

    public void setChef(Agent chef) {
        this.chef = chef;
    }

    public List<Affectation> getAffectation() {
        return listeAffectation;
    }

    public void setAffectation(List<Affectation> listeAffectation) {
        this.listeAffectation = listeAffectation;
    }

    public List<Affectation> getListeAffectation() {
        return listeAffectation;
    }

    public void setListeAffectation(List<Affectation> listeAffectation) {
        this.listeAffectation = listeAffectation;
    }
}
