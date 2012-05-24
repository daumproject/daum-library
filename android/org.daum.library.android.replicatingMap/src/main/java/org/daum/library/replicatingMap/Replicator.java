package org.daum.library.replicatingMap;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 22/05/12
 * Time: 17:51
 */

import java.io.Serializable;
public class Replicator implements Serializable {
    public Operation op;
    public Object key;
    public Object value;
    public String cache;
    public  String source;
    public  String dest;
}