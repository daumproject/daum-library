package org.daum.javase.webportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/07/12
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class InterventionDTO implements IsSerializable {

    private String id;
    private String adresse;
    private String codePostal;
    private String ville;
    private String idRequerant;
    private String codeSinistre;
    private List<String> listeIdDetachement = new ArrayList<String>();
    private List<String> listeIdVictime = new ArrayList<String>();
    private String idDetachement;
    private String description;

    public InterventionDTO(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodeSinistre() {
        return codeSinistre;
    }

    public void setCodeSinistre(String codeSinistre) {
        this.codeSinistre = codeSinistre;
    }

    public List<String> getListeIdDetachement() {
        return listeIdDetachement;
    }

    public void setListeIdDetachement(List<String> listeIdDetachement) {
        this.listeIdDetachement = listeIdDetachement;
    }

    public List<String> getListeIdVictime() {
        return listeIdVictime;
    }

    public void setListeIdVictime(List<String> listeIdVictime) {
        this.listeIdVictime = listeIdVictime;
    }

    public void addVictime(String idVictime){
        listeIdVictime.add(idVictime);
    }

    public String getIdRequerant() {
        return idRequerant;
    }

    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

    public String getIdDetachement() {
        return idDetachement;
    }

    public void setIdDetachement(String idDetachement) {
        this.idDetachement = idDetachement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString(){
        return   ""+adresse+" "+ codePostal+" "+ville+" "+codeSinistre+" "+description;
    }
}
