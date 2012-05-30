package org.daum.library.android.agetac.messages.listener;

import org.daum.library.android.agetac.messages.message.IMessage;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 30/05/12
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public class MessagesEvent {

    public enum MessagesEventType {
        SEND
    }

    private IMessage content;
    private MessagesEventType type;

    public MessagesEvent(IMessage content, MessagesEventType type) {
        this.content = content;
        this.type = type;
    }

    public IMessage getContent() {
        return content;
    }

    public MessagesEventType getType() {
        return type;
    }
}
