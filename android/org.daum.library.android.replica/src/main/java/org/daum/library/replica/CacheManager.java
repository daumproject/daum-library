package org.daum.library.replica;

import org.daum.library.replica.msg.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 14:52
 */
public class CacheManager implements  ICacheManger
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private DHashMap<String, Cache> store = new DHashMap<String,Cache>();
    private  ICluster cluster;

    public CacheManager(ICluster cluster)
    {
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
        } else
        {
            logger.error("The name of cache is null : Do you have set the annotations ?");
            return null;
        }
        return store.get(name);
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
      try {
        if(o instanceof Update){

            final Update update = (Update)o;

            if(!update.getSourceNode().equals(cluster.getCurrentNode()))
            {

                // notify
                NotifyUpdate notifyUpdate = new NotifyUpdate();
                notifyUpdate.setKey(update.getKey());
                notifyUpdate.setCache(update.cache);
                cluster.getChannel().write(notifyUpdate);

                // set update
                getCache(update.cache).localDispatch(update);


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
                logger.debug(" Receive snapshot from "+ storeSnapshot.source+" to "+ storeSnapshot.dest);

                for(Update msg:  storeSnapshot.snapshot)
                {
                    getCache(msg.cache).putIfAbsent(msg.key, msg.getVersionedValue());
                }

                cluster.getCurrentNode().setSynchronized();
            }

        } else if(o instanceof Command)
        {
            final Command command = (Command)o;

            if(command.getOp().equals(StoreRequest.HEARTBEAT))
            {
                if(!command.source.equals(cluster.getCurrentNode()))
                {
                    logger.info("Cluster "+ cluster.getNodesOfCluster());
                    cluster.addNode(command.getSourceNode());
                }
            } else  if(command.getOp().equals(StoreRequest.REQUEST_SNAPSHOT))
            {
                if(!cluster.getCurrentNode().isSynchronized())
                {
                    // todo check if isSynchronized = true
                    //   logger.error("The current CacheManager is not Synchronized");
                }

                  logger.debug(" "+cluster.getCurrentNode()+" "+command);
                if(!command.source.equals(cluster.getCurrentNode()) && command.dest.equals(cluster.getCurrentNode()))
                {
                    logger.info("Creating snapshot for "+ command.source);

                    final List<Update> current_snapshot= new ArrayList<Update>();

                    for(String namecache : store.keySet())
                    {
                        for( Object key: store.get(namecache).keySet()){

                            Update snapshot = new Update();
                            snapshot.op = StoreRequest.SNAPSHOT;
                            snapshot.dest = command.source;
                            snapshot.source = cluster.getCurrentNode();
                            snapshot.cache = namecache;
                            snapshot.key = key;
                            snapshot.setVersionedValue(store.get(namecache).get(key));
                            current_snapshot.add(snapshot);
                        }
                    }

                    logger.info("Snapshot is created for "+ command.source);

                    Snapshot storeSnapshot = new Snapshot();
                    storeSnapshot.snapshot =   current_snapshot;
                    storeSnapshot.source = cluster.getCurrentNode();
                    storeSnapshot.dest =   command.source;
                    cluster.getChannel().write(storeSnapshot);

                    logger.info("Snapshot is sent for "+ command.source);
                }
            }
        }
      }catch (Exception e){
          logger.error("processing MSG fail : ",e);
      }
    }



    public void remoteDisptach(Update update)
    {
        logger.debug("remoteDisptach "+update.cache);

        update.source = cluster.getCurrentNode();
        // todo group by block

        cluster.getChannel().write(update);
        // remoteDisptachManager.addUpdate(update);
    }





}
