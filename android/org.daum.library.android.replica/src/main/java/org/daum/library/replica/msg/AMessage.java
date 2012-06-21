package org.daum.library.replica.msg;

import org.daum.library.replica.cluster.Node;
import org.daum.library.replica.cache.StoreCommand;

import java.io.Serializable;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 25/05/12
 * Time: 11:58
 */
public abstract class AMessage implements Message,Serializable{

    public StoreCommand op;
    public  Node source;
    public  Node dest;

    public StoreCommand getOp() {
        return op;
    }

    public void setOp(StoreCommand op) {
        this.op = op;
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
}
