package org.daum.library.replicatingMap;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 18:18
 */
public interface Channel {
    public void write(Replicator e);
   // public Replicator read();
}
