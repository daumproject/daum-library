package org.daum.library.replicatingMap;

import org.daum.library.replicatingMap.msg.Update;
import org.daum.library.replicatingMap.utils.SystemTime;
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
    private SystemTime systemTime = new SystemTime();
    private  int MaxEntriesLocalHeap;

    public Cache(String cachename,CacheManager cacheManger){
        this.name = cachename;
        this.cacheManger = cacheManger;
    }

    public void setMaxEntriesLocalHeap(int max){
        this.MaxEntriesLocalHeap = max;
    }

    @Override
    public Object put(Object key, Object value) {
        if(this.size() > MaxEntriesLocalHeap)
        {
            // todo
            logger.warn("The Max Entries Local Heap is reached ");
            return null;
        }else
        {
            Update e = new Update();
            e.op = StoreRequest.ADD;
            e.key = key;
            e.value = value;
            e.cache = name;
            cacheManger.remoteDisptach(e);
            return super.put(key, value);
        }
    }


    public void localDispatch(Update replica)
    {
        logger.debug("Local dispatch "+replica.getCache());
        if (replica.op.equals(StoreRequest.ADD))
        {
            super.put(replica.key, replica.value);
        } else if (replica.op.equals(StoreRequest.DELETE))
        {
            super.remove(replica.key);
        }
    }


    @Override
    public Object remove(Object key) {

        if(!cacheManger.isSynchronized())
        {
            //logger.warn("WARNING !! TODO backup remove until synchronize is finish");
        }
        Update e = new Update();
        e.op = StoreRequest.DELETE;
        e.key =  key;
        e.cache = name;
        cacheManger.remoteDisptach(e);
        return super.remove(key);
    }

}
