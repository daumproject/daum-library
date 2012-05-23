package org.daum.library.replicatingMap;

import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 18:23
 */
public class KevoreePortsImpl implements KPort {

    private  AbstractComponentType abstractComponentType;
    public KevoreePortsImpl(AbstractComponentType abstractComponentType){
        this.abstractComponentType = abstractComponentType;
    }
    @Override
    public void process(Event e) {

        abstractComponentType.getPortByName("messagetoSend", MessagePort.class).process(e);

    }
}
