package org.daum.library.replica.cache;

import org.daum.library.replica.cluster.Node;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 07/06/12
 * Time: 10:38
 */
public class VersionedValue implements java.io.Serializable, java.lang.Cloneable {

    public Object value;
    public Node origin;
    public VectorClock clocks = new VectorClock();


    public VersionedValue(Object value){
        this.value = value;
    }
    public VersionedValue(Node node, Object value)
    {
        this.value = value;
        this.origin = node;
        clocks.incrementClock(node.getNodeID());
    }

    public void change(Node node, Object value){
        this.value = value;
        clocks.incrementClock(node.getNodeID());
    }

    public VersionedValue clone(){
        VersionedValue t = new VersionedValue(value);
        t.origin = origin.clone();
        t.clocks = this.clocks.clone();
        return t;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString(){

        return clocks.toString();
    }

    public VectorComparison compare(VersionedValue t){
        return VectorClock.compare(clocks,t.clocks);
    }
}
