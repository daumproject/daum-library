package org.daum.library.ormHM.persistence.connection;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import org.daum.library.ormHM.persistence.OrhmID;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 14:03
 */
public class EhcacheHandler implements StandardHandler {

    private CacheConfiguration cacheConfiguration=null;
    // todo use global cache configuration

    private CacheManager cacheManager = null;
    public EhcacheHandler(CacheManager cacheManager){
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
    public void unlock(OrhmID orhmH) {
        Cache cache = cacheManager.getCache(orhmH.getAttachToCache());
        cache.releaseWriteLockOnKey(orhmH.getId());
    }
}
