package org.daum.library.android.agetac.messages.listener;

import org.daum.common.message.api.IMessage;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 30/05/12
 * Time: 11:53
 * Interface definition for a callback to be invoked when a event happened
 * on the associated AmbianceMessagesView
 */
public interface IMessagesListener {

    /**
     * Called when the send message button is clicked
     * @param msg the message
     */
    void onSend(IMessage msg);
}
