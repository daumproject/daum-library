package org.daum.library.replica.msg;

import org.daum.library.replica.cluster.Node;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/06/12
 * Time: 14:57
 * To change this template use File | Settings | File Templates.
 */
public class SyncEvent implements Message,Serializable
{
    private static final long serialVersionUID = 251516L;
    private Node node;
    /**
     * Get the node with which the synchronization was made
     * @return Node
     */
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }


    public String toString(){
        return "SyncEvent "+getNode();
    }
}
