package org.daum.library.replica.msg;

import org.daum.library.replica.StoreCommand;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 19/06/12
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
public class NotifyUpdate implements Message {

    private String cache;
    private Object key;
    private StoreCommand cmd;

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }


    public StoreCommand getCmd(){
        return cmd;
    }

    public void setCmd(StoreCommand cmd){
        this.cmd = cmd;
    }

}
