package org.daum.library.android.daumauth;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.daum.library.android.daumauth.controller.IController;
import org.daum.library.android.daumauth.listener.OnConnectionListener;
import org.daum.library.android.daumauth.listener.OnInterventionSelectedListener;
import org.daum.library.android.daumauth.util.GenerateModelHelper;
import org.daum.library.android.daumauth.view.DaumAuthView;

import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.android.daumauth.DaumAuthEngine.OnStoreSyncedListener;

import org.kevoree.*;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.DictionaryAttribute;
import org.kevoree.annotation.DictionaryType;
import org.kevoree.annotation.Port;
import org.kevoree.annotation.PortType;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.api.service.core.script.KevScriptEngine;
import org.kevoree.framework.AbstractComponentType;

import org.sitac.Intervention;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import scala.Option;
import scala.Some;

import java.util.List;


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
    private static final String TEXT_MODEL_GEN = "Chargement du modèle de données...";

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

    /**
     * Display "msg" in a Toast
     *
     * @param msg
     */
    private void showToast(final String msg, final int duration) {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (msg != null) {
                    Toast.makeText(uiService.getRootActivity(), msg, duration).show();
                }
            }
        });
    }

    @Override
    public void onConnected() {
    }

    @Override
    public void onConnectionRefused() {
        showToast(TEXT_CONN_FAILED, Toast.LENGTH_SHORT);
    }

    @Override
    public void onConnectionTimedout() {
        showToast(TEXT_CONN_TIMEDOUT, Toast.LENGTH_SHORT);
    }

    @Override
    public void onInterventionSelected(final Intervention inter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    controller.showDialog(TEXT_MODEL_GEN, false);
                    generateModel(inter);
                    uiService.remove(view);

                } catch (Exception e) {
                    logger.error("Error while generating model", e);
                } finally {
                    controller.dismissDialog();
                }
            }
        }).start();
    }

    private void generateModel(Intervention inter) throws Exception {
        KevScriptEngine engine = getKevScriptEngineFactory().createKevScriptEngine();

        // retrieving binded chans for replica's ports
        String replicaNotifChanName = "";
        String replicaServiceChanName = "";
        ContainerRoot currentModel = getModelService().getLastModel();
        ContainerNode contNode = ((ContainerNode) getModelElement().eContainer());

        for (ComponentInstance component : contNode.getComponentsForJ()) {
            // TODO maybe find a better way to do this ?
            if (component.getTypeDefinition().getName().equals("Replica")) {
                GenerateModelHelper gmHelper = new GenerateModelHelper();
                Option<String> optionService = gmHelper.findChannel(component.getName(), "service", getNodeName(), currentModel);
                replicaServiceChanName = optionService.get();
                Option<String> optionNotify = gmHelper.findChannel(component.getName(), "notification", getNodeName(), currentModel);
                replicaNotifChanName = optionNotify.get();
            }
        }

        // variables
        engine.addVariable("compName", getName());
        engine.addVariable("nodeName", getNodeName());
        engine.addVariable("replicaNotifChanName", replicaNotifChanName);
        engine.addVariable("replicaServiceChanName", replicaServiceChanName);
//        engine.addVariable("interNum", interNum);

        // kevScript model for SITAC, Moyens & Messages
        engine.append("merge 'mvn:http://maven.kevoree.org/daum/snapshots!" +
                "org.daum.library.android/org.daum.library.android.sitac/1.8.3-SNAPSHOT'");
        engine.append("merge 'mvn:http://maven.kevoree.org/daum/snapshots!" +
                "org.daum.library.android/org.daum.library.android.messages/1.8.2-SNAPSHOT'");
        engine.append("merge 'mvn:http://maven.kevoree.org/daum/snapshots!" +
                "org.daum.library.android/org.daum.library.android.moyens/1.8.2-SNAPSHOT'");

        engine.append("addComponent sitacComp@{nodeName} : SITACComponent {}");
        engine.append("addComponent moyensComp@{nodeName} : MoyensComponent {}");
        engine.append("addComponent msgComp@{nodeName} : MessagesComponent {}");

        engine.append("bind sitacComp.service@{nodeName} => {replicaServiceChanName}");
        engine.append("bind sitacComp.notify@{nodeName} => {replicaNotifChanName}");
        engine.append("bind moyensComp.service@{nodeName} => {replicaServiceChanName}");
        engine.append("bind moyensComp.notify@{nodeName} => {replicaNotifChanName}");
        engine.append("bind msgComp.service@{nodeName} => {replicaServiceChanName}");
        engine.append("bind msgComp.notify@{nodeName} => {replicaNotifChanName}");

        engine.append("updateDictionary socketChan {port='9001'}@{nodeName}");
//        engine.append("updateDictionary sitacComp {interNum='{interNum}'}@{nodeName}");
//        engine.append("updateDictionary moyensComp {interNum='{interNum}'}@{nodeName}");
//        engine.append("updateDictionary msgComp {interNum='{interNum}'}@{nodeName}");

        // remove daumAuthComp
        engine.append("removeComponent {compName}@{nodeName}");

        engine.interpretDeploy();
    }

    @Port(name = "notify")
    public void notifiedByReplica(final Object m) {
        DaumAuthComponent.getChangeListener().receive(m);
    }

    @Override
    public void onStoreSynced() {
        this.storeSynced = true;
    }

    public void onInterventionsUpdated() {
        controller.updateUI();
    }

    public static ChangeListener getChangeListener() {
        return listener;
    }
}
