package org.daum.library.replicatingMap;

import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 22/05/12
 * Time: 17:50
 */


public class ReplicatingMap extends ConcurrentHashMap<String, Object> {

   private  AbstractComponentType componentType=null;


    public ReplicatingMap(AbstractComponentType componentType){
     this.componentType = componentType;
    }

    public  ReplicatingMap(){

    }
    public void messageReceived(Object o){

        if(o instanceof Event)
        {
            if (((Event) o).op.equals(Event.Operation.ADD))
            {
                this.put(((Event) o).key, ((Event) o).value);

            } else if (((Event) o).op.equals(Event.Operation.DELETE))
            {
                this.remove(((Event) o).key);
            }
        }
    }
    @Override
    public Object put(String key, Object value) {
        Event e = new Event();
        e.op = Event.Operation.ADD;
        e.key = key;
        e.value = value;

       if(componentType != null){
        componentType.getPortByName("messagetoSend", MessagePort.class).process(e);
       }

        return super.put(key, value);
    }

    @Override
    public Object remove(Object key) {

        Event e = new Event();
        e.op = Event.Operation.DELETE;
        e.key = (String) key;

        // write msg
        if(componentType != null){
           componentType.getPortByName("messagetoSend", MessagePort.class).process(e);
          }

        return super.remove(key);
    }


}
