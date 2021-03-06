package org.daum.library.replica.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 07/06/12
 * Time: 14:04
 */
public class Node implements INode,Serializable {

    private static final long serialVersionUID = 352981891516L;
    private String nodeID= "";
    private boolean isSynchronized = false;
    private volatile long lastTickTime;
    private  NodeType nodeType=null;

    public Node(String node){
        nodeID = node;
        isSynchronized = false;
    }
    public String getNodeID() {
        return nodeID;
    }

    public void setSynchronized() {
        this.isSynchronized = true;
    }

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public Boolean equals(Node node)
    {
        if(this.getNodeID().equals(node.getNodeID()))
        {
            return  true;
        }
        else
        {
            return false;
        }
    }


    public long getLastTickTime() {
        return lastTickTime;
    }

    public void setLastTickTime(long lastTickTime) {
        this.lastTickTime = lastTickTime;
    }

    @Override
    public String toString(){
        return "{"+nodeID+" isSynchronized="+isSynchronized()+"}";
    }

    public Node clone(){
        Node t = new Node(getNodeID());
        t.setSynchronized(this.isSynchronized());
        t.setLastTickTime(this.getLastTickTime());
        t.setNodeType(this.getNodeType());

        return t;
    }
}
