package org.daum.library.android.sitac.engine;

import java.util.ArrayList;
import java.util.Collection;

import org.daum.common.model.api.ArrowAction;
import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.IModel;
import org.daum.common.model.api.Target;
import org.daum.common.model.api.ZoneAction;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.persistence.PersistentClass;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;

import android.util.Log;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.daum.library.replica.listener.SyncListener;
import org.daum.library.replica.msg.SyncEvent;

/**
 * SITACEngine save/update/delete data from/to the Replica/Persistence system
 */
public class SITACEngine {
	
	private static final String TAG = "SITACEngine";

    /** Persisted classes in replica */
    private final Class[] classes = {
            Demand.class,
            Danger.class,
            Target.class,
            ArrowAction.class,
            ZoneAction.class
    };
    private final String nodeName;

    private PersistenceSessionFactoryImpl factory;
	private OnEngineStateChangeListener listener;
	
	public SITACEngine(final String nodeName, ReplicaStore store, OnEngineStateChangeListener engineHandler) {
        this.nodeName = nodeName;
        listener = engineHandler;

        try {
            // configuring persistence
            PersistenceConfiguration configuration = new PersistenceConfiguration(nodeName);
            configuration.setStore(store);
            for (Class c : classes) configuration.addPersistentClass(c);

            // retrieve the persistence factory
            this.factory = configuration.getPersistenceSessionFactory();

        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while initializing persistence in engine", ex);
        }

        // add a callback to populate engine when sync is ready
        ChangeListener.getInstance("SITACDAUM").addSyncListener(new SyncListener() {
            @Override
            public void sync(SyncEvent e) {
                if (listener != null) {
                    PersistenceSession session = null;
                    try {
                        session = factory.getSession();
                        ArrayList<IModel> data = new ArrayList<IModel>();
                        for (Class c : classes) {
                            data.addAll((Collection<IModel>) session.getAll(c).values());
                        }
                        listener.onReplicaSynced(data);

                    } catch (PersistenceException ex) {
                        Log.e(TAG, "Error while retrieving data on sync", ex);

                    } finally {
                        if (session != null) session.close();
                    }
                }
            }
        });

        // add callback to handle remote events on replica
        for (final Class c : classes) {
            ChangeListener.getInstance("SITACDAUM").addEventListener(c, new PropertyChangeListener() {
                @Override
                public void update(PropertyChangeEvent e) {
                    if (listener != null) {
                        PersistenceSession session = null;
                        try {
                            session = factory.getSession();
                            IModel m = null;
                            switch (e.getEvent()) {
                                case ADD:
                                    m = (IModel) session.get(c, e.getId());
                                    Log.d(TAG, "Replica Event: ADD "+m);
                                    listener.onAdd(m);
                                    break;

                                case DELETE:
                                    Log.d(TAG, "Replica Event: DELETE "+e.getId());
                                    listener.onDelete((String) e.getId());
                                    break;

                                case UPDATE:
                                    m = (IModel) session.get(c, e.getId());
                                    Log.d(TAG, "Replica Event: UPDATE "+m);
                                    listener.onUpdate(m);
                                    break;
                            }

                        } catch (PersistenceException ex) {
                            Log.e(TAG, "Error while initializing replica events handler", ex);
                        } finally {
                            if (session != null) session.close();
                        }
                    }
                }
            });
        }
	}
	
	public void add(IModel m) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.save(m);
            
        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while adding object in Replica", ex);
        } finally {
            if (session != null) session.close();
        }
	}

	public void update(IModel m) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.update(m);

        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while updating object in Replica", ex);
        } finally {
            if (session != null) session.close();
        }
	}
	
	public void delete(IModel m) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.delete(m);
            
        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while deleting object in Replica", ex);
        } finally {
            if (session != null) session.close();
        }
	}
}
