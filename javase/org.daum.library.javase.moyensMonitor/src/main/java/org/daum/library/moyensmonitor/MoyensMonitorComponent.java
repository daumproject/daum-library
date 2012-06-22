package org.daum.library.moyensmonitor;

import org.daum.library.moyensmonitor.view.MoyensMonitorFrame;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 22/06/12
 * Time: 09:45
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "JavaSE")
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true)  ,
        @RequiredPort(name = "value", type = PortType.MESSAGE, optional = true)
})
@Provides({
        @ProvidedPort(name = "notify", type = PortType.MESSAGE)
})
@ComponentType
public class MoyensMonitorComponent extends AbstractComponentType {

    private MoyensMonitorFrame frame;

    @Start
    public void start() {
        ReplicaService replicatingService =   this.getPortByName("service", ReplicaService.class);
        ReplicaStore storeImpl = new ReplicaStore(replicatingService);

        frame = new MoyensMonitorFrame(getNodeName(), storeImpl);
        frame.setVisible(true);
    }

    @Stop
    public void stop() {
        if (frame != null) frame.dispose();
    }

    @Update
    public void update() {

    }

    @Port(name = "notify")
    public void notifybyReplica(Object m) {
        ChangeListener.getInstance().receive(m);
    }
}