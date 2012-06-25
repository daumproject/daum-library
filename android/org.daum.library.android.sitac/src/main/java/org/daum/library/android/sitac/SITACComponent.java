package org.daum.library.android.sitac;

import android.util.Log;
import org.daum.library.android.sitac.controller.ISITACController;
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

    private static final String TAG = "STIACComponent";

    private KevoreeAndroidService uiService;
    private ISITACController sitacCtrl;

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
                                final ReplicaStore storeImpl = new ReplicaStore(replicatingService);
                                final SITACView sitacView = new SITACView(uiService.getRootActivity(), getNodeName(), storeImpl);
                                sitacCtrl = sitacView.getController();
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
    public void notifiedByReplica(Object m) {
        ChangeListener.getInstance().receive(m);
    }
}
