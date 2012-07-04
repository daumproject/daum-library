package org.daum.library.android.sitac;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.daum.library.android.sitac.view.SITACView;
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
 * Date: 08/06/12
 * Time: 09:36
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
public class SITACComponent extends AbstractComponentType {

    private static final String TAG = "SITACComponent";

    private KevoreeAndroidService uiService;
    private ReplicaStore store;

    @Start
    public void start() {
        uiService = UIServiceHandler.getUIService();

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
                                store = new ReplicaStore(replicatingService);

                                SITACView sitacView = new SITACView(uiService.getRootActivity(), getNodeName(), store);
                                uiService.addToGroup("SITAC", sitacView);

                            } catch (PersistenceException e) {
                                Log.e(TAG, "Error while initializing ReplicaStore", e);
                            }
                        }
                    });
            }
        });
    }

    @Stop
    public void stop() {

    }

    @Update
    public void update() {

    }

    @Port(name = "notify")
    public void notifiedByReplica(final Object m) {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChangeListener.getInstance().receive(m);
            }
        });
    }
}
