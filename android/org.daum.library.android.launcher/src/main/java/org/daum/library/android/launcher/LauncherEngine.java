package org.daum.library.android.launcher;

import android.util.Log;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.daum.library.replica.listener.SyncListener;
import org.daum.library.replica.msg.SyncEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/07/12
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
public class LauncherEngine {

    private static final String TAG = "LauncherEngine";

    private PersistenceSessionFactoryImpl factory;

    public LauncherEngine(final String nodeName, ReplicaStore store) {
        try {
            // configuring persistence
            PersistenceConfiguration configuration = new PersistenceConfiguration(nodeName);
            configuration.setStore(store);

            // retrieve the persistence factory
            this.factory = configuration.getPersistenceSessionFactory();

        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while initializing persistence in engine", ex);
        }

        // add a callback to populate engine when sync is ready
        /*
        LauncherComponent.getChangeListener().addSyncListener(new SyncListener() {
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
        */
    }
}
