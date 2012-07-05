package org.daum.library.web;

import org.daum.common.model.api.Demand;
import org.daum.common.model.api.VehicleType;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.replica.listener.Event;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.kevoree.annotation.*;
import org.kevoree.extra.marshalling.JacksonSerializer;
import org.kevoree.extra.marshalling.RichJSONObject;
import org.kevoree.framework.AbstractComponentType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/06/12
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "JavaSE")
@ComponentType
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = false) })
@Provides({
        @ProvidedPort(name = "notify", type = PortType.MESSAGE)
})
public class WsServer extends AbstractComponentType {

    private WebServer webServer = null;
    private WebSocketChannel webSocketChannel = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private  ChangeListener changeListener = new ChangeListener();

    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;
    private PersistenceSession s=null;


    @Start
    public void startServer()
    {
        webSocketChannel = new WebSocketChannel();
        webServer = WebServers.createWebServer(8082)
                .add("/jed", webSocketChannel);
        webServer.start();
        logger.debug("Starting WsServer");
        listeners();
    }


    public  void listeners()
    {
        changeListener.addEventListener(Demand.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent propertyChangeEvent) {

                try
                {
                    init();

                    PersistenceSession session = null;
                    try
                    {
                        session = factory.getSession();
                        if(session != null)
                        {
                            Demand updateD = (Demand) session.get(Demand.class,propertyChangeEvent.getId());

                            switch (propertyChangeEvent.getEvent())
                            {
                                case  ADD:

                                    if(updateD != null)
                                    {
                                        RichJSONObject c = new RichJSONObject(updateD);
                                        webSocketChannel.broadcast(Event.ADD+"$"+c.toJSON());
                                    }

                                    break;


                                case UPDATE:
                                    if(updateD != null)
                                    {
                                        RichJSONObject c = new RichJSONObject(updateD);
                                        webSocketChannel.broadcast(Event.UPDATE+"$"+c.toJSON());
                                    }
                                    break;

                                case DELETE:

                                        RichJSONObject c = new RichJSONObject(updateD);
                                        webSocketChannel.broadcast(Event.DELETE+"$"+propertyChangeEvent.getId());

                                    break;



                            }




                        }
                    } catch (PersistenceException ex)
                    {
                        logger.error("",ex);
                        replicaService = null;
                    }
                    finally
                    {
                        if (session != null) session.close();

                    }



                } catch (Exception e) {
                    logger.error("", e);
                }

            }
        });
    }


    @Stop
    public void stopServer() {
        logger.debug("Stoping");
        webServer.stop();
        webServer = null;
        webSocketChannel = null;
    }



    @Port(name = "notify")
    public void notifybyReplica(Object m)
    {
        try {
            changeListener.receive(m);
        } catch (Exception e){
            logger.error("notify " + e);
        }
    }


    public void init()
    {
        if(replicaService == null )
        {
            try
            {
                configuration = new PersistenceConfiguration(getNodeName());
                replicaService =   this.getPortByName("service", ReplicaService.class);
                ReplicaStore store = new ReplicaStore(replicaService);
                configuration.setStore(store);
                configuration.addPersistentClass(Demand.class);
                factory = configuration.getPersistenceSessionFactory();


            } catch (PersistenceException e)
            {
                logger.error("init : ",e);
            }
        }
    }


}