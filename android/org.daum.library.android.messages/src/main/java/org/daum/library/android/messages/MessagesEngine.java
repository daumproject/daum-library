package org.daum.library.android.messages;

import android.util.Log;
//import org.daum.common.message.api.Message;
import org.daum.common.genmodel.impl.MessageAmbianceImpl;
import org.daum.library.android.messages.view.ListItemView.MessageType;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.daum.library.replica.listener.SyncListener;
import org.daum.library.replica.msg.SyncEvent;
import org.daum.common.genmodel.*;

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 29/06/12
 * Time: 09:47
 * To change this template use File | Settings | File Templates.
 */
public class MessagesEngine {

    private static final String TAG = "MessagesEngine";

    private OnEventListener listener;
    private final String nodeName;
    private PersistenceSessionFactoryImpl factory;

    public MessagesEngine(ReplicaStore store, final String nodeName) {
        this.nodeName = nodeName;

        try {
            // configuring persistence
            PersistenceConfiguration configuration = new PersistenceConfiguration(nodeName);
            configuration.setStore(store);
            configuration.addPersistentClass(MessageAmbianceImpl.class);

            // retrieve the persistence factory
            this.factory = configuration.getPersistenceSessionFactory();

        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while initializing persistence in engine", ex);
        }

        MessagesComponent.getChangeListenerInstance().addEventListener(MessageAmbianceImpl.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent e) {
                if (listener != null) {
                    PersistenceSession session = null;
                    try {
                        session = factory.getSession();
                        MessageAmbiance m = (MessageAmbiance) session.get(MessageAmbianceImpl.class, e.getId());

                        switch (e.getEvent()) {
                            case ADD:
                                MessageType type = null;
                                if (e.getNode().getNodeID().equals(nodeName)) {
                                    type = MessageType.OUT;
                                } else {
                                    type = MessageType.IN;
                                }
                                listener.onAdd(m, type);
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

        MessagesComponent.getChangeListenerInstance().addSyncListener(new SyncListener() {
            @Override
            public void sync(SyncEvent e) {
                PersistenceSession session = null;
                try {
                    session = factory.getSession();
                    Map<Object, MessageAmbianceImpl> map = session.getAll(MessageAmbianceImpl.class);
                    if (listener != null && map != null) listener.onReplicaSynced(map.values());

                } catch (PersistenceException ex) {
                    Log.e(TAG, "Error while retrieving session on replica sync", ex);

                } finally {
                    if (session != null) session.close();
                }
            }
        });
    }

    public void add(MessageAmbiance m) {
        PersistenceSession session = null;
        try {
            session = factory.getSession();
            session.save(m);

        } catch (PersistenceException ex) {
            Log.e(TAG, "Error while saving message "+m, ex);

        } finally {
            if (session != null) session.close();
        }
    }

    public void setOnEventListener(OnEventListener listener) {
        this.listener = listener;
    }

    public interface OnEventListener {
        /**
         * Called when a message has been added to the replica
         *
         * @param msg the new message
         * @param type the type of the message
         */
        public void onAdd(MessageAmbiance msg, MessageType type);

        /**
         * Called when the data in the replica are synced with other
         *
         * @param messages the complete list of messages
         */
        public void onReplicaSynced(Collection<? extends MessageAmbiance> messages);
    }
}
