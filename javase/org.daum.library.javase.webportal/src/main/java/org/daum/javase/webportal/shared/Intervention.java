package org.daum.javase.webportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/07/12
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class Intervention implements IsSerializable {

    private String id;
    private String adresse;
    private String codePostal;
    private String ville;
    private Personne requerant;
    private String codeSinistre;
    private List<Detachement> detachement;
    private List<Personne> listeVictime;


    public Intervention(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Personne getRequerant() {
        return requerant;
    }

    public void setRequerant(Personne requerant) {
        this.requerant = requerant;
    }

    public List<Detachement> getDetachement() {
        return detachement;
    }

    public void setDetachement(List<Detachement> detachement) {
        this.detachement = detachement;
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

    public List<Personne> getListeVictime() {
        return listeVictime;
    }

    public void setListeVictime(List<Personne> listeVictime) {
        this.listeVictime = listeVictime;
    }








}
