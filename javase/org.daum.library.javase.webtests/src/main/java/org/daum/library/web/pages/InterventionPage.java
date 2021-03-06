package org.daum.library.web.pages;

import org.daum.common.genmodel.*;
import org.daum.common.genmodel.SitacFactory;
import org.daum.common.genmodel.impl.InterventionImpl;
import org.daum.common.model.api.Demand;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.*;
import org.daum.library.replica.msg.SyncEvent;
import org.daum.library.web.utils.WebCache;
import org.daum.library.web.ws.WebSocketChannel;
import org.daum.library.web.ws.WsHandler;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.extra.marshalling.RichJSONObject;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import org.kevoree.log.Log;
import org.webbitserver.WebSocketConnection;


import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 03/07/12
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */


@Requires({
        @RequiredPort(name = "replica", type = PortType.SERVICE, className = ReplicaService.class, optional = false),
        @RequiredPort(name = "ws", type = PortType.SERVICE, className = WsHandler.class, optional = false)
})
@Provides({
        @ProvidedPort(name = "notify", type = PortType.MESSAGE)
})
@ComponentType
public class InterventionPage extends AbstractPage implements Observer {

    private WebSocketChannel webSocketChannel = new WebSocketChannel();
    private ChangeListener changeListener = new ChangeListener();

    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;

    @Start
    public void  start()
    {
        super.startPage();

        getModelService().registerModelListener(new ModelListener() {
            @Override
            public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public boolean afterLocalUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }


            @Override
            public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public void modelUpdated()
            {
                Log.debug("Request Ws Demand");
                getPortByName("ws", WsHandler.class).addHandler("/intervention",webSocketChannel);
            }

            @Override
            public void preRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void postRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        webSocketChannel.getNotifyConnection().addObserver(this);


        changeListener.addEventListener(InterventionImpl.class,new PropertyChangeListener() {
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
                            Intervention updateD = (Intervention) session.get(InterventionImpl.class,propertyChangeEvent.getId());

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
                        Log.error("",ex);
                        replicaService = null;
                    }
                    finally
                    {
                        if (session != null) session.close();

                    }



                } catch (Exception e) {
                    Log.error("", e);
                }
            }
        });

        changeListener.addSyncListener(new SyncListener() {
            @Override
            public void sync(SyncEvent syncEvent) {

                for(WebSocketConnection connection :webSocketChannel.getConnections())
                {
                    loadAll((WebSocketConnection) connection);
                }
            }
        });

    }


    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse)
    {
        String page = new String(WebCache.load("pages/intervention.html"));
        kevoreeHttpResponse.setContent(WebCache.apply(getModelService().getLastModel(),getNodeName(),page));
        return kevoreeHttpResponse;
    }



    @Port(name = "notify")
    public void notifiedByReplica(Object m)
    {
        changeListener.receive(m);
    }

    @Override
    public void update(Observable observable, Object connection) {
        loadAll((WebSocketConnection) connection);

    }

    public void init()
    {
        if(replicaService == null )
        {
            try
            {
                configuration = new PersistenceConfiguration(getNodeName());
                replicaService =   this.getPortByName("replica", ReplicaService.class);
                ReplicaStore store = new ReplicaStore(replicaService);
                configuration.setStore(store);
                configuration.addPersistentClass(Demand.class);

                for (Class c : SitacFactory.classes()) configuration.addPersistentClass(c);

                factory = configuration.getPersistenceSessionFactory();


            } catch (PersistenceException e)
            {
                Log.error("init : ",e);
            }
        }
    }


    public void loadAll(WebSocketConnection connection){

        try
        {
            init();

            PersistenceSession session = null;
            try
            {
                session = factory.getSession();
                if(session != null)
                {

                    Map<Object, InterventionImpl> demands = session.getAll(InterventionImpl.class);


                    for(Object key : demands.keySet())
                    {

                        RichJSONObject c = new RichJSONObject(demands.get((String) key));
                        ((WebSocketConnection)connection).send(Event.ADD + "$" + c.toJSON());
                    }

                }
            } catch (PersistenceException ex)
            {
                Log.error("",ex);
                replicaService = null;
            }
            finally
            {
                if (session != null) session.close();

            }



        } catch (Exception e) {
            Log.error("", e);
        }
    }
}
