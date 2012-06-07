package org.daum.library.replicatingMap;

import org.daum.library.replicatingMap.msg.Message;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 07/06/12
 * Time: 14:27
 */
public interface ICacheManger {
    public  Cache getCache(String name);
    public void processingMSG(Message e);
}
