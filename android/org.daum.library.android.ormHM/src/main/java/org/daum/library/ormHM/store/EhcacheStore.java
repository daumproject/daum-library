package org.daum.library.ormHM.store;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import org.daum.library.ormHM.api.PersistenceSessionStore;
import org.daum.library.ormHM.persistence.OrhmID;
import org.daum.library.ormHM.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 14:03
 */
public class EhcacheStore implements PersistenceSessionStore {

    private static Logger logger =  LoggerFactory.getLogger(EhcacheStore.class);

    // todo use global cache configuration
    private CacheConfiguration cacheConfiguration=null;

    private CacheManager cacheManager = null;

    public EhcacheStore(CacheManager cacheManager){
        this.cacheManager = cacheManager;
    }
    public CacheManager getCacheManager()
    {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cache)
    {
        cacheManager = cache;
    }


    @Override
    public void save(OrhmID orhmH, Object bean)
    {
        Cache cache = cacheManager.getCache(orhmH.getAttachToCache());

        if(cache == null)
        {
            cacheManager.addCache(orhmH.getAttachToCache());
            cache = cacheManager.getCache(orhmH.getAttachToCache());
        }
        Element aCacheElement = new Element(orhmH.getId(),bean);
        cache.put(aCacheElement);

    }

    @Override
    public void delete(OrhmID id) throws PersistenceException {
        Cache cache = cacheManager.getCache(id.getAttachToCache());
        if(cache == null)
        {
            throw new PersistenceException("The cache doesn't not exist");
        }else {
            cache.remove(id.getId()) ;
        }
    }


    @Override
    public Object get(OrhmID orhmH)
    {
        Object bean=null;
        Cache cache = cacheManager.getCache(orhmH.getAttachToCache());
        bean = cache.get(orhmH.getId()).getValue();
        return bean;
    }


    @Override
    public Object lock(OrhmID orhmH) {
        Cache cache = cacheManager.getCache(orhmH.getAttachToCache());
        cache.acquireWriteLockOnKey(orhmH.getId());

        if(cache.isWriteLockedByCurrentThread(orhmH.getId()))
        {
            return  get(orhmH);
        } else
        {

            return null;
        }

    }

    @Override
    public void unlock(OrhmID orhmH)
    {
        Cache cache = cacheManager.getCache(orhmH.getAttachToCache());
        cache.releaseWriteLockOnKey(orhmH.getId());
    }

    @Override
    public Map<Object,Object> getAll(OrhmID id) throws PersistenceException {
        logger.debug("getAll "+id.getAttachToCache());
        Cache cache = cacheManager.getCache(id.getAttachToCache());

        HashMap<Object,Object> result = new HashMap<Object, Object>();

        for( Object key : cache.getKeys())
        {
            result.put(key,cache.get(key));
        }
        return result;
    }

}
