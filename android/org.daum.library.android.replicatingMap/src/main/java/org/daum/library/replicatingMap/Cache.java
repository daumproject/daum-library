package org.daum.library.replicatingMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 15:43
 */
public class Cache extends  DHashMap<Object,Object>{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String name = "";
    private CacheManager cacheManger=null;


    public Cache(String cachename,CacheManager cacheManger){
        this.name = cachename;
        this.cacheManger = cacheManger;
    }

    @Override
    public Object put(Object key, Object value) {
        Replicator e = new Replicator();
        e.op = Operation.ADD;
        e.key = key;
        e.value = value;
        e.cache = name;
        cacheManger.remoteDisptach(e);
        return super.put(key, value);
    }


    public void localDispatch(Replicator replicator)
    {
        logger.debug("Local dispatch");

        if (replicator.op.equals(Operation.ADD))
        {
            super.put(replicator.key, replicator.value);
        } else if (replicator.op.equals(Operation.DELETE))
        {
            super.remove(replicator.key);
        }
    }


    @Override
    public Object remove(Object key) {

        if(!cacheManger.isSynchronized())
        {
            logger.error("WARNING !! TODO backup remove until synchronize is finish");
        }
        Replicator e = new Replicator();
        e.op = Operation.DELETE;
        e.key =  key;
        e.cache = name;
        cacheManger.remoteDisptach(e);
        return super.remove(key);
    }

}
