package org.daum.library.replica.disk;

import java.util.Map;
import java.util.NavigableMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 31/07/12
 * Time: 11:15
 * To change this template use File | Settings | File Templates.
 */
public interface IDiskPersistence {

    public void commit();
    public void close();
    public void rollback();
    public NavigableMap<Comparable, Object> createCache(String cachename);
}
