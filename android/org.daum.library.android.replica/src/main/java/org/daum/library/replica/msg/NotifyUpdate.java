package org.daum.library.replica.msg;

import org.daum.library.replica.cache.StoreEvent;
import org.daum.library.replica.cluster.Node;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 19/06/12
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
public class NotifyUpdate implements Message {
    private static final long serialVersionUID = 1518L;
    private String cache;
    private Object id;
    private StoreEvent event;
    private Node node;

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }


    public StoreEvent getEvent(){
        return event;
    }

    public void setEvent(StoreEvent event){
        this.event = event;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
