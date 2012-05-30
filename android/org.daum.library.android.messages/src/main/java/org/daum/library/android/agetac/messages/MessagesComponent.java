package org.daum.library.android.agetac.messages;

import android.util.Log;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.daum.library.android.agetac.messages.view.IMessagesListener;
import org.daum.library.android.agetac.messages.view.MessagesView;


/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 15/05/12
 * Time: 14:59
 */



@Library(name = "Android")
@Provides({
        @ProvidedPort(name = "inMessage", type = PortType.MESSAGE)
})
@Requires({
        @RequiredPort(name = "outMessage", type = PortType.MESSAGE, optional = true)
})
@ComponentType
public class MessagesComponent extends AbstractComponentType implements IMessagesListener {

    // Debugging
    private static final String TAG = "MessagesComponent";
    private static boolean D = true;

    private static final String TAB_NAME = "Messages";

    private KevoreeAndroidService uiService;
    private MessagesView messagesView;

    @Start
    public void start() {
        if (D) Log.i(TAG, "BEGIN start");
        this.uiService = UIServiceHandler.getUIService();
        initUI();
        if (D) Log.i(TAG, "END start");
    }

    private void initUI() {
        messagesView = new MessagesView(uiService.getRootActivity());
        messagesView.addEventListener(this);
        uiService.addToGroup(TAB_NAME, messagesView);
    }

    @Stop
    public void stop() {
        uiService.remove(messagesView);
    }

    @Update
    public void update() {
        stop();
        start();
    }

    @Port(name="inMessage")
    public void messageIncoming(final Object inMessage) {
        if (D) Log.i(TAG, "BEGIN messageIncoming");
        if (inMessage instanceof AmbianceMessage) {
            uiService.getRootActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messagesView.setAmbienceMessage((AmbianceMessage) inMessage);
                }
            });
        } else if (inMessage instanceof IMessage) {
            uiService.getRootActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messagesView.setMessage((IMessage) inMessage);
                }
            });
        } else {
            Log.w(TAG, "Received object on port \"inMessage\" must be of org.kevoree.library.android.agetac.messages.AmbianceMessage type");
        }
        if (D) Log.i(TAG, "END messageIncoming");
    }

    @Override
    public void onSend(IMessage msg) {
        if (D) Log.i(TAG, "BEGIN onSend");
        if (isPortBinded("outMessage")) {
            getPortByName("outMessage", MessagePort.class).process(msg);
            if (D) Log.i("Message sent: ", msg.toString());
        }
        if (D) Log.i(TAG, "END onSend");
    }
}
