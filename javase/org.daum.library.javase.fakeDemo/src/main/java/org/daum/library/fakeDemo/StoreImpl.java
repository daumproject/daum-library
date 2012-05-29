package org.daum.library.fakeDemo;

import org.daum.library.ormHM.api.PersistenceSessionStore;
import org.daum.library.ormHM.persistence.OrhmID;
import org.daum.library.ormHM.utils.PersistenceException;
import org.daum.library.replicatingMap.Cache;
import org.daum.library.replicatingMap.ReplicatingService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 29/05/12
 * Time: 18:57
 */
public class StoreImpl implements PersistenceSessionStore {

    private ReplicatingService replicatingService=null;
    public StoreImpl(ReplicatingService cache){
        this.replicatingService = cache;
    }


    @Override
    public void save(OrhmID id, Object bean) throws PersistenceException {
        Cache cache = replicatingService.getCache(id.getAttachToCache());
        cache.setMaxEntriesLocalHeap(id.getMaxEntriesLocalHeap());
        cache.put(id.getId(),bean);
    }

    @Override
    public void delete(OrhmID id) throws PersistenceException {
        Cache cache = replicatingService.getCache(id.getAttachToCache());
        cache.setMaxEntriesLocalHeap(id.getMaxEntriesLocalHeap());
        cache.remove(id.getId());
    }

    @Override
    public Object get(OrhmID id) throws PersistenceException {
        Cache cache = replicatingService.getCache(id.getAttachToCache());
        cache.setMaxEntriesLocalHeap(id.getMaxEntriesLocalHeap());
        return cache.get(id.getId());
    }

    @Override
    public Map<Object, Object> getAll(OrhmID id) throws PersistenceException {
        Cache cache = replicatingService.getCache(id.getAttachToCache());
        HashMap<Object,Object> result = new HashMap<Object, Object>();
        for( Object key : cache.keySet())
        {
            result.put(key,cache.get(key));
        }
        return result;
    }

    @Override
    public Object lock(OrhmID id) throws PersistenceException {
        return null;
    }

    @Override
    public void unlock(OrhmID id) throws PersistenceException {

    }

}