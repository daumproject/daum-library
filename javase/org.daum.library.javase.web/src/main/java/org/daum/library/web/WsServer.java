package org.daum.library.web;

import org.daum.library.fakeDemo.pojos.TemperatureMonitor;
import org.daum.library.replica.cache.StoreCommand;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/06/12
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
@ComponentType

@Provides({
        @ProvidedPort(name = "notify", type = PortType.MESSAGE)
})
public class WsServer extends AbstractComponentType {

    private WebServer webServer = null;
    private WebSocketChannel webSocketChannel = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Start
    public void startServer()
    {

        webSocketChannel = new WebSocketChannel();
        webServer = WebServers.createWebServer(8082)
                .add("/jed", webSocketChannel)
                .add(new StaticFileHandler("/web"));
        webServer.start();

        logger.debug("Server running at " + webServer.getUri());

        ChangeListener.getInstance().addEventListener(TemperatureMonitor.class,new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent propertyChangeEvent)
            {
                if(propertyChangeEvent.getCmd().equals(StoreCommand.ADD))
                {
                    webSocketChannel.broadcast("update");
                }

            }
        });
    }

    @Stop
    public void stopServer() {
        webServer.stop();
        webServer = null;
        webSocketChannel = null;
    }



    @Port(name = "notify")
    public void notifybyReplica(Object m)
    {
        ChangeListener.getInstance().receive(m);
    }
}