package org.daum.library.replica.listener;

import org.daum.library.replica.msg.NotifyUpdate;
import org.daum.library.replica.msg.SyncEvent;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 20/06/12
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public class ChangeListener implements IChangeListener
{

    private  HashMap<String,EventListenerList> listHashMap = new HashMap<String,EventListenerList>();
    private  EventListenerList syncList = new EventListenerList();


    private  static ChangeListener singleton = null;

    public static ChangeListener getInstance()
    {
        if(singleton == null){
            singleton = new ChangeListener();
        }
        return  singleton;
    }

    public void addSyncListener(SyncListener syncListener)
    {
        syncList.add(SyncListener.class,syncListener);
    }

    public void removeSyncListener(SyncListener syncListener)
    {
        syncList.add(SyncListener.class,syncListener);
    }

    public void addEventListener (Class zclass,PropertyChangeListener listener) {

        String cache = zclass.getName();

        if(listHashMap.containsKey(cache))
        {
            EventListenerList eventListenerList =   listHashMap.get(cache);
            eventListenerList.add(PropertyChangeListener.class,listener);
        } else
        {
            EventListenerList eventListenerList = new EventListenerList();
            eventListenerList.add(PropertyChangeListener.class,listener);
            listHashMap.put(cache, eventListenerList);
        }


    }

    public void removeEventListener (Class zclass,PropertyChangeListener listener) {
        String cache = zclass.getName();

        if(listHashMap != null)
        {

            EventListenerList eventListenerList =   listHashMap.get(cache);
            eventListenerList.remove(PropertyChangeListener.class, listener);

            if(eventListenerList.getListenerCount() == 0)
            {
                listHashMap.remove(cache);
            }
        }
    }

    public void receive(Object msg)
    {
        if(msg instanceof NotifyUpdate)
        {
            NotifyUpdate e = (NotifyUpdate)msg;
            if(listHashMap.containsKey(e.getCache()))
            {
                PropertyChangeEvent update = new PropertyChangeEvent();
                update.setId(e.getId());
                update.setNode(e.getNode());

                switch (e.getEvent())
                {
                    case ADD :
                        update.setEvent(Event.ADD);
                        break;
                    case UPDATE:
                        update.setEvent(Event.UPDATE);
                        break;

                    case DELETE:
                        update.setEvent(Event.DELETE);
                        break;
                }


                Object[] listeners = listHashMap.get(e.getCache()).getListenerList();
                if(listeners != null)
                {
                    for (int i = 0; i < listeners.length;i += 2)
                    {
                        ((PropertyChangeListener) listeners[i + 1]).update(update);
                    }

                }

            }

        }  else if(msg instanceof SyncEvent)
        {
            Object[] listeners =   syncList.getListenerList();
            for (int i = 0; i < listeners.length;i += 2)
            {
                ((SyncListener) listeners[i + 1]).sync((SyncEvent)msg);
            }
        }
    }

}
