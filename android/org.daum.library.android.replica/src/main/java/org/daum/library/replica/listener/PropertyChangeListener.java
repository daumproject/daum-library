package org.daum.library.replica.listener;

import org.daum.library.replica.listener.PropertyChangeEvent;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 20/06/12
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public interface PropertyChangeListener extends java.util.EventListener {

    void update(PropertyChangeEvent propertyChangeEvent);


}
