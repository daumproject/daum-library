package org.daum.library.replica.utils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 31/07/12
 * Time: 14:18
 * To change this template use File | Settings | File Templates.
 */
public class LRUMap<T, U> implements ILRUMap {

    protected final int cacheSize;
    protected final Map<T, U> cache;
    protected final List<T> lruKeys;

    public LRUMap(int cacheSize)
    {
        this.cacheSize = cacheSize;
        this.cache = new HashMap<T, U>(cacheSize);
        this.lruKeys = new LinkedList<T>();
    }

    public boolean containsKey(T key)
    {
        return cache.containsKey(key);
    }

    public U get(T key)
    {
        if (lruKeys.get(0) != key) // Pointer comparison
        {
            if (!this.lruKeys.remove(key)) return null;
            lruKeys.add(0, key);
        }
        return cache.get(key);
    }

    public void clear()
    {
        cache.clear();
        lruKeys.clear();
    }

    public void put(T key, U value)
    {
        if (cacheIsFull())
            removeLeastRecentlyUsedFromCache();

        cache.put(key, value);
        lruKeys.add(key);
    }

    private boolean cacheIsFull()
    {
        return cache.size() == cacheSize;
    }

    protected void removeLeastRecentlyUsedFromCache()
    {
        T lru = lruKeys.remove(0);
        cache.remove(lru);
    }

    public void remove(T key)
    {
        lruKeys.remove(key);
        cache.remove(key);
    }

    public Set<T> keySet() {
        return cache.keySet();
    }


    public int size() {
        return cache.size();
    }
}