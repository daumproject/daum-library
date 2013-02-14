package org.daum.library.javase.copterManager.ws;

import org.webbitserver.WebSocketConnection;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 06/07/12
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public interface IWebSocketChannel {

    public void onOpen(WebSocketConnection connection);
    public void onClose(WebSocketConnection connection);

}
