package org.daum.library.replicav2.cache;

import org.daum.library.replicav2.Node;
import org.daum.library.replicav2.VectorClock;
import org.daum.library.replicav2.VectorComparison;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 27/03/13
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class Element implements Serializable {
    public Object value;
    public Node origin;
    public VectorClock clocks = new VectorClock();


    public Element(Object value){
        this.value = value;
    }
    public Element(Node node, Object value)
    {
        this.value = value;
        this.origin = node;
        clocks.incrementClock(node.name);
    }



    public void change(Node node, Object value){
          this.value = value;
        clocks.incrementClock(node.name);
    }

    public Element clone(){
        Element t = new Element(value);
        t.origin = origin.clone();
        t.clocks = this.clocks.clone();

        return t;

    }

    public String toString(){

        return clocks.toString();
    }

    public VectorComparison compare(Element t){
        return VectorClock.compare(clocks,t.clocks);


    }




}
