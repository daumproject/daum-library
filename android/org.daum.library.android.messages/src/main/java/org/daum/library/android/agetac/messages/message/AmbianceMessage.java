package org.daum.library.android.agetac.messages.message;

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

    public AmbianceMessage(String s, String v, String pre, String pro, String d) {
        this.jeDemande = d;
        this.jePrevois = pre;
        this.jeProcede = pro;
        this.jeSuis = s;
        this.jeVois = v;
    }

    @Override
    public String toString() {
        String ret = "Je suis: "+jeSuis+" // ";
        ret += "Je vois: "+jeVois+" // ";
        ret += "Je prevois: "+jePrevois+" // ";
        ret += "Je procede: "+jeProcede+" // ";
        ret += "Je demande: "+jeDemande;
        return ret;
    }
}
