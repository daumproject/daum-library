package org.daum.library.replica.msg;

import org.daum.library.replica.cache.StoreEvent;
import org.daum.library.replica.cluster.Node;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 25/05/12
 * Time: 11:58
 */
public abstract class AMessage implements Message,Serializable{

    private static final long serialVersionUID = 1516L;
    public  String uuid = UUID.randomUUID().toString();
    public StoreEvent event;
    public  Node source;
    public  Node dest;

    public StoreEvent getEvent() {
        return event;
    }

    public void setEvent(StoreEvent event) {
        this.event = event;
    }

    public Node getSourceNode() {
        return source;
    }

    public void setSourceNode(Node source) {
        this.source = source;
    }

    public Node getDestNode() {
        return dest;
    }

    public void setDestNode(Node dest) {
        this.dest = dest;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }
}
