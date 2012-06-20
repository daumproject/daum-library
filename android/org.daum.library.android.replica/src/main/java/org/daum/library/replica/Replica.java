package org.daum.library.replica;

import org.daum.library.replica.msg.Command;
import org.daum.library.replica.msg.Message;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 22/05/12
 * Time: 18:00
 */

@Library(name = "JavaSE", names = {"Android"})
@ComponentType
@Requires({
        @RequiredPort(name = "broadcast", type = PortType.MESSAGE),
        @RequiredPort(name = "notification", type = PortType.MESSAGE,optional = true)
})
@Provides({
        @ProvidedPort(name = "remote", type = PortType.MESSAGE) ,
        @ProvidedPort(name = "service", type = PortType.SERVICE, className = ReplicatingService.class)
})
public class Replica extends AbstractComponentType implements ReplicatingService {

    KChannelImpl kChannel=null;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private  ICluster cluster;
    @Start
    public void start()
    {
        kChannel   = new KChannelImpl(this);
        Node node = new Node(getNodeName());
        cluster = new ClusterImpl(node,kChannel);

        // request synchronize
        cluster.synchronize();
    }

    @Stop
    public void stop()
    {
        cluster.shutdown();
    }

    @Update
    public void update()
    {
        kChannel   = new KChannelImpl(this);
        cluster.setChanel(kChannel);
    }

    @Port(name = "remote")
    public void remote(Object o)
    {
        if(o instanceof Message)
        {
            cluster.remoteReceived((Message)o);
        }
    }

    @Port(name = "service", method = "getCache")
    @Override
    public Cache getCache(String name) {
        return cluster.getCacheManager().getCache(name);
    }


}
