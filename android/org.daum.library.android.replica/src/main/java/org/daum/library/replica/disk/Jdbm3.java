package org.daum.library.replica.disk;

import org.apache.jdbm.DB;
import org.apache.jdbm.DBMaker;
import org.daum.library.replica.cache.Cache;
import org.daum.library.replica.cache.VersionedValue;
import org.daum.library.replica.cluster.ICacheManger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.NavigableMap;
import java.util.SortedMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 31/07/12
 * Time: 11:15
 * To change this template use File | Settings | File Templates.
 */
public class Jdbm3 extends ADisk
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private DB db=null;
    private ICacheManger cacheManger=null;

    public  Jdbm3(ICacheManger cacheManger,String path)
    {
        this.cacheManger = cacheManger;
        File folder = new File(path);
        if(!folder.exists())
        {
            folder.mkdir();
            folder.canRead();
            folder.canWrite();
        }
        db  = DBMaker.openFile(path+ File.pathSeparatorChar + "store") .make();
        restoreFromDB();
    }

    public void restoreFromDB()
    {
        // restore
        for(String key_cache :   db.getCollections().keySet())
        {
            logger.debug("Reading cache from diskPersitence "+key_cache);

            Cache cache =      cacheManger.getCache(key_cache) ;
            SortedMap<String,VersionedValue> disk = (SortedMap<String, VersionedValue>) db.getCollections().get(key_cache);

            for(Object key_row : disk.keySet())
            {
                logger.debug("Reading object from diskPersitence "+key_row);

                cache.put(key_row,disk.get(key_row));
            }
        }
    }


    @Override
    public void commit() {
        db.commit();
    }

    @Override
    public void close() {
        db.close();
    }

    @Override
    public void rollback() {
        db.rollback();
    }

    @Override
    public NavigableMap<Comparable, Object> createCache(String cachename)
    {
        if(cacheManger.getCluster().isDiskPersitence())
        {
            if(db.getTreeMap(cachename) == null)
            {
               return db.createTreeMap(cachename);
            }else {
                return db.getTreeMap(cachename);
            }
        }
        return null;
    }
}
