package org.daum.javase.webportal.server;

import org.daum.javase.webportal.client.AuthentificationService;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/13/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthentificationServiceImpl implements AuthentificationService {
    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;

    @Override
    public void initHibernate() {
        try
        {
            configuration = new PersistenceConfiguration(getNodeName());
            replicaService =   this.getPortByName("service", ReplicaService.class);
            ReplicaStore store = new ReplicaStore(replicaService);
            configuration.setStore(store);
            configuration.addPersistentClass(Demand.class);

            for (Class c : SitacFactory.classes()) configuration.addPersistentClass(c);

            factory = configuration.getPersistenceSessionFactory();


        } catch (PersistenceException e)
        {

        }
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
            SitacFactory.
           //session.get()
        }
    }


}
