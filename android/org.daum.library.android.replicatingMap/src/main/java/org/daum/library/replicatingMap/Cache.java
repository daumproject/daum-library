package org.daum.library.replicatingMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 15:43
 */
public class Cache extends  ConcurrentHashMap<Object, Object> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String cachename= "";
    private CacheManager cacheManger=null;

    public Cache(String cachename,CacheManager cacheManger){
        this.cachename = cachename;
        this.cacheManger = cacheManger;

    }

   	public Object put(String key, Object value) {
        Event e = new Event();
        e.op = Event.Operation.ADD;
        e.key = key;
        e.value = value;
        e.cache = cachename;
        cacheManger.remoteDisptach(e);
        return super.put(key, value);
    }


    public void processEvent(Event msg)
    {

        if (msg.op.equals(Event.Operation.ADD))
        {
            put(msg.key, msg.value);

        } else if (msg.op.equals(Event.Operation.DELETE))
        {
            remove(msg.key);
        }
    }


    @Override
   	public Object remove(Object key) {

        logger.debug("Local Received "+Event.Operation.DELETE);

        Event e = new Event();
        e.op = Event.Operation.DELETE;
        e.key = (String) key;
        e.cache = cachename;
        cacheManger.remoteDisptach(e);
        return super.remove(key);
    }

}
