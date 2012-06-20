package org.daum.library.replica;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 20/06/12
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class PropertyChangeEvent {
    private Object key;
    private StoreCommand cmd;

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public StoreCommand getCmd() {
        return cmd;
    }

    public void setCmd(StoreCommand cmd) {
        this.cmd = cmd;
    }
}
