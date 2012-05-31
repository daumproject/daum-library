package org.daum.common.message.api;

import org.daum.common.util.api.TimeFormatter;

import java.lang.String;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 30/05/12
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */
public class AmbianceMessage implements IMessage {

    public String jeSuis;
    public String jeVois;
    public String jePrevois;
    public String jeProcede;
    public String jeDemande;
    public String groupeHoraire;

    public AmbianceMessage(String s, String v, String pre, String pro, String d) {
        this.jeDemande = d;
        this.jePrevois = pre;
        this.jeProcede = pro;
        this.jeSuis = s;
        this.jeVois = v;
        this.groupeHoraire = TimeFormatter.getGroupeHoraire(new Date());
    }

    @Override
    public String toString() {
        return "["+groupeHoraire+"]"+"["+jeSuis+", "+jeVois+", "+jePrevois+", "+jeProcede+", "+jeDemande+"]";
    }
}
