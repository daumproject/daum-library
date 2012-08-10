package org.daum.library.replica.cluster;

import org.apache.jdbm.DB;
import org.apache.jdbm.DBMaker;
import org.daum.library.replica.cache.Cache;
import org.daum.library.replica.cache.CacheManager;
import org.daum.library.replica.cache.StoreEvent;
import org.daum.library.replica.cache.VersionedValue;
import org.daum.library.replica.channel.Channel;
import org.daum.library.replica.channel.KChannelImpl;
import org.daum.library.replica.msg.Command;
import org.daum.library.replica.msg.Message;
import org.daum.library.replica.utils.SystemTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;


/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 07/06/12
 * Time: 14:15
 */
public class ClusterImpl implements  ICluster,Runnable{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Node currentNode;
    private List<Node> nodesOfCluster = new ArrayList<Node>();
    private SystemTime systemTime = new SystemTime();
    private  Thread tsnapshot =null;
    private boolean alive=true;
    private Channel chanel;
    private  Thread theartbeat = null;
    private  ICacheManger cacheManger=null;
    private  final int freqHearBeat = 15000;
    private double start;
    private String path_disk="";
    private DB db=null;
    private boolean diskPersitence =false;

    public ClusterImpl(Node current, Channel chanel,boolean diskPersitence,String pathdisk)
    {
        alive = true;
        this.currentNode = current;
        this.chanel = chanel;
        theartbeat = new Thread(this);
        cacheManger = new CacheManager(this);
        this.diskPersitence = diskPersitence;

        // adding node id
        setPath_disk(pathdisk+File.separator+current.getNodeID()+File.separator);

        if(diskPersitence)
        {
            try
            {
                File folder = new File(path_disk);

                if(!folder.exists())
                {
                    logger.debug("Creating disk path "+folder.getAbsolutePath());
                    folder.mkdirs();
                    folder.canRead();
                    folder.canWrite();
                }

                db  = DBMaker.openFile(folder.getAbsolutePath()+File.separator+"store") .make();
                restoreFromDB();

            }catch (Exception e)
            {
                logger.error("Disk persistence ",e);
                diskPersitence =false;
            }
        }


        theartbeat.start();
    }

    public boolean isDiskPersitence() {
        return diskPersitence;
    }

    public void setDiskPersitence(boolean diskPersitence) {
        this.diskPersitence = diskPersitence;
    }

    public void restoreFromDB()
    {
        if(diskPersitence)
        {
            // restore
            for(String key_cache :   db.getCollections().keySet())
            {
                logger.debug("Reading cache from diskPersitence "+key_cache);
               try
               {
                   Cache cache =      cacheManger.getCache(key_cache) ;
                   SortedMap<String,VersionedValue> disk = (SortedMap<String, VersionedValue>) db.getCollections().get(key_cache);

                   for(Object key_row : disk.keySet())
                   {
                       logger.debug("Reading object from diskPersitence "+key_row);

                       cache.put(key_row,disk.get(key_row));
                   }
               }catch (Exception e)
               {
                    logger.warn("Restore from disk ",e);
               }

            }
        }
    }

    public void shutdown()
    {
        logger.debug("Cluster is closing");
        alive = false;

        if(db != null)
        {
            db.commit();
            db.close();
        }
        if(theartbeat != null){
            theartbeat.interrupt();
        }
        if(tsnapshot != null){
            tsnapshot.interrupt();
        }
    }


    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    @Override
    public Channel getChannel() {
        return chanel;
    }

    public  List<Node> getNodesOfCluster() {
        return nodesOfCluster;
    }

    @Override
    public Node getCurrentNode() {
        return currentNode;
    }


    public Node searchNode(Node n){
        for (Node node : nodesOfCluster){
            if(node.equals(n)){
                return  node;
            }
        }
        return null;
    }
    public void addNode(Node n)
    {
        if(!currentNode.equals(n))
        {
            Node exist =  searchNode(n);
            if(exist !=null)
            {
                //  logger.debug("Update node tick "+n.getNodeID());
                exist.setLastTickTime(systemTime.getNanoseconds());
                exist.setSynchronized(n.isSynchronized());
            } else
            {
                //    logger.debug("addNode "+n.getNodeID());
                n.setLastTickTime(systemTime.getNanoseconds());
                nodesOfCluster.add(n);
            }
        }
    }

    public boolean synchronizedNodesInCluster()
    {
        for( Node node : nodesOfCluster){
            if(node.isSynchronized()){
                return true;
            }
        }
        return false;
    }


    public void remoteReceived(Message o)
    {

        cacheManger.processingMSG(o);
    }

    public void synchronize()
    {
        tsnapshot = new Thread(new Runnable() {
            @Override
            public void run()
            {

                while (getNodesOfCluster().isEmpty() && alive == true && !Thread.currentThread().isInterrupted())
                {
                    logger.debug("Waiting to discovery nodes..."+getNodesOfCluster());
                    try
                    {
                        Thread.sleep(freqHearBeat*2);
                    } catch (InterruptedException e) {
                        //ignore
                    }
                }

                Long min =Long.MAX_VALUE;
                Node nodeReqSnapshot=null;
                boolean sync = synchronizedNodesInCluster();

                for( Node node : nodesOfCluster){

                    if(!currentNode.equals(node))
                    {
                        if(sync)
                        {
                            if(node.getLastTickTime() < min && node.isSynchronized())
                            {
                                nodeReqSnapshot = node;
                            }
                        } else
                        {
                            if(node.getLastTickTime() < min)
                            {
                                nodeReqSnapshot = node;
                            }
                        }
                    }
                }

                Command req = new Command();
                req.event = StoreEvent.REQUEST_SNAPSHOT;
                req.source = currentNode;
                req.dest = nodeReqSnapshot;

                logger.info("Synchronization request sent to" + req.dest);

                chanel.write(req);
                start= System.currentTimeMillis();
            }
        });

        tsnapshot.start();

    }

    public double getStart() {
        return start;
    }

    @Override
    public void setPath_disk(String path_disk)
    {
        this.path_disk = path_disk;
    }

    @Override
    public String getPath_disk() {
        return path_disk;
    }


    @Override
    public ICacheManger getCacheManager() {
        return cacheManger;
    }

    @Override
    public void setChanel(KChannelImpl kChannel) {
        this.chanel = kChannel;
    }


    // send heartbeat to notify other peer that the node is connected
    @Override
    public void run() {

        while (alive == true && !Thread.currentThread().isInterrupted())
        {
            try
            {
                Thread.sleep(freqHearBeat);
            }  catch (Exception e){
                //ignore
            }
            Command req = new Command();
            req.event = StoreEvent.HEARTBEAT;
            req.source = currentNode;
            req.dest = null;

            chanel.write(req);

            //   logger.debug("Sending heatbeat "+ currentNode.getNodeID());
        }
        logger.debug("HeartBeat is closed");
    }
}
