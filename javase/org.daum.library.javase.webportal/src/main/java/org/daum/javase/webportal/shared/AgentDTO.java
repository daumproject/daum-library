package org.daum.javase.webportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 23/07/12
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class AgentDTO implements IsSerializable {

    private String id;
    private String nom;
    private String prenom;
    private String matricule;
    private String password;
    private String pathToImage;
    private boolean isLogged;


    public AgentDTO(){

    }

    public AgentDTO(String nom, String prenom, String matricule, String password){
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        this.pathToImage = pathToImage;
    }

}
