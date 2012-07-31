package org.daum.library.android.messages.listener;

//import org.daum.common.message.api.Message;

import org.daum.common.genmodel.*;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 30/05/12
 * Time: 11:53
 * Interface definition for a callback to be invoked when a event happened
 * on the associated NewMessageView
 */
public interface IMessagesListener {

    /**
     * Called when the send message button is clicked
     * @param msg the message
     */
    void onSend(MessageAmbiance msg);
}
