package org.kevoree.common.replica.api;

import org.daum.library.ormH.api.PersistenceSessionStore;
import org.daum.library.ormH.persistence.Orhm;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.Cache;
import org.daum.library.replica.cache.ReplicaService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/06/12
 * Time: 10:25
 * To change this template use File | Settings | File Templates.
 */
public class ReplicaStore implements PersistenceSessionStore {

    private ReplicaService replicatingService=null;

    public ReplicaStore(ReplicaService cache){
        this.replicatingService = cache;
    }

    @Override
    public void save(Orhm id, Object bean) throws PersistenceException {
        Cache cache = replicatingService.getCache(id.getCacheName());
        cache.put(id.getId(),bean);
    }

    @Override
    public void update(Orhm orhm, Object bean) throws PersistenceException {
        Cache cache = replicatingService.getCache(orhm.getCacheName());
        cache.put(orhm.getId(),bean);
    }

    @Override
    public void delete(Orhm id) throws PersistenceException {
        Cache cache = replicatingService.getCache(id.getCacheName());
        cache.remove(id.getId());
    }

    @Override
    public Object get(Orhm id) throws PersistenceException {
        Cache cache = replicatingService.getCache(id.getCacheName());
        return cache.get(id.getId()).getValue();
    }

    @Override
    public Map<Object, Object> getAll(Orhm id) throws PersistenceException
    {
        Cache cache = replicatingService.getCache(id.getCacheName());
        if(cache != null){
            HashMap<Object,Object> result = new HashMap<Object, Object>();
            for( Object key : cache.keySet())
            {
                result.put(key,cache.get(key).getValue());
            }       return result;
        }
        return  null;
    }

    @Override
    public Object lock(Orhm id) throws PersistenceException {

        return null;
    }

    @Override
    public void unlock(Orhm id) throws PersistenceException {

    }

}