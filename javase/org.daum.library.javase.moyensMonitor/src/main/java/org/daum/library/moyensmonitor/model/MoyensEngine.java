package org.daum.library.moyensmonitor.model;

import org.daum.common.model.api.Demand;
import org.daum.library.moyensmonitor.listener.OnEngineStateChangeListener;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.LocalStore;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * save/update/delete Demand from/to the Replica/Persistence system
 */
public class MoyensEngine {
	
	private static final String TAG = "SITACEngine";

    private PersistenceSessionFactoryImpl factory;
	private OnEngineStateChangeListener listener;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public MoyensEngine(String nodeName, ReplicaStore storeImpl, OnEngineStateChangeListener engineHandler) {
        PersistenceSession session = null;

        try {
            // configuring persistence
            PersistenceConfiguration configuration = new PersistenceConfiguration(nodeName);
            configuration.setStore(storeImpl);
            configuration.addPersistentClass(Demand.class);

            // retrieve the persistence factory
            this.factory = configuration.getPersistenceSessionFactory();

            // check if there is already some data in the replica
            session = factory.getSession();
            Map<Object, Object> demands = session.getAll(Demand.class);
            for (Object d : demands.values()) {
                if (listener != null) listener.onAdd((Demand) d);
            }

            // add callback to handle remote events on replica


        } catch (PersistenceException ex) {
            logger.error(TAG, "Error while initializing persistence in engine", ex);

        } finally {
            if (session != null) session.close();
        }

		listener = engineHandler;
	}
	
	public void add(Demand d) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.save(d);
            if (listener != null) listener.onAdd(d);
            
        } catch (PersistenceException ex) {
            logger.error(TAG, "Error while adding object in Replica", ex);
        } finally {
            if (session != null) session.close();
        }
	}

	public void update(Demand d) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.update(d);
            if (listener != null) listener.onUpdate(d);

        } catch (PersistenceException ex) {
        	logger.error(TAG, "Error while updating object in Replica", ex);
        } finally {
            if (session != null) session.close();
        }
	}
	
	public void delete(Demand d) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.delete(d);
            if (listener != null) listener.onDelete(d);
            
        } catch (PersistenceException ex) {
        	logger.error(TAG, "Error while deleting object in Replica", ex);
        } finally {
            if (session != null) session.close();
        }
	}
}
