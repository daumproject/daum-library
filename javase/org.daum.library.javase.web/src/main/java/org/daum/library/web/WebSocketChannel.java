package org.daum.library.web;

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
public class WebSocketChannel extends BaseWebSocketHandler {
    private Set<WebSocketConnection> connections = new HashSet<WebSocketConnection>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onOpen(WebSocketConnection connection) throws Exception {
        logger.debug("WebSocketConnection "+connection.toString());
        connections.add(connection);
    }


    public void broadcast(String msg) {
        for (WebSocketConnection connection : connections)
        {
            connection.send(msg);
        }
    }

    @Override
    public void onClose(WebSocketConnection connection) throws Exception {
        connections.remove(connection);
    }

    public void onMessage(WebSocketConnection connection, String message) {
        logger.debug("onMessage "+message);
        connection.send(message.toUpperCase()); // echo back message in upper case
    }


}