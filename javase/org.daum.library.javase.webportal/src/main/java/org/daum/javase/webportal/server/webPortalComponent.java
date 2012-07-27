package org.daum.javase.webportal.server;

import org.daum.common.genmodel.SitacFactory;
import org.daum.common.genmodel.impl.AgentImpl;
import org.daum.javase.webportal.client.AdministrationForm;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.daum.library.replica.listener.SyncListener;
import org.daum.library.replica.msg.SyncEvent;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.library.javase.webserver.*;
import org.kevoree.library.javase.webserver.servlet.LocalServletRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.immutable.List;

import javax.servlet.ServletContextListener;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 5/31/12
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
@Provides({
        @ProvidedPort(name = "notify", type = PortType.MESSAGE)
})
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true)
})
@ComponentType
public class webPortalComponent extends ParentAbstractPage {

    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;
    private LocalServletRegistry servletRepository = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static ChangeListener singleton=null;

    public static ChangeListener getChangeListenerInstance(){
        if(singleton == null){
            singleton = new ChangeListener();
        }
        return singleton;
    }

    public void startPage() {

        getModelService().registerModelListener(new ModelListener() {
            @Override
            public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public void modelUpdated() {
                initormh();
                servletRepository.registerServlet("/ihmwebportal/authentifService", new AuthentificationServiceImpl(factory));
            }
        });

        servletRepository = new LocalServletRegistry() {
            @Override
            public String getCDefaultPath () {
                return "/ihmwebportal";
            }
        };
        /*
        getChangeListenerInstance().addSyncListener(new SyncListener() {
            @Override
            public void sync(SyncEvent syncEvent) {

            }
        });

        getChangeListenerInstance().addEventListener(AgentImpl.class,new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent propertyChangeEvent) {
                switch (propertyChangeEvent.getEvent()){
                    case UPDATE:
                        break;

                    case ADD:
                        break;

                    case DELETE:
                        break;
                }

            }
        });*/
        super.startPage();




    }

    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest request, KevoreeHttpResponse response) {
        String pattern = this.getDictionary().get("urlpattern").toString();
        pattern = pattern.replace("**", "");
        if (request.getUrl().equals(pattern)) {
            if (FileServiceHelper.checkStaticFile("IHMwebPortal.html", this, request, response)) {
                logger.debug("The IHMcodeMirror.html has been returned to respond to the request: {}", request.getUrl());
                return response;
            }
        }
        ClassLoader l = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(webPortalComponent.class.getClassLoader());

        boolean res = servletRepository.tryURL(request.getUrl(), request, response);
        Thread.currentThread().setContextClassLoader(l);
        if (res) {
            logger.debug("one servlet is able to respond to the request: {}", request.getUrl());
            return response;

        }


        if (FileServiceHelper.checkStaticFile(request.getUrl(), this, request, response)) {
            logger.debug("A static resource has been found to respond to the request: {}", request.getUrl());
            return response;
        }

        logger.warn("Unable to find the needed resource to the request: {}", request.getUrl());
        response.setContent("Bad request");
        return response;
    }

    @Port(name="notify")
    public void notifiedByReplica(final Object m) {
        try {
            getChangeListenerInstance().receive(m);
        } catch (Exception e) {
            logger.error("notify ",e);
        }
    }

    public void initormh()
    {
        try
        {
            configuration = new PersistenceConfiguration(getNodeName());
            replicaService =   this.getPortByName("service", ReplicaService.class);
            ReplicaStore store = new ReplicaStore(replicaService);
            configuration.setStore(store);
            for (Class c : SitacFactory.classes()) configuration.addPersistentClass(c);

            factory = configuration.getPersistenceSessionFactory();
        } catch (PersistenceException e)
        {
            e.printStackTrace();
        }
    }
}
