package org.daum.library.android.sitac.engine;

import java.util.Map;

import org.daum.common.model.api.ArrowAction;
import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.IModel;
import org.daum.common.model.api.Target;
import org.daum.common.model.api.ZoneAction;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.LocalStore;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;

import android.util.Log;

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

    private PersistenceSessionFactoryImpl factory;
	private OnEngineStateChangeListener listener;
	
	public SITACEngine(String nodeName, ReplicaStore store, OnEngineStateChangeListener engineHandler) {
        PersistenceSession session = null;

        try {
            // configuring persistence
            PersistenceConfiguration configuration = new PersistenceConfiguration(nodeName);
            configuration.setStore(store);
            for (Class c : classes) configuration.addPersistentClass(c);

            // retrieve the persistence factory
            this.factory = configuration.getPersistenceSessionFactory();

            // check if there is already some data in the replica
            if (listener != null) {
                session = factory.getSession();
                Map<Object, Object> objects;
                for (Class c : classes) {
                    objects = session.getAll(c);
                    for (Object o : objects.values()) {
                        listener.onAdd((IModel) o, null);
                    }
                }
            }

            // add callback to handle remote events on replica
            // TODO


        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while initializing persistence in engine", ex);

        } finally {
            if (session != null) session.close();
        }

        listener = engineHandler;
	}
	
	public void add(IModel m, IEntity e) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.save(m);
            
        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while adding object in Replica", ex);
        } finally {
            if (session != null) session.close();
        }

		if (listener != null) listener.onAdd(m, e);
	}

	public void update(IModel m, IEntity e) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.update(m);

        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while updating object in Replica", ex);
        } finally {
            if (session != null) session.close();
        }

		if (listener != null) listener.onUpdate(m, e);
	}
	
	public void delete(IModel m, IEntity e) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.delete(m);
            
        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while deleting object in Replica", ex);
        } finally {
            if (session != null) session.close();
        }
		if (listener != null) listener.onDelete(e);
	}
}
