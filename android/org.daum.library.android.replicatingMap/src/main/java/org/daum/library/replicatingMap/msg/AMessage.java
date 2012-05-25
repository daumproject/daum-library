package org.daum.library.replicatingMap.msg;

import org.daum.library.replicatingMap.StoreRequest;

import java.io.Serializable;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 25/05/12
 * Time: 11:58
 */
public abstract class AMessage implements Message,Serializable{

    public StoreRequest op;
    public  String source;
    public  String dest;

    public StoreRequest getOp() {
        return op;
    }

    public void setOp(StoreRequest op) {
        this.op = op;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}
