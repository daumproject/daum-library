package org.daum.library.replicatingMap;

import org.daum.library.replicatingMap.msg.Command;
import org.daum.library.replicatingMap.msg.Snapshot;
import org.daum.library.replicatingMap.msg.Update;
import org.daum.library.replicatingMap.msg.Updates;
import org.daum.library.replicatingMap.utils.SystemTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 14:52
 */
public class CacheManager implements  Runnable{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Channel chanel;
    private String id;
    private DHashMap<String, Cache> store = new DHashMap<String,Cache>();
    private HashMap<String,Long> nodes = new HashMap<String, Long>();
    private boolean alive = true;
    private  Thread thread = new Thread(this);
    private boolean isSynchronized = false;
    private SystemTime systemTime = new SystemTime();
   // private  RemoteDisptachManager remoteDisptachManager= null;

    public CacheManager(Channel channel,String id)
    {
        this.chanel = channel;
        this.id =id;
        alive = true;
    //    remoteDisptachManager = new RemoteDisptachManager(id,chanel);
        thread.start();

    }

    public void shutdown(){
        alive = false;
        thread.interrupt();
    }
    public void setChanel(Channel chanel) {
        this.chanel = chanel;
    }


    public boolean isSynchronized() {
        return isSynchronized;
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

    public   int getCount(){
        int count=0;
        for( Object key : store.keySet())
        {
            count += store.get(key).size();
        }
        count += store.size();
        return count;
    }

    public void remoteReceived(Object o)
    {

        if(o instanceof Update){

            final Update update = (Update)o;

            if(!update.getSource().equals(id))
            {
                getCache(update.cache).localDispatch(update);
            }
        }
        if(o instanceof Updates)
        {
            final Updates updates = (Updates)o;

            if(!updates.getSource().equals(id))
            {
                logger.debug("Remote receive "+updates.getUpdates().size());
                for(Update update : updates.getUpdates()){
                    getCache(update.cache).localDispatch(update);
                }

            }

        }else  if(o instanceof Snapshot){

            final Snapshot storeSnapshot = (Snapshot)o;

            if(storeSnapshot.dest.equals(id))
            {
                logger.debug(" Receive snapshot from "+ storeSnapshot.source+" to "+ storeSnapshot.dest);

                for(Update msg:  storeSnapshot.snapshot)
                {
                    getCache(msg.cache).putIfAbsent(msg.key, msg.value);
                }

                isSynchronized = true;
            }

        } else if(o instanceof Command)
        {
            final Command command = (Command)o;

            if(command.getOp().equals(StoreRequest.HEARTBEAT))
            {
                if(!command.source.equals(id))
                {
                    logger.info("Replica Cluster "+ nodes);
                    long start = systemTime.getNanoseconds();
                    nodes.put(command.source,start);
                }
            } else  if(command.getOp().equals(StoreRequest.REQUEST_SNAPSHOT))
            {
                if(!isSynchronized)
                {
                    // todo check if isSynchronized = true
                 //   logger.error("The current CacheManager is not Synchronized");
                }


                if(!command.source.equals(id) && command.dest.equals(id))
                {
                    logger.info("Creating snapshot for "+ command.source);

                    final List<Update> current_snapshot= new ArrayList<Update>();

                    for(String namecache : store.keySet())
                    {
                        for( Object key: store.get(namecache).keySet()){

                            Update snapshot = new Update();
                            snapshot.op = StoreRequest.SNAPSHOT;
                            snapshot.dest = command.source;
                            snapshot.source = id;
                            snapshot.cache = namecache;
                            snapshot.key = key;
                            snapshot.value = store.get(namecache).get(key);
                            current_snapshot.add(snapshot);
                        }
                    }

                    logger.info("Snapshot is created for "+ command.source);

                    Snapshot storeSnapshot = new Snapshot();
                    storeSnapshot.snapshot =   current_snapshot;
                    storeSnapshot.source = id;
                    storeSnapshot.dest =   command.source;
                    chanel.write(storeSnapshot);

                    logger.info("Snapshot is sent for "+ command.source);
                }
            }
        }
    }

    public void remoteDisptach(Update update)
    {
        logger.debug("remoteDisptach "+update.cache);
        update.source = id;
        // todo group by block
        chanel.write(update);
        // remoteDisptachManager.addUpdate(update);
    }

    public void snapshot()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Command req = new Command();
                req.op= StoreRequest.REQUEST_SNAPSHOT;
                req.source = id;
                while (nodes.keySet().isEmpty() && alive == true){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        //ignore
                    }
                }

                // todo check if the nodes is already replicated and if the node didn't respond in time request an other snapshot
                Long min =Long.MAX_VALUE;
                String node ="";
                for(String key : nodes.keySet())
                {
                    if(!key.equals(id)){
                        if(nodes.get(key) < min)
                        {
                            min =  nodes.get(key);
                            node = key;
                        }
                    }
                }
                req.dest = node;

                logger.info("Replication is processing with " + req.dest);

                chanel.write(req);

            }
        }).start();
    }


    @Override
    public void run() {

        try {
            Thread.sleep(9500);
        } catch (InterruptedException e) {

        }

        while (alive && !Thread.currentThread().isInterrupted())
        {
            Command req = new Command();
            req.op= StoreRequest.HEARTBEAT;
            req.source = id;
            req.setReplicated(isSynchronized());

            chanel.write(req);

            logger.debug("Sending "+ StoreRequest.HEARTBEAT);
          //  logger.warn(" "+getCount());
            try
            {
                Thread.sleep(9000);
            }  catch (Exception e){
                //ignore
            }

        }

    }
}
