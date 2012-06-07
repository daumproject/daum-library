package org.daum.library.replicatingMap;

import java.io.Serializable;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 07/06/12
 * Time: 14:04
 */
public class Node implements INode,Serializable {

    private String nodeID= "";
    private boolean isSynchronized = false;
    private volatile long lastTickTime;

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


    public Boolean equals(Node node)
    {
        if(this.getNodeID().equals(node.getNodeID())){
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
        return "{"+nodeID+":"+isSynchronized()+"}";
    }
}
