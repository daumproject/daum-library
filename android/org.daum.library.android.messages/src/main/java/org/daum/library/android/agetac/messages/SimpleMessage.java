package org.daum.library.android.agetac.messages;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 30/05/12
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
public class SimpleMessage implements IMessage {

    public String text;

    public SimpleMessage(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
