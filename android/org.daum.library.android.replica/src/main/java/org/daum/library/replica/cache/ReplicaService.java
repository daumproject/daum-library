package org.daum.library.replica.cache;

import org.daum.library.replica.cache.Cache;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 22/05/12
 * Time: 18:30
 *
 */
public interface ReplicaService
{
    /**
     * get the cache manager
     * @param name the name of the cache
     * @return  Cache
     */
    public Cache getCache(String name);

    /**
     * Waiting that the replica is synchronized
     */
    public void waitingSync();

   // public void waitingSync(int timeout);

}
