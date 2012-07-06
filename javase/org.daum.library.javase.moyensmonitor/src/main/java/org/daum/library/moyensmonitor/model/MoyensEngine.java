package org.daum.library.moyensmonitor.model;

import org.daum.common.model.api.Demand;
import org.daum.library.moyensmonitor.listener.OnEngineStateChangeListener;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.daum.library.replica.listener.SyncListener;
import org.daum.library.replica.msg.SyncEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * save/update/delete Demand from/to the Replica/Persistence system
 */
public class MoyensEngine {
	
	private static final String TAG = "MoyensEngine";

    private PersistenceSessionFactoryImpl factory;
	private OnEngineStateChangeListener listener;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String nodeName;
	
	public MoyensEngine(final String nodeName, ReplicaStore storeImpl, OnEngineStateChangeListener engineHandler) {
        this.nodeName = nodeName;
        PersistenceSession session = null;
        listener = engineHandler;

        try {
            // configuring persistence
            PersistenceConfiguration configuration = new PersistenceConfiguration(nodeName);
            configuration.setStore(storeImpl);
            configuration.addPersistentClass(Demand.class);

            // retrieve the persistence factory
            this.factory = configuration.getPersistenceSessionFactory();

        } catch (PersistenceException ex) {
            logger.error(TAG, "Error while initializing persistence in engine", ex);

        } finally {
            if (session != null) session.close();
        }

        // add a callback to populate engine when sync is ready
        ChangeListener.getInstance("MoyensMonitorDAUM").addSyncListener(new SyncListener() {
            @Override
            public void sync(SyncEvent e) {
                if (listener != null) {
                    try {
                        PersistenceSession session = factory.getSession();
                        Map<Object, Demand> demands = session.getAll(Demand.class);
                        listener.doUpdate(demands.values());

                    } catch (PersistenceException ex) {
                        logger.error(TAG, "Error while retrieving data on sync", ex);
                    }
                }
            }
        });

        // add callback to handle remote events on replica
        ChangeListener.getInstance("MoyensMonitorDAUM").addEventListener(Demand.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent e) {
                if (listener != null) {
                    try {
                        PersistenceSession session = factory.getSession();
                        Demand d = (Demand) session.get(Demand.class, e.getId());
                        switch (e.getEvent()) {
                            case ADD:
                            case UPDATE:
                            case DELETE:
                                Map<Object, Demand> map = session.getAll(Demand.class);
                                listener.doUpdate(map.values());
                        }

                    } catch (PersistenceException ex) {
                        logger.error(TAG, "Error while initializing replica events handler", ex);
                    }
                }
            }
        });
	}
	
	public void add(Demand d) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.save(d);
            logger.warn("session.add("+d+")");
            
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
            logger.warn("session.update("+d+")");

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

        } catch (PersistenceException ex) {
        	logger.error(TAG, "Error while deleting object in Replica", ex);
        } finally {
            if (session != null) session.close();
        }
	}
}
