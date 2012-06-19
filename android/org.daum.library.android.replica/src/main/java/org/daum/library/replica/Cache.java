package org.daum.library.replica;

import org.daum.library.replica.msg.Update;
import org.daum.library.replica.utils.SystemTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 15:43
 */
public class Cache extends  DHashMap<Object,VersionedValue>{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String name = "";
    private CacheManager cacheManger=null;
    private SystemTime systemTime = new SystemTime();
    private  int MaxEntriesLocalHeap=1000;


    public Cache(String cachename,CacheManager cacheManger)
    {
        this.name = cachename;
        this.cacheManger = cacheManger;
    }

    public void setMaxEntriesLocalHeap(int max)
    {
        this.MaxEntriesLocalHeap = max;
    }

    public VersionedValue put(Object key, Object value) {

        if(this.size() > MaxEntriesLocalHeap)
        {
            // todo
            logger.warn("The Max Entries Local Heap is reached ");
            return null;
        }else
        {

            Update e = new Update();
            VersionedValue version = new VersionedValue(value);

            e.op = StoreRequest.ADD;
            e.key = key;
            e.setVersionedValue(version);
            e.cache = name;
            // remote
            cacheManger.remoteDisptach(e);
            // local
            return super.put(key, version);
        }
    }


    @Override
    public VersionedValue get(Object key) {
        VersionedValue versionedValue = (VersionedValue) super.get(key);
        return versionedValue;
    }


    public void localDispatch(Update replica)
    {
        logger.debug("Local dispatch "+replica.getCache());

        if (replica.op.equals(StoreRequest.ADD))
        {
            VersionedValue old = (VersionedValue) super.get(replica.key);
            if(old == null)
            {
                super.put(replica.key, replica.getVersionedValue());
            }
            else
            {
                // compare version
                if(old.before(replica.getVersionedValue()))
                {
                    super.put(replica.key, replica.getVersionedValue());
                } else
                {
                    logger.debug("receive old version ");
                }
            }

        } else if (replica.op.equals(StoreRequest.DELETE))
        {
            super.remove(replica.key);
        }
    }


    @Override
    public VersionedValue remove(Object key) {
        //logger.warn("WARNING !! TODO backup remove until synchronize is finish");
        Update e = new Update();
        e.op = StoreRequest.DELETE;
        e.key =  key;
        e.cache = name;
        cacheManger.remoteDisptach(e);
        return super.remove(key);
    }

}
