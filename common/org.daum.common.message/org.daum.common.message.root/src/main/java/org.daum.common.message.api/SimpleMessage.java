package org.daum.common.message.api;

import org.daum.common.util.api.TimeFormatter;

import java.lang.String;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 30/05/12
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
public class SimpleMessage implements IMessage {

    public String text;
    public String groupeHoraire;

    public SimpleMessage(String text) {
        this.text = text;
        this.groupeHoraire = TimeFormatter.getGroupeHoraire(new Date());
    }

    @Override
    public String toString() {
        return "["+groupeHoraire+"]"+text;
    }
}
