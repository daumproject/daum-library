package org.kevoree.library.ormHM.leader;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;
import org.apache.zookeeper.Watcher;

import java.util.NavigableSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 16/05/12
 * Time: 17:42
 */
public class PersistanceServer  extends Thread implements IZkDataListener, IZkStateListener {

    private static final Logger LOG = Logger.getLogger(PersistanceServer.class);

    private ZkClient zkClient;

    private SequentialZkNode zkNode;

    private SequentialZkNode zkLeaderNode;

    private volatile boolean shutdown;

    private final String nodeNameLeader;
    private final Object mutex = new Object();

    private final AtomicBoolean start = new AtomicBoolean(false);

    private final Random random = new Random();
    public PersistanceServer(String nodeNameLeader, int zkPort) {
        this.nodeNameLeader = nodeNameLeader;
        this.zkClient = new ZkClient("localhost:" + zkPort);

    }

    public boolean isLeader() {
        return start.get();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(random.nextInt(1000));
            // Create Ephermal node
            zkNode = ZkUtils.createEpheremalNode("/leader", nodeNameLeader, zkClient);

            // Find all children
            NavigableSet<SequentialZkNode> nodes = ZkUtils.getNodes("/leader", zkClient);

            zkLeaderNode = ZkUtils.findLeaderOfNode(nodes, zkNode);

            if (zkLeaderNode.getPath().equals(zkNode.getPath())) {
                start.set(true);
            }

            // SEt a watch on next leader path
            zkClient.subscribeDataChanges(zkLeaderNode.getPath(), this);
            zkClient.subscribeStateChanges(this);

            synchronized (mutex) {
                while (!start.get()) {
                    LOG.info("Server on Port:" + nodeNameLeader + " waiting to spawn socket....");
                    mutex.wait();
                }
            }

            System.out.println("CacheManger is available on " + nodeNameLeader + " is now the Leader. Starting to accept connections...");

            // TODO




        }
        catch (Exception e) {

        }

        finally {
            synchronized (mutex) {


                if (zkClient != null) {
                    zkClient.close();
                    zkClient = null;
                }
            }
        }
        LOG.info("Server on Port:" + nodeNameLeader + " has been shutdown");
    }

    public void shutdown() {
        if (shutdown) {
            return;
        }

        shutdown = true;

        interrupt();

        // No more server socket jobs
        synchronized (mutex) {
            if (zkClient != null) {
                zkClient.close();
                zkClient = null;
            }
        }
    }


    private void electNewLeader() {
        final NavigableSet<SequentialZkNode> nodes = ZkUtils.getNodes("/echo", zkClient);

        if (nodes.size() == 0) {
            // No nodes present
            return;
        }

        if (!zkClient.exists(zkLeaderNode.getPath())) {
            // My Leader does not exist
            zkLeaderNode = ZkUtils.findLeaderOfNode(nodes, zkNode);
            zkClient.subscribeDataChanges(zkLeaderNode.getPath(), this);
            zkClient.subscribeStateChanges(this);

        }

        // If I am the leader then start
        if (zkNode.getSequence().equals(nodes.first().getSequence())) {
            System.out.println("Server on :" + nodeNameLeader + "  will now be notifed to assume leadership");
            synchronized (mutex) {
                start.set(true);
                mutex.notify();
            }
        }
    }

    @Override
    public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {}

    @Override
    public void handleNewSession() throws Exception {}

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {}

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        if (dataPath.equals(zkLeaderNode.getPath())) {
            // Leader gone away
            LOG.info("Recived a notification that Leader on path:" + dataPath
                    + " has gone away..electing new leader");
            electNewLeader();
        }
    }
}
