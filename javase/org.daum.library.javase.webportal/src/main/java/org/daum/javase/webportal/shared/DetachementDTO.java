package org.daum.javase.webportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/07/12
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class DetachementDTO implements IsSerializable {

    private String id;
    private List<String> listeIdAffectation = new ArrayList<String>();
    private String idChef;

    public DetachementDTO(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdChef() {
        return idChef;
    }

    public void setIdChef(String idChef) {
        this.idChef = idChef;
    }

    public List<String> getListeIdAffectation() {
        return listeIdAffectation;
    }

    public void setListeIdAffectation(List<String> listeIdAffectation) {
        this.listeIdAffectation = listeIdAffectation;
    }

    public void addIdAffectation(String idAffectation){
        listeIdAffectation.add(idAffectation);
    }
}
