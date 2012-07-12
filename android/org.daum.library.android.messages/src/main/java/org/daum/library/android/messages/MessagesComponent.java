package org.daum.library.android.messages;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.Toast;
import org.daum.common.message.api.Message;
import org.daum.library.android.messages.view.MessagesView;
import org.daum.library.android.messages.view.ListItemView.MessageType;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
import org.kevoree.ContainerRoot;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.daum.library.android.messages.listener.IMessagesListener;

import java.util.Collection;


/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 15/05/12
 * Time: 14:59
 */

@Library(name = "Android")
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = false)
})
@Provides({
        @ProvidedPort(name = "notify", type = PortType.MESSAGE)
})
@DictionaryType({
        @DictionaryAttribute(name = "interNum", optional = false)
})
@ComponentType
public class MessagesComponent extends AbstractComponentType
        implements IMessagesListener, MessagesEngine.OnEventListener {

    // Debugging
    private static final String TAG = "MessagesComponent";
    private static boolean D = true;

    private static final String TAB_NAME = "Messages";

    private KevoreeAndroidService uiService;
    private MessagesView messagesView;
    private MessagesEngine engine;

    private static ChangeListener singleton=null;

    public static ChangeListener getChangeListenerInstance(){
        if(singleton == null){
            singleton = new ChangeListener();
        }
        return singleton;
    }


    @Start
    public void start() {
        this.uiService = UIServiceHandler.getUIService();

        getModelService().registerModelListener(new ModelListener() {
            @Override
            public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public void modelUpdated() {
                try {
                    ReplicaService replicatingService = getPortByName("service", ReplicaService.class);
                    ReplicaStore store = new ReplicaStore(replicatingService);
                    engine = new MessagesEngine(store, getNodeName());
                    engine.setOnEventListener(MessagesComponent.this);

                } catch (PersistenceException e) {
                    Log.e(TAG, "Error while initializing ReplicaStore", e);
                }
            }
        });

        initUI();
    }

    private void initUI() {
        try {
            uiService.getRootActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Window window = uiService.getRootActivity().getWindow();
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    messagesView = new MessagesView(uiService.getRootActivity(), getNodeName());
                    messagesView.addEventListener(MessagesComponent.this);

                    uiService.addToGroup(TAB_NAME, messagesView);
                }
            });
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

    @Port(name="notify")
    public void notifiedByReplica(final Object m) {
        MessagesComponent.getChangeListenerInstance().receive(m);
    }

    @Override
    public void onSend(Message msg) {
        engine.add(msg);
    }

    @Override
    public void onAdd(final Message msg, final MessageType type) {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesView.addMessage(msg, type);
            }
        });
    }

    @Override
    public void onReplicaSynced(final Collection<Message> messages) {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Message m : messages) {
                    // FIXME maybe change the way IN/OUT are determined because if the replica's id change..
                    // this is going to crash pretty surely
                    String id = m.getId().split("\\+")[0];
                    Log.d(TAG, "replica synced: id >> "+id);
                    if (id.equals(getNodeName())) {
                        messagesView.addMessage(m, MessageType.OUT);

                    } else {
                        messagesView.addMessage(m, MessageType.IN);
                    }
                }
            }
        });
    }
}
