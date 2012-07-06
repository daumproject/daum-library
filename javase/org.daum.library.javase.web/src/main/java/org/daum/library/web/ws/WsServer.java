package org.daum.library.web.ws;

import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
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
    private  ChangeListener changeListener = new ChangeListener();
    private HashMap<String,BaseWebSocketHandler> wspages = new HashMap<String, BaseWebSocketHandler>();

    @Start
    public void start(){

    }
    @Update
    public void update(){
        // todo update port

    }


    @Stop
    public void stopServer() {
        logger.debug("Stoping");
        webServer.stop();
        webServer = null;
        webSocketChannel = null;
    }


    @Port(name = "service", method = "addHandler")
    @Override
    public void addHandler(String name,BaseWebSocketHandler webSocketChannel){

        logger.warn("Adding WS " + name);
        if(!wspages.containsKey(name))
        {
            wspages.put(name,webSocketChannel);

            if(webServer != null)
            {
                webServer.stop();
                try
                {
                    // todo improve
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
               // todo check is stopped
            webServer = null;

            // todo port

            webServer = WebServers.createWebServer(8082);

            for(String key : wspages.keySet())
            {
                webServer.add(key,wspages.get(key));
            }

            webServer.start();


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

        // todo remove restart

    }


}