package org.daum.javase.webportal.server;

import org.daum.javase.webportal.client.AuthentificationService;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.framework.AbstractComponentType;
import org.sitac.SitacFactory;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/13/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true)
})
@ComponentType
public class AuthentificationServiceImpl extends AbstractComponentType implements AuthentificationService {
    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;

    @Override
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

    @Start
    public void start()
    {
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
    }

    @Stop
    public void stop(){

    }


    @Update
    public void update(){

    }

    @Override
    public boolean authenticateAgent(String name, String password) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
        } catch (PersistenceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if(session != null){
            //get user

        }
        return false;
    }


}
