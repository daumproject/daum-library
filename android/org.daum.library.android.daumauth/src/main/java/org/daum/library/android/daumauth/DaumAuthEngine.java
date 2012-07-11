package org.daum.library.android.daumauth;

import android.util.Log;
import org.daum.library.android.daumauth.controller.IConnectionEngine;
import org.daum.library.android.daumauth.controller.IInterventionEngine;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.daum.library.replica.listener.SyncListener;
import org.daum.library.replica.msg.SyncEvent;
import org.sitac.*;
import org.sitac.impl.AgentImpl;
import org.sitac.impl.InterventionImpl;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/07/12
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
public class DaumAuthEngine implements IConnectionEngine, IInterventionEngine {

    private static final String TAG = "DaumAuthEngine";

    private PersistenceSessionFactoryImpl factory;
    private OnStoreSyncedListener listener;
    private boolean synced;

    public DaumAuthEngine(final String nodeName, ReplicaStore store, boolean storeSynced) {
        this.synced = storeSynced;

        try {
            // configuring persistence
            PersistenceConfiguration configuration = new PersistenceConfiguration(nodeName);
            configuration.setStore(store);
            // TODO fix that
            for (Class c : SitacFactory.classes()) configuration.addPersistentClass(c);

            // retrieve the persistence factory
            this.factory = configuration.getPersistenceSessionFactory();

        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while initializing persistence in engine", ex);
        }

        // add a callback to populate engine when sync is ready
        DaumAuthComponent.getChangeListener().addSyncListener(new SyncListener() {
            @Override
            public void sync(SyncEvent e) {
                synced = true;
                if (listener != null) listener.onStoreSynced();
                Log.w(TAG, "DaumAuthComponent.getChangeListener().addSyncListener");
            }
        });


        DaumAuthComponent.getChangeListener().addEventListener(InterventionImpl.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent e) {
                if (listener != null) listener.onInterventionsUpdated();
            }
        });
    }

    @Override
    public boolean authenticate(String matricule, String password) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            Map<String, AgentImpl> agents = session.getAll(AgentImpl.class);
            for (Agent a : agents.values()) {
                if (a.getMatricule().equals(matricule) && a.getPassword().equals(password)) {
                    return true;
                }
            }

        } catch (PersistenceException e) {
            Log.e(TAG, "Error while trying to authenticate user", e);
        } finally {
            if (session != null) session.close();
        }

        return false;
    }

    public ArrayList<Intervention> getInterventions() {
        ArrayList<Intervention> interventions = new ArrayList<Intervention>();
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            Map<Object, InterventionImpl> map = session.getAll(InterventionImpl.class);
            Log.w(TAG, "getInterventions.size = "+map.size());
            interventions.addAll(map.values());

        } catch (PersistenceException e) {
            Log.e(TAG, "Error while retrieving interventions", e);
        } finally {
            if (session != null) session.close();
        }

        return interventions;
    }

    public void setOnStoreSyncedListener(OnStoreSyncedListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isSynced() {
        return synced;
    }

    public interface OnStoreSyncedListener {
        /**
         * Called when the store is synced
         */
        void onStoreSynced();

        /**
         * Called when the interventions changed in the replica
         */
        void onInterventionsUpdated();
    }
}

