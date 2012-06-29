package org.daum.common.message.api;

import org.daum.common.util.api.TimeFormatter;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;

import org.daum.library.ormH.annotations.Generated;
import org.daum.library.ormH.annotations.Id;
import org.daum.library.ormH.persistence.GeneratedType;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 30/05/12
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */
public class Message implements Serializable {

    @Id
    @Generated(strategy = GeneratedType.UUID)
    private String id = "";
    public String jeSuis;
    public String jeVois;
    public String jePrevois;
    public String jeFais;
    public String jeDemande;
    public String groupeHoraire;
    public String sender;

    /**
     * Construct a message and set its "groupeHoraire" to the actual time
     * @param sender name of the sender
     * @param s jeSuis
     * @param v jeVois
     * @param p jePrevois
     * @param f jeFais
     * @param d jeDemande
     */
    public Message(String sender, String s, String v, String p, String f, String d) {
        this.jeDemande = d;
        this.jePrevois = p;
        this.jeFais = f;
        this.jeSuis = s;
        this.jeVois = v;
        this.groupeHoraire = TimeFormatter.getGroupeHoraire(new Date());
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return a well formed string representation of the message
     */
    public String getText() {
        return jeSuis+"\n"+jeVois+"\n"+jePrevois+"\n"+ jeFais +"\n"+jeDemande;
    }

    /**
     * @return true if every fields are empty; false otherwise
     */
    public boolean isEmpty() {
        return (jeSuis+jeVois+jePrevois+jeFais+jeDemande).isEmpty();
    }

    @Override
    public String toString() {
        return "["+groupeHoraire+" - "+sender+"]"+"["+jeSuis+", "+jeVois+", "+jePrevois+", "+ jeFais +", "+jeDemande+"]";
    }
}
