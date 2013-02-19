package org.daum.library.javase.copterManager.ws;


import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.util.HashMap;


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
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private HashMap<String,BaseWebSocketHandler> wspages = new HashMap<String, BaseWebSocketHandler>();
    private int port;

    @Start
    public void start(){
        port= Integer.parseInt(getDictionary().get("port").toString());
    }

    @Update
    public void update()
    {
        if(Integer.parseInt(getDictionary().get("port").toString()) != port){
            webServer.stop();
            webServer = null;
            port= Integer.parseInt(getDictionary().get("port").toString());
            webServer = WebServers.createWebServer(port);
        }
    }


    @Stop
    public void stopServer() {
        logger.debug("Stoping");
        webServer.stop();
        webServer = null;
        webSocketChannel = null;
    }


    public void startWebSock(){
        webServer = WebServers.createWebServer(port);
        for(String key : wspages.keySet())
        {
            webServer.add(key,wspages.get(key));
        }
        webServer.start();
    }

    public void stopWebSock(){
        if(webServer != null)
        {
            webServer.stop();
            try
            {
                // todo improve  hack ws
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // todo check is stopped
        webServer = null;
    }

    @Port(name = "service", method = "addHandler")
    @Override
    public void addHandler(String name,BaseWebSocketHandler webSocketChannel){

        logger.warn("Adding WS " + name);
        if(!wspages.containsKey(name))
        {
            wspages.put(name,webSocketChannel);
            stopWebSock();
            startWebSock();

        }else
        {
            logger.warn("Already added "+name);
        }

    }



    @Port(name = "service", method = "removeHandler")
    @Override
    public void removeHandler(String name)
    {
        wspages.remove(name);
        logger.warn("Removing WS " + name);
        stopWebSock();
        startWebSock();
    }


}