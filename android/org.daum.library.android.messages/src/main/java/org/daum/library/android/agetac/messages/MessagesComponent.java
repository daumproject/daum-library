package org.daum.library.android.agetac.messages;

import android.R;
import android.app.LocalActivityManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import org.daum.common.message.api.Message;
import org.daum.library.android.agetac.messages.view.MessagesListView;
import org.daum.library.android.agetac.messages.view.NewMessageView;
import org.daum.library.android.agetac.messages.view.MyTabHost;
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
    private static final String SUB_TAB_NEW_MSG = "Saisi d'un nouveau message";
    private static final String SUB_TAB_MSG_LIST = "Historique des messages";

    private KevoreeAndroidService uiService;
    private NewMessageView newMsgView;
    private MessagesListView msgListView;
    private MyTabHost tabHost;

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

            newMsgView = new NewMessageView(uiService.getRootActivity(), getNodeName());
            newMsgView.addEventListener(this);

            msgListView = new MessagesListView(uiService.getRootActivity());
            msgListView.addEventListener(this);

            tabHost = new MyTabHost(uiService.getRootActivity());
            LocalActivityManager lam = new LocalActivityManager(uiService.getRootActivity(), false);
            tabHost.setup(lam);
            TabHost.TabSpec spec;

            spec = tabHost.newTabSpec(SUB_TAB_NEW_MSG).setIndicator(SUB_TAB_NEW_MSG).setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String s) {
                    return newMsgView;
                }
            });
            tabHost.addTab(spec);
            spec = tabHost.newTabSpec(SUB_TAB_MSG_LIST).setIndicator(SUB_TAB_MSG_LIST).setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String s) {
                    return msgListView;
                }
            });
            tabHost.addTab(spec);
            uiService.addToGroup(TAB_NAME, tabHost);
        } catch (Exception e) {
            Log.e(TAG, "Creating view failed", e);
        }
    }

    @Stop
    public void stop() {
        uiService.remove(tabHost);
    }

    @Update
    public void update() {
        stop();
        start();
    }

    @Port(name="inMessage")
    public void messageIncoming(final Object inMessage) {
        if (D) Log.i(TAG, "BEGIN messageIncoming");
        final TextView tabTextView = (TextView) tabHost.getCurrentTabView().findViewById(R.id.title);

        if (inMessage instanceof Message) {
            uiService.getRootActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msgListView.addMessage((Message) inMessage, MessageType.IN);
                    if (tabTextView.getText().equals(SUB_TAB_NEW_MSG)) {
                        Toast.makeText(
                                uiService.getRootActivity(),
                                "Nouveau message reçu",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Log.w(TAG, "Cannot handle received object on port \"inMessage\". Received object: "+inMessage.getClass().getSimpleName());
        }
        if (D) Log.i(TAG, "END messageIncoming");
    }

    @Override
    public void onSend(Message msg) {
        if (D) Log.i(TAG, "BEGIN onSend");
        if (isPortBinded("outMessage")) {
            getPortByName("outMessage", MessagePort.class).process(msg);
            msgListView.addMessage(msg, MessageType.OUT);
            if (D) Log.i("Message sent: ", msg.toString());
        }
        if (D) Log.i(TAG, "END onSend");
    }
}
