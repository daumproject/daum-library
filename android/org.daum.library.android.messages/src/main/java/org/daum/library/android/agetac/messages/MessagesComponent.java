package org.daum.library.android.agetac.messages;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import org.daum.common.message.api.Message;
import org.daum.library.android.agetac.messages.view.MessagesView;
import org.daum.library.android.agetac.messages.view.ListItemView.MessageType;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.daum.library.android.agetac.messages.listener.IMessagesListener;


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
        try {
            uiService.getRootActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Window window = uiService.getRootActivity().getWindow();
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            });

            messagesView = new MessagesView(uiService.getRootActivity(), getNodeName());
            messagesView.addEventListener(this);

            uiService.addToGroup(TAB_NAME, messagesView);
        } catch (Exception e) {
            Log.e(TAG, "Creating view failed", e);
        }
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
        if (inMessage instanceof Message) {
            uiService.getRootActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messagesView.addMessage((Message) inMessage, MessageType.IN);
                }
            });
        } else {
            Log.w(TAG, "Cannot handle received object on port \"inMessage\"." +
                    "Received object: "+inMessage.getClass().getSimpleName()+", type expected "+Message.class.getName());
        }
    }

    @Override
    public void onSend(Message msg) {
        getPortByName("outMessage", MessagePort.class).process(msg);
        messagesView.addMessage(msg, MessageType.OUT);
        if (D) Log.i("Message sent: ", msg.toString());
    }
}
