package org.daum.library.replica.cache;

import org.apache.jdbm.DB;
import org.daum.library.replica.msg.Update;
import org.daum.library.replica.utils.SystemTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedMap;

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
    private SortedMap<String,VersionedValue> disk;
    private DB db =null;

    public Cache(String cachename,CacheManager cacheManger)
    {
        this.name = cachename;
        this.cacheManger = cacheManger;

        if(cacheManger.getCluster().isDiskPersitence())
        {
            db =  cacheManger.getCluster().getDb();
            if(cacheManger.getCluster().getDb().getTreeMap(cachename) == null)
            {
                disk = db.createTreeMap(cachename);
            }else {
                disk =db.getTreeMap(cachename);
            }
        }

    }

    public void setMaxEntriesLocalHeap(int max)
    {
        this.MaxEntriesLocalHeap = max;
    }

    public VersionedValue put(Object key, Object value) {

        try{
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

                if(cacheManger.getCluster().isDiskPersitence()){
                    disk.put(key.toString(),updated);
                    db.commit();
                }
                return  updated;
            }
        }catch (Exception e )
        {
            if(cacheManger.getCluster().isDiskPersitence()){
                db.rollback();
            }

            return null;
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

            if(cacheManger.getCluster().getDb().getTreeMap(replica.getCache()) == null)
            {
                disk = db.createTreeMap(replica.getCache());
            }else {
                disk =db.getTreeMap(replica.cache);
            }


            if (replica.event.equals(StoreEvent.ADD) || replica.event.equals(StoreEvent.UPDATE))
            {
                VersionedValue old = get(replica.key);
                if(old == null)
                {
                    // disk
                    if(cacheManger.getCluster().isDiskPersitence())
                    {
                        disk.put(replica.key.toString(), replica.getVersionedValue());
                    }
                    // memory
                    super.put(replica.key, replica.getVersionedValue());

                }
                else
                {
                    if(replica.getVersionedValue() != null)
                    {
                        if(replica.getVersionedValue().getVersion() > old.getVersion())
                        {
                            //disk
                            if(cacheManger.getCluster().isDiskPersitence()){
                                disk.put(replica.key.toString(), replica.getVersionedValue());
                            }
                            // memory
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
                    if(cacheManger.getCluster().isDiskPersitence()){
                        disk.remove(replica.key.toString());
                    }
                    super.remove(replica.key);
                }

            }

            if(cacheManger.getCluster().isDiskPersitence()){
                db.commit();
            }

        } catch (Exception e)
        {
            logger.error("Local dispatch ",e);
            if(cacheManger.getCluster().isDiskPersitence()){
                db.rollback();
            }

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
        if(cacheManger.getCluster().isDiskPersitence())
        {
            disk.remove(key.toString());
            db.commit();
        }

        return   last;
    }

}
