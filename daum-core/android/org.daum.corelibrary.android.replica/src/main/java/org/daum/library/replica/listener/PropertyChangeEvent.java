package org.daum.library.replica.listener;

import org.daum.library.replica.cluster.Node;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 20/06/12
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class PropertyChangeEvent {

    private Event event;
    private Object id;
    private Node node;

    public Object getId() {
        return id;
    }
    public void setId(Object id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
