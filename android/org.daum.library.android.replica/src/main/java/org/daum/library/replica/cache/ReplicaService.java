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
    public Cache getCache(String name);
}
