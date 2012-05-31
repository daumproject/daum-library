package org.daum.library.android.agetac.messages;

import android.R;
import android.app.LocalActivityManager;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import org.daum.common.message.api.*;
import org.daum.library.android.agetac.messages.view.AmbianceMessagesView;
import org.daum.library.android.agetac.messages.view.MyTabHost;
import org.daum.library.android.agetac.messages.view.SimpleMessagesView;
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
    private static final String SUB_TAB_AMBIANCE = "Message d'ambiance";
    private static final String SUB_TAB_SIMPLE = "Liste des messages";

    private KevoreeAndroidService uiService;
    private AmbianceMessagesView ambianceMsgView;
    private SimpleMessagesView simpleMsgView;
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
            ambianceMsgView = new AmbianceMessagesView(uiService.getRootActivity());
            ambianceMsgView.addEventListener(this);

            simpleMsgView = new SimpleMessagesView(uiService.getRootActivity());
            simpleMsgView.addEventListener(this);

            tabHost = new MyTabHost(uiService.getRootActivity());
            LocalActivityManager lam = new LocalActivityManager(uiService.getRootActivity(), false);
            tabHost.setup(lam);
            TabHost.TabSpec spec;

            spec = tabHost.newTabSpec(SUB_TAB_AMBIANCE).setIndicator(SUB_TAB_AMBIANCE).setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String s) {
                    return ambianceMsgView;
                }
            });
            tabHost.addTab(spec);
            spec = tabHost.newTabSpec(SUB_TAB_SIMPLE).setIndicator(SUB_TAB_SIMPLE).setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String s) {
                    return simpleMsgView;
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

        if (inMessage instanceof AmbianceMessage) {
            uiService.getRootActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ambianceMsgView.setMessage((AmbianceMessage) inMessage);
                    if (tabTextView.getText().equals(SUB_TAB_SIMPLE)) {
                        Toast.makeText(
                                uiService.getRootActivity(),
                                "Nouveau message d'ambiance reçu",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (inMessage instanceof SimpleMessage) {
            uiService.getRootActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    simpleMsgView.addMessage((SimpleMessage) inMessage);
                    if (tabTextView.getText().equals(SUB_TAB_AMBIANCE)) {
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
    public void onSend(IMessage msg) {
        if (D) Log.i(TAG, "BEGIN onSend");
        if (isPortBinded("outMessage")) {
            getPortByName("outMessage", MessagePort.class).process(msg);
            if (D) Log.i("Message sent: ", msg.toString());
        }
        if (D) Log.i(TAG, "END onSend");
    }
}
