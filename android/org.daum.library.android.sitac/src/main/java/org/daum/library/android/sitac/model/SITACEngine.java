package org.daum.library.android.sitac.model;

import java.util.Map;

import org.daum.common.model.api.ArrowAction;
import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.IModel;
import org.daum.common.model.api.Target;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.LocalStore;
import org.daum.library.ormH.utils.PersistenceException;

import android.util.Log;

/**
 * Here you should save/update/delete data from the Replica
 */
public class SITACEngine {
	
	private static final String TAG = "SITACEngine";

    private PersistenceSessionFactoryImpl factory;
	private OnEngineStateChangeListener listener;
	
	public SITACEngine(OnEngineStateChangeListener engineHandler) {
        try {
            PersistenceConfiguration configuration = new PersistenceConfiguration("FIXME");
            configuration.setStore(new LocalStore());
            configuration.addPersistentClass(Demand.class);
            configuration.addPersistentClass(Danger.class);
            configuration.addPersistentClass(Target.class);
            configuration.addPersistentClass(ArrowAction.class);

            this.factory = configuration.getPersistenceSessionFactory();

        } catch (PersistenceException e)
        {
            e.printStackTrace();
        }

		listener = engineHandler;
	}
	
	public void add(IModel m, IEntity e) {
        PersistenceSession session = null;
        try {
            session = factory.openSession();
            session.save(m);
            
            Map<Object, Object> map = session.getAll(m.getClass());
            for (Object o : map.values()) {
            	System.out.println("[[ADD]] "+o);
            }
            
        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while persisting object", ex);
        } finally {
            if (session != null) session.close();
        }

		if (listener != null) listener.onAdd(m, e);
	}

	public void update(IModel m, IEntity e) {
        PersistenceSession session = null;
        try {
            session = factory.openSession();
            session.update(m);
            
            Map<Object, Object> map = session.getAll(m.getClass());
            for (Object o : map.values()) {
            	System.out.println("[[MAJ]] "+o);
            }
            
        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while persisting object", ex);
        } finally {
            if (session != null) session.close();
        }

		if (listener != null) listener.onUpdate(m, e);
	}
	
	public void delete(IModel m, IEntity e) {
        PersistenceSession session = null;
        try {
            session = factory.openSession();
            session.delete(m);
            
            Map<Object, Object> map = session.getAll(m.getClass());
            for (Object o : map.values()) {
            	System.out.println("[[DEL]] "+o);
            }
            
        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while persisting object", ex);
        } finally {
            if (session != null) session.close();
        }
		if (listener != null) listener.onDelete(e);
	}
}
