package org.daum.library.ormH.store;

import org.daum.library.ormH.api.PersistenceSessionStore;
import org.daum.library.ormH.persistence.Orhm;
import org.daum.library.ormH.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 15/06/12
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */
public class LocalStore implements PersistenceSessionStore {
    private static Logger logger =  LoggerFactory.getLogger(LocalStore.class);
    private static HashMap<String, LocalCache> store = new HashMap<String,LocalCache>();

    @Override
    public void save(Orhm id, Object bean) throws PersistenceException {
        String name =id.getCacheName();
        if(!store.containsKey(id.getCacheName()))
        {
            LocalCache cache  =  new LocalCache(name) ;
            store.put(name,cache);
        }
        store.get(name).put(id.getId(), bean);
    }

    @Override
    public void update(Orhm id, Object bean) throws PersistenceException {
        String name =id.getCacheName();
        store.get(name).put(id.getId(), bean);
    }

    @Override
    public void delete(Orhm id) throws PersistenceException
    {
        String name =id.getCacheName();
        if(store.get(name) != null){
            store.get(name).remove(id.getId());
        }  else {
            logger.error("the cache not exist");
        }

    }

    @Override
    public Object get(Orhm id) throws PersistenceException {
        String name =id.getCacheName();
        return  store.get(name).get(id.getId());
    }

    @Override
    public Map<Object, Object> getAll(Orhm id) throws PersistenceException {

        HashMap<Object,Object> result = new HashMap<Object, Object>();
        String name =id.getCacheName();
        LocalCache cache =  store.get(name);
        if(cache != null)
        {
            for( Object key : cache.keySet())
            {
                result.put(key,cache.get(key));
            }
        }else {
            logger.error("the cache not exist");
        }
        return result;
    }

    @Override
    public Object lock(Orhm id) throws PersistenceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unlock(Orhm id) throws PersistenceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
