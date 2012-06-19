package org.daum.library.replica;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 24/05/12
 * Time: 10:44
 */
public class DHashMap<K, V> extends ConcurrentHashMap<K, V> implements Serializable {
    private  long updated;
}
