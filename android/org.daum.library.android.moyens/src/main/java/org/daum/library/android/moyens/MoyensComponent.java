package org.daum.library.android.moyens;

import android.util.Log;
import org.daum.common.model.api.Demand;
import org.daum.library.android.moyens.view.MoyensView;
import org.daum.library.android.moyens.view.listener.IMoyensListener;
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

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 15/05/12
 * Time: 14:59
 */



@Library(name = "Android")
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true)
})
@Provides({
        @ProvidedPort(name = "notify", type = PortType.MESSAGE)
})
@ComponentType
public class MoyensComponent extends AbstractComponentType implements IMoyensListener, MoyensEngine.OnEventListener {

    // Debugging
    private static final String TAG = "MoyensComponent";

    // String constants
    private static final String TAB_NAME = "Moyens";

    private KevoreeAndroidService uiService;
    private MoyensView moyensView;
    private MoyensEngine engine;


    @Start
    public void start() {
        this.uiService = UIServiceHandler.getUIService();

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
                try {
                    ReplicaService replicatingService = getPortByName("service", ReplicaService.class);
                    ReplicaStore storeImpl = new ReplicaStore(replicatingService);

                    engine = new MoyensEngine(storeImpl, getNodeName());
                    engine.setOnEventListener(MoyensComponent.this);

                } catch (PersistenceException e) {
                    Log.e(TAG, "Error while initializing ReplicaStore", e);
                }
            }
        });


    }

    private void initUI() {
        try {
            uiService.getRootActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    moyensView = new MoyensView(uiService.getRootActivity());
                    moyensView.addEventListener(MoyensComponent.this);
                    uiService.addToGroup(TAB_NAME, moyensView);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Creating view failed", e);
        }
    }

    @Stop
    public void stop() {
        uiService.remove(moyensView);
    }

    @Update
    public void update() {
        stop();
        start();
    }

    @Port(name="notify")
    public void notifiedByReplica(final Object m) {
        ChangeListener.getInstance().receive(m);
    }

    @Override
    public void onDemandAsked(Demand newDemand) {
        Log.d(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!! onDemandAsked "+newDemand);
        engine.add(newDemand);
    }

    @Override
    public void onAdd(final Demand d) {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "?????????? onAdd event si si");
                moyensView.addDemand(d);
            }
        });
    }

    @Override
    public void onUpdate(final Demand d) {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "?????????? onUpdate event si si");
                moyensView.updateDemand(d);
            }
        });
    }

    @Override
    public void onReplicaSynced(final Collection<Demand> data) {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "?????????? onReplicaSynced event si si");
                moyensView.addDemands(new ArrayList<Demand>(data));
            }
        });
    }
}
