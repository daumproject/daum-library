package org.daum.library.replicatingMap;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
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
        @RequiredPort(name = "messagetoSend", type = PortType.MESSAGE)
})
@Provides({
        @ProvidedPort(name = "remoteReceived", type = PortType.MESSAGE) ,
        @ProvidedPort(name = "service", type = PortType.SERVICE, className = ReplicatingService.class)
})
public class ReplicatingManager extends AbstractComponentType implements ReplicatingService {

    private CacheManager cacheManager =null;
    KChannelImpl kChannel = new KChannelImpl(this);
  //  private Thread thread = new Thread(this);
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Start
    public void start()
    {
        cacheManager = new CacheManager(kChannel,getNodeName());
        cacheManager.snapshot();
    }

    @Stop
    public void stop()
    {

    }

    @Update
    public void update()
    {

    }


    @Port(name = "remoteReceived")
    public void messageReceived(Object o)
    {
        cacheManager.remoteReceived(o);
    }


    @Port(name = "service", method = "getCache")
    @Override
    public Cache getCache(String name) {
        return cacheManager.getCache(name);
    }


}
