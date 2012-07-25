package org.daum.library.replica.cache;

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
public class Cache extends DHashMap<Object,VersionedValue> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String name = "";
    private CacheManager cacheManger=null;
    private SystemTime systemTime = new SystemTime();
    private int MaxEntriesLocalHeap=8000;


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
            //todo
            logger.warn("The Max Entries Local Heap is reached ");
            return null;
        }else
        {
            Update e = new Update();
            e.key = key;
            e.cache = name;

            VersionedValue updated=new VersionedValue();
            VersionedValue old = super.get(key);
            if(old == null)
            {
                e.event = StoreEvent.ADD;
                updated.setValue(value);
                e.setVersionedValue(updated);
            } else
            {
                // update
                e.event = StoreEvent.UPDATE;
                updated.setVersion(old.getVersion());
                updated.updated();
                updated.setValue(value);
                e.setVersionedValue(updated);
            }

            // local
            if(updated == null)
            {
                logger.error("update is null");
            } else
            {
                super.put(key, updated);
            }

            // remote
            cacheManger.remoteDisptach(e);
            return  updated;
        }
    }


    @Override
    public VersionedValue get(Object key) {
        VersionedValue versionedValue = (VersionedValue) super.get(key);
        return versionedValue;
    }


    public void localDispatch(Update replica)
    {
    //    logger.debug("Local dispatch "+name);
        try
        {
            if (replica.event.equals(StoreEvent.ADD) || replica.event.equals(StoreEvent.UPDATE))
            {
                VersionedValue old = get(replica.key);
                if(old == null)
                {
                    super.put(replica.key, replica.getVersionedValue());
                }
                else
                {
                    if(replica.getVersionedValue() != null)
                    {
                        if(replica.getVersionedValue().getVersion() > old.getVersion())
                        {
                            super.put(replica.key, replica.getVersionedValue());
                        } else
                        {
                            logger.warn("Current version "+replica.getVersionedValue().getVersion()+" replica version ="+old.getVersion());
                        }
                    } else {
                        logger.error("not versionned "+old.getVersion());
                    }
                }

            } else if (replica.event.equals(StoreEvent.DELETE))
            {
                if(super.get(replica.key) != null)
                {
                    super.remove(replica.key);
                }

            }
        } catch (Exception e){
            logger.error("Local dispatch ",e);
        }

    }


    @Override
    public VersionedValue remove(Object key) {

        // TODO backup remove until synchronize is finish");
        Update e = new Update();
        e.event = StoreEvent.DELETE;
        e.key =  key;
        e.cache = name;
        VersionedValue last = super.remove(key);
        cacheManger.remoteDisptach(e);
        return   last;
    }

}
