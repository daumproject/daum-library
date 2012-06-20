package org.daum.library.replica.cluster;

import org.daum.library.replica.cache.Cache;
import org.daum.library.replica.msg.Message;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 07/06/12
 * Time: 14:27
 */
public interface ICacheManger {
    public Cache getCache(String name);
    public void processingMSG(Message e);
}
