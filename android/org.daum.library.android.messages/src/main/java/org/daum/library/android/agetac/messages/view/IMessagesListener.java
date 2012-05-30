package org.daum.library.android.agetac.messages.view;

import org.daum.library.android.agetac.messages.AmbianceMessage;
import org.daum.library.android.agetac.messages.IMessage;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 30/05/12
 * Time: 11:53
 * To change this template use File | Settings | File Templates.
 */
public interface IMessagesListener {

    /**
     * Called when the send message button is clicked
     * @param msg the message
     */
    void onSend(IMessage msg);
}
