package org.daum.library.replicatingMap;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

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
        @ProvidedPort(name = "messageReceived", type = PortType.MESSAGE) ,
        @ProvidedPort(name = "ReplicatingService", type = PortType.SERVICE, className = ReplicatingService.class)

})
public class ReplicatingManager extends AbstractComponentType implements  ReplicatingService {

    ReplicatingMap replicatingMap = new ReplicatingMap(this);

    @Start
    public void start() {

    }

    @Stop
    public void stop()
    {

    }


    @Update
    public void update()
    {

    }

    @Port(name = "messageReceived")
    public void messageReceived(Object o)
    {
        replicatingMap.messageReceived(o);
    }



    @Port(name = "ReplicatingService", method = "getReplicatingMap")
    public ReplicatingMap getReplicatingMap() {

        return replicatingMap;
    }



}
