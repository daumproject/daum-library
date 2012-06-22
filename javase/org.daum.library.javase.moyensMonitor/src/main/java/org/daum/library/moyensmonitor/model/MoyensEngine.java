package org.daum.library.moyensmonitor.model;

import org.daum.common.model.api.Demand;
import org.daum.library.moyensmonitor.listener.OnEngineStateChangeListener;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.LocalStore;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * save/update/delete Demand from/to the Replica/Persistence system
 */
public class MoyensEngine {
	
	private static final String TAG = "SITACEngine";

    private PersistenceSessionFactoryImpl factory;
	private OnEngineStateChangeListener listener;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public MoyensEngine(String nodeName, ReplicaStore storeImpl, OnEngineStateChangeListener engineHandler) {
        try {
            PersistenceConfiguration configuration = new PersistenceConfiguration(nodeName);
            configuration.setStore(storeImpl);
            configuration.addPersistentClass(Demand.class);

            this.factory = configuration.getPersistenceSessionFactory();

        } catch (PersistenceException e)
        {
            e.printStackTrace();
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
            logger.error(TAG, "Error while persisting object", ex);
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
        	logger.error(TAG, "Error while persisting object", ex);
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
        	logger.error(TAG, "Error while persisting object", ex);
        } finally {
            if (session != null) session.close();
        }
	}
}
