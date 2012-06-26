package org.daum.library.replica.channel;

import org.daum.library.replica.msg.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 18:23
 */
public class KChannelImpl implements Channel {

    private  AbstractComponentType abstractComponentType;
    public KChannelImpl(AbstractComponentType abstractComponentType){
        this.abstractComponentType = abstractComponentType;
    }

    @Override
    public void write(Message e)
    {
        if(e instanceof Update ||(e instanceof Updates) || (e instanceof Snapshot) || (e instanceof Command)){
            abstractComponentType.getPortByName("broadcast", MessagePort.class).process(e);
        } else if(e instanceof  NotifyUpdate | e instanceof SyncEvent)
        {
            abstractComponentType.getPortByName("notification", MessagePort.class).process(e);
        }
    }

}
