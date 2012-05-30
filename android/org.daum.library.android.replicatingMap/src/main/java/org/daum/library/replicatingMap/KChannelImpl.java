package org.daum.library.replicatingMap;

import org.daum.library.replicatingMap.msg.Command;
import org.daum.library.replicatingMap.msg.Message;
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
    public void write(Message e) {

        abstractComponentType.getPortByName("messagetoSend", MessagePort.class).process(e);

        if(!(e instanceof Command))
        {
            abstractComponentType.getPortByName("trigger", MessagePort.class).process("tick");
        }

    }

}
