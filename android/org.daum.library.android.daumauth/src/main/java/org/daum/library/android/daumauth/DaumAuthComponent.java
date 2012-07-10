package org.daum.library.android.daumauth;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.daum.library.android.daumauth.controller.IController;
import org.daum.library.android.daumauth.listener.OnConnectionListener;
import org.daum.library.android.daumauth.listener.OnInterventionSelectedListener;
import org.daum.library.android.daumauth.view.DaumAuthView;

import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.android.daumauth.DaumAuthEngine.OnStoreSyncedListener;

import org.kevoree.ContainerRoot;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.api.service.core.script.KevScriptEngine;
import org.kevoree.framework.AbstractComponentType;

import org.sitac.Intervention;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/07/12
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Android")
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true)
})
@Provides({
        @ProvidedPort(name = "notify", type = PortType.MESSAGE)
})
@DictionaryType({
        @DictionaryAttribute(name = "connTimeout", defaultValue = "15000", optional = false)
})
@ComponentType
public class DaumAuthComponent extends AbstractComponentType
        implements OnStoreSyncedListener, OnConnectionListener, OnInterventionSelectedListener {

    private static final String TAG = "DaumAuthComponent";
    private static final Logger logger = LoggerFactory.getLogger(DaumAuthComponent.class);

    // Text string constants
    private static final String TAB_NAME = "Connexion";
    private static final String TEXT_CONN_FAILED = "Mauvais matricule et/ou mot de passe";
    private static final String TEXT_CONN_TIMEDOUT = "Impossible de se connecter, réessayez plus tard.";

    private static final int CONNECTION_TIMEOUT = 1000*15; // 15 seconds

    private KevoreeAndroidService uiService;
    private DaumAuthView view;
    private DaumAuthEngine engine;
    private IController controller;
    private int connTimeout = CONNECTION_TIMEOUT;
    private static ChangeListener listener = new ChangeListener();
    private boolean storeSynced = false;
    private DaumAuthView.State viewState = DaumAuthView.State.NOT_CONNECTED;

    @Start
    public void start() {
        uiService = UIServiceHandler.getUIService();
        int value = Integer.parseInt(getDictionary().get("connTimeout").toString());
        if (value >= 0) connTimeout = value;
        else logger.warn(TAG, "Dictionary connTimeout value must be >= 0 (set to default: "+CONNECTION_TIMEOUT+")");

        initUI();

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
                uiService.getRootActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ReplicaService replicatingService = getPortByName("service", ReplicaService.class);
                            ReplicaStore store = new ReplicaStore(replicatingService);
                            engine = new DaumAuthEngine(getNodeName(), store, storeSynced);
                            engine.setOnStoreSyncedListener(DaumAuthComponent.this);
                            controller.setConnectionEngine(engine);
                            controller.setInterventionEngine(engine);

                        } catch (PersistenceException e) {
                            Log.e(TAG, "Error on component startup", e);
                        }
                    }
                });
            }
        });

    }

    private void initUI() {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Window window = uiService.getRootActivity().getWindow();
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                view = new DaumAuthView(uiService.getRootActivity(), viewState);
                view.setOnConnectionListener(DaumAuthComponent.this);
                view.setOnInterventionSelectedListener(DaumAuthComponent.this);
                controller = view.getController();
                controller.setTimeout(connTimeout);

                uiService.addToGroup(TAB_NAME, view);
            }
        });
    }

    @Stop
    public void stop() {
        Log.w(TAG, "COMP method: stop()");
        uiService.remove(view);
    }

    @Update
    public void update() {
        Log.w(TAG, "COMP method: update()");
        viewState = view.getState();
        stop();
        start();
    }

    private void showDialog(final ProgressDialog dialog) {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    /**
     * Close the given progress dialog and display "msg" in a Toast
     *
     * @param msg
     */
    private void showToast(final String msg) {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (msg != null) {
                    Toast.makeText(uiService.getRootActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnected() {
        Log.w(TAG, "Authentication succeeded !");
    }

    @Override
    public void onConnectionRefused() {
        showToast(TEXT_CONN_FAILED);
    }

    @Override
    public void onConnectionTimedout() {
        showToast(TEXT_CONN_TIMEDOUT);
    }

    @Override
    public void onInterventionSelected(Intervention inter) {
        // TODO  give parameters to components for the right intervention
        generateModel();
    }

    private void generateModel() {
        KevScriptEngine engine = getKevScriptEngineFactory().createKevScriptEngine();

        // node name variable
        engine.addVariable("nodeName", getNodeName());

        // kevScript model for SITAC, Moyens & Messages
        engine.append("merge 'mvn:org.daum.library.android/org.daum.library.android.sitac/1.8.3-SNAPSHOT'");
        engine.append("merge 'mvn:org.daum.library.android/org.daum.library.android.messages/1.8.2-SNAPSHOT'");
        engine.append("merge 'mvn:org.daum.library.android/org.daum.library.android.moyens/1.8.2-SNAPSHOT'");

        engine.append("addComponent sitacComp@{nodeName} : SITACComponent {}");
        engine.append("addComponent replicaComp@{nodeName} : Replica {}");
        engine.append("addComponent moyensComp@{nodeName} : MoyensComponent {}");
        engine.append("addChannel defServ0 : defSERVICE {}");
        engine.append("addChannel socketChan : SocketChannel {port='9001',replay='false',maximum_size_messaging='50',timer='2000'}");
        engine.append("addChannel defMsg0 : defMSG {}");
        engine.append("bind sitacComp.service@{nodeName} => defServ0");
        engine.append("bind replicaComp.service@{nodeName} => defServ0");
        engine.append("bind replicaComp.remote@{nodeName} => socketChan");
        engine.append("bind replicaComp.broadcast@{nodeName} => socketChan");
        engine.append("bind moyensComp.service@{nodeName} => defServ0");
        engine.append("bind replicaComp.notification@{nodeName} => defMsg0");
        engine.append("bind moyensComp.notify@{nodeName} => defMsg0");
        engine.append("bind sitacComp.notify@{nodeName} => defMsg0");

        engine.interpretDeploy();
    }

    @Port(name = "notify")
    public void notifiedByReplica(final Object m) {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DaumAuthComponent.getChangeListener().receive(m);
            }
        });
    }

    @Override
    public void onStoreSynced() {
        this.storeSynced = true;
    }

    public static ChangeListener getChangeListener() {
        return listener;
    }
}
