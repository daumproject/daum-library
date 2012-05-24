package org.daum.library.replicatingMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
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

    public CacheManager(Channel channel,String id)
    {
        this.chanel = channel;
        this.id =id;
        thread.start();
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public  Cache getCache(String name)
    {
        if(!store.containsKey(name))
        {
            logger.debug("Creating cache "+name);
            Cache cache  =  new Cache(name,this) ;
            store.put(name,cache);
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

    public void remoteReceived(Object o){

        if(o instanceof Replicator)
        {
            final Replicator replicator = (Replicator)o;

            if(replicator.op == Operation.MAKESNAPSHOT)
            {

                if(!replicator.source.equals(id) && replicator.dest.equals(id))
                {
                    logger.debug(" REQUEST MAKESNAPSHOT "+ replicator.source);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(String namecache : store.keySet()){

                                for( Object key: store.get(namecache).keySet()){

                                    Replicator snapshot = new Replicator();
                                    snapshot.op = Operation.SNAPSHOT;
                                    snapshot.dest = replicator.source;
                                    snapshot.source = id;

                                    snapshot.cache = namecache;
                                    snapshot.key = key;
                                    snapshot.value = store.get(namecache).get(key);

                                    chanel.write(snapshot);

                                }
                                // notify
                                Replicator snapshot = new Replicator();
                                snapshot.op = Operation.SNAPSHOT_FINISH;
                                snapshot.dest = replicator.source;
                                snapshot.source = id;
                                chanel.write(snapshot);


                            }
                        }
                    }) .start();

                }

            }
            else  if(replicator.op == Operation.SNAPSHOT)
            {
                if(replicator.dest.equals(id))
                {
                    logger.debug(" Receive snapshot from "+ replicator.source+" to "+ replicator.dest);
                    getCache(replicator.cache).putIfAbsent(replicator.key, replicator.value);
                }

            } else if(replicator.op == Operation.HEARTBEAT)
            {
                if(!replicator.source.equals(id))
                {
                    logger.info(" RECEIVE "+ Operation.HEARTBEAT+" "+nodes);
                    long start = System.nanoTime();
                    nodes.put(replicator.source,start);
                }

            }
            else  if(replicator.op == Operation.SNAPSHOT_FINISH)
            {
                isSynchronized =true;
                logger.info("Replication is finish");

            } else
            {
                logger.debug("Remote Received "+((Replicator) o).op);
                getCache(replicator.cache).localDispatch(replicator);
            }

        }
    }

    public void remoteDisptach(Replicator e)
    {
        logger.debug("remoteDisptach "+e.key);
        e.source = id;
        chanel.write(e);
    }

    public void sendRequestSnapshot(String node_id){

        Replicator req = new Replicator();
        req.op= Operation.MAKESNAPSHOT;
        req.source = id;
        req.dest = node_id;
        chanel.write(req);

    }


    public void snapshot()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Replicator req = new Replicator();
                req.op= Operation.MAKESNAPSHOT;
                req.source = id;
                while (nodes.keySet().isEmpty() && alive == true){

                    logger.debug("Wait node ");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        //ignore
                    }
                }
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

                logger.info("Replication is processing with " + req.source);

                chanel.write(req);

            }
        }).start();

    }


    @Override
    public void run() {

        while (alive)
        {
            Replicator req = new Replicator();
            req.op= Operation.HEARTBEAT;
            req.source = id;
            chanel.write(req);

            logger.debug(""+Operation.HEARTBEAT);
            logger.warn(" "+getCount());
            try
            {
                Thread.sleep(5000);
            }  catch (Exception e){
                //ignore
            }

        }

    }
}
