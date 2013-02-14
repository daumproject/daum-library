package org.daum.library.javase.copterManager.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/06/12
 * Time: 13:15
 * To change this template use File | Settings | File Templates.
 */
public class WebSocketChannel extends BaseWebSocketHandler implements IWebSocketChannel {
    private Set<WebSocketConnection> connections = new HashSet<WebSocketConnection>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private NotifyConnection notifyConnection = new NotifyConnection();
    @Override
    public void onOpen(WebSocketConnection connection)
    {
        logger.debug("WebSocketConnection "+connection.toString());
        connections.add(connection);
        notifyConnection.notifyConnection(connection);
    }


    public NotifyConnection getNotifyConnection() {
        return notifyConnection;
    }

    public void broadcast(String msg)
    {

        for (WebSocketConnection connection : connections)
        {
            connection.send(msg);
        }

    }

    @Override
    public void onClose(WebSocketConnection connection)  {
        connections.remove(connection);
    }

    public void onMessage(WebSocketConnection connection, String message) {
        logger.debug("onMessage "+message);
        connection.send(message.toUpperCase()); // echo back message in upper case
    }


    public Set<WebSocketConnection> getConnections() {
        return connections;
    }

    public void setConnections(Set<WebSocketConnection> connections) {
        this.connections = connections;
    }
}