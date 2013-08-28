package org.daum.library.javase.copterManager.ws;


import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.log.Log;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/06/12
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "JavaSE")
@ComponentType
@DictionaryType({
        @DictionaryAttribute(name = "port", defaultValue = "8092")
})
@Provides({
        @ProvidedPort(name = "service", type = PortType.SERVICE, className = WsHandler.class)
})
public class WsServer extends AbstractComponentType implements  WsHandler {

    private WebServer webServer = null;
    private WebSocketChannel webSocketChannel = null;

    private HashMap<String,BaseWebSocketHandler> wspages = new HashMap<String, BaseWebSocketHandler>();
    private int port;

    @Start
    public void start() {
        Log.debug("Starting {}", getName());
        startWebSock();
    }

    @Update
    public void update() {
        if (Integer.parseInt(getDictionary().get("port").toString()) != port) {
            Log.debug("updating {}", getName());
            stopWebSock();
            startWebSock();
        }
    }


    @Stop
    public void stopServer() {
        Log.debug("Stopping {}", getName());
        stopWebSock();
    }


    public void startWebSock() {
        port = Integer.parseInt(getDictionary().get("port").toString());
        webServer = WebServers.createWebServer(port);
        for (String key : wspages.keySet()) {
            webServer.add(key, wspages.get(key));
        }
        webServer.start();
    }

    public void stopWebSock() {
        if (webServer != null) {
            try {
                webServer.stop().get();
            } catch (InterruptedException e) {
                Log.error("WsServer.stopWebSock", e);
            } catch (ExecutionException e) {
                Log.error("WsServer.stopWebSock", e);
            }
        }
        webServer = null;
    }

    @Port(name = "service", method = "addHandler")
    @Override
    public void addHandler(String name,BaseWebSocketHandler webSocketChannel){

        Log.warn("Adding WS " + name);
        if(!wspages.containsKey(name))
        {
            wspages.put(name,webSocketChannel);
            stopWebSock();
            startWebSock();

        }else
        {
            Log.warn("Already added "+name);
        }

    }



    @Port(name = "service", method = "removeHandler")
    @Override
    public void removeHandler(String name)
    {
        wspages.remove(name);
        Log.warn("Removing WS " + name);
        stopWebSock();
        startWebSock();
    }


}