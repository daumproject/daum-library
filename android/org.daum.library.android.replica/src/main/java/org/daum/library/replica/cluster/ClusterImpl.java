package org.daum.library.replica.cluster;

import org.daum.library.replica.cache.CacheManager;
import org.daum.library.replica.cache.StoreEvent;
import org.daum.library.replica.channel.Channel;
import org.daum.library.replica.channel.KChannelImpl;
import org.daum.library.replica.msg.Command;
import org.daum.library.replica.msg.Message;
import org.daum.library.replica.utils.SystemTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


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
    private  final int freqHearBeat = 5000;


    public ClusterImpl(Node current, Channel chanel)
    {
        alive = true;
        this.currentNode = current;
        this.chanel = chanel;
        theartbeat = new Thread(this);
        cacheManger = new CacheManager(this);
        theartbeat.start();

    }

    public void shutdown()
    {
        logger.debug("Cluster is closing");
        alive = false;
        theartbeat.interrupt();
        tsnapshot.interrupt();
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
                logger.debug("Update node tick "+n.getNodeID());
                exist.setLastTickTime(systemTime.getNanoseconds());
                exist.setSynchronized(n.isSynchronized());
            } else
            {
                logger.debug("addNode "+n.getNodeID());
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
            }
        });

        tsnapshot.start();

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

            logger.debug("Sending heatbeat "+ currentNode.getNodeID());
        }
        logger.debug("HeartBeat is closed");
    }
}
