package org.daum.library.android.sitac;

import org.daum.library.android.sitac.controller.ISITACController;
import org.daum.library.android.sitac.view.SITACView;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
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
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true)  ,
        @RequiredPort(name = "value", type = PortType.MESSAGE, optional = true)
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
        ReplicaService replicatingService =   this.getPortByName("service", ReplicaService.class);
        final ReplicaStore storeImpl = new ReplicaStore(replicatingService);

        uiService = UIServiceHandler.getUIService();
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SITACView sitacView = new SITACView(uiService.getRootActivity(), getNodeName(), storeImpl);
                sitacCtrl = sitacView.getController();
                uiService.addToGroup("SITAC", sitacView);
            }
        });
    }

    @Stop
    public void stop() {
    }

    @Update
    public void update() {
        stop();
        start();
    }

    @Port(name = "notify")
    public void notifiedByReplica(Object m) {
        ChangeListener.getInstance().receive(m);
    }
}
