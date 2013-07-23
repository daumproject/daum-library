package org.daum.library.web.utils;

import org.daum.common.genmodel.SitacFactory;
import org.daum.common.model.api.Demand;
import org.daum.library.ormH.api.IPersistenceSessionFactory;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.common.genmodel.*;
import org.kevoree.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 06/07/12
 * Time: 11:53
 * To change this template use File | Settings | File Templates.
 */
public class PersistenceHelper {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;
    private PersistenceSession s=null;
    private String nodename;


    public PersistenceHelper(String nodename,ReplicaService service){
        this.nodename = nodename;
        this.replicaService= service;
        initializePersistence();
    }


    public void initializePersistence()
    {
        if(replicaService == null )
        {
            try
            {
                configuration = new PersistenceConfiguration(nodename);

                ReplicaStore store = new ReplicaStore(replicaService);
                configuration.setStore(store);
                configuration.addPersistentClass(Demand.class);
                for (Class c : SitacFactory.classes()) configuration.addPersistentClass(c);

                factory = configuration.getPersistenceSessionFactory();


            } catch (PersistenceException e)
            {
                Log.error("init : ", e);
            }
        }
    }



    public IPersistenceSessionFactory getFactory(){
        initializePersistence();
        return factory;
    }


}
