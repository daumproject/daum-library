package org.daum.javase.webportal.server;

import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.library.javase.webserver.*;
import org.kevoree.library.javase.webserver.servlet.LocalServletRegistry;
import org.sitac.SitacFactory;
import scala.collection.immutable.List;

import javax.servlet.ServletContextListener;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 5/31/12
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true)
})
@ComponentType
public class webPortalComponent extends ParentAbstractPage {

    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;
    private LocalServletRegistry servletRepository = null;


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
            }
        });

        servletRepository = new LocalServletRegistry(){
            @Override
            public String getCDefaultPath(){
                return "/ihmwebportal";
            }

            @Override
            public List<ServletContextListener> listeners() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void listeners_$eq(List<ServletContextListener> listeners) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        super.startPage();
        servletRepository.registerServlet("/ihmwebportal/authentifService", new AuthentificationServiceImpl());
    }

    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest request, KevoreeHttpResponse response) {
        ClassLoader l = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(webPortalComponent.class.getClassLoader() );
        System.err.println("URL " + request.getUrl() + "\n" + request + "\n" );

        boolean res = servletRepository.tryURL(request.getUrl(),request,response);

        Thread.currentThread().setContextClassLoader(l);
        if ( res ){
            return response;
        }


        if (FileServiceHelper.checkStaticFile(request.getUrl(), this, request, response)) {
            return response;
        }
        if (FileServiceHelper.checkStaticFile("IHMwebPortal.html", this, request, response)) {

            return response;
        }
        response.setContent("Bad request1");
        return response;
    }

    public void initormh() {
        try
        {
            ReplicaService replicatingService = getPortByName("service", ReplicaService.class);
            configuration = new PersistenceConfiguration(getNodeName());
            replicaService =   this.getPortByName("service", ReplicaService.class);
            ReplicaStore store = new ReplicaStore(replicaService);
            configuration.setStore(store);
            for (Class c : SitacFactory.classes()) configuration.addPersistentClass(c);

            factory = configuration.getPersistenceSessionFactory();


        } catch (PersistenceException e)
        {

        }
    }
}
