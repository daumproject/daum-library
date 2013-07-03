package org.daum.library.javase.copterManager.test;

import org.webbitserver.WebSocketConnection;

import java.util.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 06/07/12
 * Time: 16:27
 * To change this template use File | Settings | File Templates.
 */
public class NotifyConnection extends Observable {

    public void notifyConnection(WebSocketConnection connection)
    {
        setChanged();
        notifyObservers(connection);
    }
}
