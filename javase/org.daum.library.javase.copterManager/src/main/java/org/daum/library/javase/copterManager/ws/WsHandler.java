package org.daum.library.javase.copterManager.ws;

import org.webbitserver.BaseWebSocketHandler;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 06/07/12
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public interface WsHandler {

    public void addHandler(String name,BaseWebSocketHandler webSocketChannel);
    public void removeHandler(String name);
}
