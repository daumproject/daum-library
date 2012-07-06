package org.daum.library.android.daumauth;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import org.daum.library.android.daumauth.view.DaumAuthView;
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
@ComponentType
public class DaumAuthComponent extends AbstractComponentType implements DaumAuthView.OnClickListener {

    private static final String TAG = "DaumAuthComponent";
    private static final String TAB_NAME = "Connexion";
    private static final int CONNECTION_TIMEOUT = 1000*60; // one minute

    private KevoreeAndroidService uiService;
    private DaumAuthEngine engine;
    private static ChangeListener listener = new ChangeListener();

    @Start
    public void start() {
        uiService = UIServiceHandler.getUIService();

        initUI();

        getModelService().registerModelListener(new ModelListener() {
            @Override
            public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return false;
            }

            @Override
            public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return false;
            }

            @Override
            public void modelUpdated() {
                uiService.getRootActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ReplicaService replicatingService = getPortByName("service", ReplicaService.class);
                            ReplicaStore store = new ReplicaStore(replicatingService);
                            engine = new DaumAuthEngine(getNodeName(), store);

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
            }
        });

        DaumAuthView launcherView = new DaumAuthView(uiService.getRootActivity());
        launcherView.setOnClickListener(this);
        uiService.addToGroup(TAB_NAME, launcherView);
    }

    @Stop
    public void stop() {

    }

    @Update
    public void update() {
        initUI();
        Log.d(TAG, "component updated !!!!");
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

    public static ChangeListener getChangeListener() {
        return listener;
    }

    @Override
    public void onConnectionButtonClicked(String matricule, String password) {
        ProgressDialog pDialog = new ProgressDialog(uiService.getRootActivity());
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        new Thread(new ConnectionTask(matricule, password)).start();
    }

    private class ConnectionTask implements Runnable {

        private String matricule;
        private String password;

        public ConnectionTask(String matricule, String password) {
            this.matricule = matricule;
            this.password = password;
        }

        @Override
        public void run() {
            while (!engine.isSynced()) {} // wait until replica is synced
            if (engine.check(matricule, password)) {
                // matricule & password are ok

            } else {
                // wrong matricule and/or password

            }
        }
    }
}
