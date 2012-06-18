package org.daum.library.android.messages.listener;

import org.daum.common.message.api.Message;

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

    private Message content;
    private MessagesEventType type;

    public MessagesEvent(Message content, MessagesEventType type) {
        this.content = content;
        this.type = type;
    }

    public Message getContent() {
        return content;
    }

    public MessagesEventType getType() {
        return type;
    }
}
