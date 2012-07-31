package org.daum.library.replica.cluster;

import org.apache.jdbm.DB;
import org.daum.library.replica.cache.Cache;
import org.daum.library.replica.msg.Message;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 07/06/12
 * Time: 14:27
 */
public interface ICacheManger {
    public ICluster getCluster();
    public Cache getCache(String name);
    public void processingMSG(Message e);
}
