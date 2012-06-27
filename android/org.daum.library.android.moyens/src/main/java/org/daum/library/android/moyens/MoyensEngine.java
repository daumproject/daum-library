package org.daum.library.android.moyens;

import android.util.Log;
import org.daum.common.model.api.Demand;
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

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 27/06/12
 * Time: 14:41
 * To change this template use File | Settings | File Templates.
 */
public class MoyensEngine {

    private static final String TAG = "MoyensEngine";

    private OnEventListener listener;
    private final String nodeName;
    private PersistenceSessionFactoryImpl factory;

    public MoyensEngine(ReplicaStore store, final String nodeName) {
        this.nodeName = nodeName;

        try {
            // configuring persistence
            PersistenceConfiguration configuration = new PersistenceConfiguration(nodeName);
            configuration.setStore(store);
            configuration.addPersistentClass(Demand.class);

            // retrieve the persistence factory
            this.factory = configuration.getPersistenceSessionFactory();

        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while initializing persistence in engine", ex);
        }

        ChangeListener.getInstance().addEventListener(Demand.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent e) {
                if (listener != null) {
                    PersistenceSession session = null;
                    try {
                        session = factory.getSession();
                        Demand d = (Demand) session.get(Demand.class, e.getId());

                        switch (e.getEvent()) {
                            case ADD:
                                if (!e.getNode().getNodeID().equals(nodeName)) {
                                    listener.onRemoteAdd(d);
                                }
                                break;

                            case UPDATE:
                                if (!e.getNode().getNodeID().equals(nodeName)) {
                                    listener.onRemoteUpdate(d);
                                }
                                break;
                        }

                    } catch (PersistenceException ex) {
                        Log.e(TAG, "Error while processing event listener in replica", ex);

                    } finally {
                        if (session != null) session.close();
                    }
                }
            }
        });

        ChangeListener.getInstance().addSyncListener(new SyncListener() {
            @Override
            public void sync(SyncEvent e) {
                PersistenceSession session = null;
                try {
                    session = factory.getSession();
                    Map<Object, Demand> map = (Map<Object, Demand>) session.getAll(Demand.class);
                    if (listener != null) listener.onReplicaSynced(map.values());

                } catch (PersistenceException ex) {
                    Log.e(TAG, "Error while retrieving session on replica sync", ex);

                } finally {
                    if (session != null) session.close();
                }
            }
        });
    }

    public void add(Demand d) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.save(d);
            if (listener != null) listener.onLocalAdd(d);

        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while saving demand "+d, ex);

        } finally {
            if (session != null) session.close();
        }
    }

    public void setOnEventListener(OnEventListener listener) {
        this.listener = listener;
    }

    public interface OnEventListener {
        public void onLocalAdd(Demand d);

        public void onRemoteAdd(Demand d);
        public void onRemoteUpdate(Demand d);

        public void onReplicaSynced(Collection<Demand> data);
    }
}
