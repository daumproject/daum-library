package org.daum.library.ormHM.persistence;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 11:30
 */

import org.daum.library.ormHM.api.IPersistenceConfiguration;
import org.daum.library.ormHM.api.PersistenceSessionStore;
import org.daum.library.ormHM.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PersistenceConfiguration implements IPersistenceConfiguration {

    private String nodeID;
    private  Logger logger = LoggerFactory.getLogger(this.getClass());
    private PersistenceSessionStore connectionConfiguration=null;
    private Map<String, PersistentClass> persistentClasses=null;


    /**
     * @param nodeID  the unique id of the node
     * @throws PersistenceException
     */
    public PersistenceConfiguration(String nodeID) throws PersistenceException
    {
       this.nodeID = nodeID;
        persistentClasses =  new HashMap<String, PersistentClass>();
        for(PersistentClass pc : getPersistentClasses())
        {
            pc.parse();
        }
    }


    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public void addPersistentClass(Class pc) throws PersistenceException
    {
        logger.debug("addPersistent "+pc.getName());
        PersistentClass tmp = new PersistentClass(pc.getName());
        persistentClasses.put(tmp.getName(), tmp);
        tmp.parse();
    }


    public void addPersistentClass(PersistentClass pc)
    {
        persistentClasses.put(pc.getName(), pc);
    }

    public PersistenceSessionStore getConnectionConfiguration()
    {
        return connectionConfiguration;
    }

    public void setStore(PersistenceSessionStore connectionConfiguration)
    {
        this.connectionConfiguration = connectionConfiguration;
    }


    public PersistenceSessionFactoryImpl getPersistenceSessionFactory(){
        return new PersistenceSessionFactoryImpl(this);
    }


    public PersistentClass getPersistentClass(Class clazz)
    {
        return getPersistentClass(clazz.getName());
    }

    public PersistentClass getPersistentClass(Object clazz)
    {
        return getPersistentClass(clazz.getClass());
    }

    public PersistentClass getPersistentClass(String name) {
        return persistentClasses.get(name);
    }

    public Collection<PersistentClass> getPersistentClasses()
    {
        return persistentClasses.values();
    }

}