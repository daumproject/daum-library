package org.daum.library.replica.cache;

import org.daum.library.replica.cluster.ICacheManger;
import org.daum.library.replica.cluster.ICluster;
import org.daum.library.replica.msg.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 14:52
 */
public class CacheManager implements ICacheManger
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private StoreMap<String, Cache> store = new StoreMap<String,Cache>();
    //   //private LRUMap<String, Cache> store = new LRUMap<String,Cache>(1000);
    private HashMap<String,Integer> history = new HashMap<String,Integer>();
    private ICluster cluster;


    public CacheManager(ICluster cluster)
    {
        this.cluster = cluster;
    }

    public ICluster getCluster() {
        return cluster;
    }

    public void setCluster(ICluster cluster) {
        this.cluster = cluster;
    }

    public  Cache getCache(String name)
    {
        if(name != null)
        {
            if(!store.containsKey(name))
            {
                logger.debug("Creating cache "+name);
                Cache cache  =  new Cache(name,this) ;
                store.put(name,cache);
            }
            return store.get(name);
        } else
        {
            logger.error("The name of cache is null : Do you have set the annotations ?");
            return null;
        }
    }


    public Set<String> getAllCache(){
        return  store.keySet();
    }

    public  int getNumberOfCache()
    {
        int count=0;
        for( Object key : store.keySet())
        {
            count += store.get(key).size();
        }
        count += store.size();
        return count;
    }


    public void processingMSG(Message o)
    {
        try
        {
            /* break cycle    */
            if(history.containsKey(((AMessage)o).getUuid()))
            {
                Integer counter = history.get(((AMessage)o).getUuid());
                counter++;
                if(counter > cluster.getNodesOfCluster().size()-1)
                {
                    history.remove(((AMessage)o).getUuid());
                }else
                {
                    history.put(((AMessage)o).getUuid(),counter);
                }

            } else
            {
                history.put(((AMessage)o).getUuid(),1);
                if(o instanceof Update)
                {
                    // first

                    final Update update = (Update)o;

                    if(!update.getSourceNode().equals(cluster.getCurrentNode()))
                    {
                        // set update
                        getCache(update.cache).localDispatch(update);

                        //notify update from remote
                        NotifyUpdate notifyUpdate = new NotifyUpdate();
                        notifyUpdate.setId(update.getKey());
                        notifyUpdate.setCache(update.cache);
                        notifyUpdate.setEvent(update.getEvent());
                        notifyUpdate.setNode(update.getSourceNode());

                        cluster.getChannel().write(notifyUpdate);

                    }
                }
                if(o instanceof Updates)
                {
                    final Updates updates = (Updates)o;

                    if(!updates.getSourceNode().equals(cluster.getCurrentNode()))
                    {
                        logger.debug("Remote receive "+updates.getUpdates().size());

                        for(Update update : updates.getUpdates()){
                            getCache(update.cache).localDispatch(update);
                        }

                    }

                }else  if(o instanceof Snapshot){

                    final Snapshot storeSnapshot = (Snapshot)o;

                    if(storeSnapshot.dest.equals(cluster.getCurrentNode()))
                    {
                        if(!cluster.getCurrentNode().isSynchronized())
                        {

                            new Thread(new Runnable() {
                                @Override
                                public void run()
                                {

                                    try
                                    {
                                        logger.debug(" Receive snapshot from "+ storeSnapshot.source+" to "+ storeSnapshot.dest+" "+storeSnapshot.getSnapshot().size());
                                         /*
                                        ByteArrayInputStream bais = new ByteArrayInputStream(storeSnapshot.getBytes());
                                        GZIPInputStream gzipIn = new GZIPInputStream(bais);

                                        ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
                                        snapshot  = ( List<Update>) objectIn.readObject();
                                        objectIn.close();
                                            */
                                        for(Update msg: storeSnapshot.getSnapshot())
                                        {
                                            Cache cache = getCache(msg.cache);
                                            cache.localDispatch(msg);
                                        }

                                        // notify is syncEvent
                                        SyncEvent syncEvent = new SyncEvent();
                                        // todo add node
                                        cluster.getChannel().write(syncEvent);
                                        cluster.getCurrentNode().setSynchronized();

                                        double duree = (System.currentTimeMillis() - cluster.getStart())  / 1000;

                                        logger.info(cluster.getCurrentNode().getNodeID()+" isSynchronized in "+duree+" secondes size="+ storeSnapshot.getSnapshot().size());
                                    }catch (Exception e)
                                    {
                                        try
                                        {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e1) {
                                            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                        }
                                        logger.error("Snapshot ",e);
                                        cluster.getCurrentNode().setSynchronized(false);
                                        cluster.synchronize();
                                    }

                                }
                            }).start();


                        }

                    }

                } else if(o instanceof Command)
                {
                    final Command command = (Command)o;

                    //  logger.debug("Command from : "+command.getSourceNode().getNodeID());

                    if(!command.getSourceNode().equals(cluster.getCurrentNode()))
                    {
                        if(command.getEvent().equals(StoreEvent.HEARTBEAT))
                        {
                            cluster.addNode(command.getSourceNode());

                        } else  if(command.getEvent().equals(StoreEvent.REQUEST_SNAPSHOT))
                        {
                            if(command.dest.equals(cluster.getCurrentNode()))
                            {

                                double startc= System.currentTimeMillis();
                                logger.info("Creating snapshot for "+ command.source);

                                final List<Update> current_snapshot= new ArrayList<Update>();

                                for(String namecache : store.keySet())
                                {
                                    for( Object key: store.get(namecache).keySet()){

                                        Update snapshot = new Update();
                                        snapshot.event = StoreEvent.SNAPSHOT;
                                        snapshot.dest = command.source;
                                        snapshot.source = cluster.getCurrentNode();
                                        snapshot.cache = namecache;
                                        snapshot.key = key;
                                        snapshot.setVersionedValue(store.get(namecache).get(key));
                                        current_snapshot.add(snapshot);
                                    }
                                }

                                logger.info("Snapshot is created for "+ command.source+" "+current_snapshot.size());

                                  /*
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
                                ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
                                objectOut.writeObject(current_snapshot);
                                objectOut.close();
                                   */

                                Snapshot storeSnapshot = new Snapshot();
                            //    storeSnapshot.setBytes(baos.toByteArray());
                                storeSnapshot.setSnapshot(current_snapshot);
                                storeSnapshot.source = cluster.getCurrentNode();
                                storeSnapshot.dest =   command.source;
                                cluster.getChannel().write(storeSnapshot);
                                double duree = (System.currentTimeMillis() - startc)  / 1000;
                                logger.info("Snapshot is sent for "+ command.source+" "+duree+" secondes");
                            } else
                            {
                                // ignore it's not for me
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("processing MSG fail : ",e);
        }
    }



    public void remoteDisptach(final Update update)
    {
        logger.debug("remoteDisptach "+update.cache);
        update.source = cluster.getCurrentNode();
        cluster.getChannel().write(update);
        // remoteDisptachManager.addUpdate(update);

        // notify an update from local
        NotifyUpdate notifyUpdate = new NotifyUpdate();
        notifyUpdate.setId(update.getKey());
        notifyUpdate.setCache(update.cache);
        notifyUpdate.setEvent(update.getEvent());
        notifyUpdate.setNode(update.getSourceNode());

        cluster.getChannel().write(notifyUpdate);
    }





}
