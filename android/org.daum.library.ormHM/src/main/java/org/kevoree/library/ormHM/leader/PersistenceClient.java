package org.kevoree.library.ormHM.leader;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;
import org.apache.zookeeper.Watcher;

import java.util.List;
import java.util.NavigableSet;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 16/05/12
 * Time: 17:42
 */
public class PersistenceClient  implements IZkDataListener, IZkStateListener, IZkChildListener {

    private static final Logger LOG = Logger.getLogger(PersistenceClient.class);
    private Object mutex = new Object();

    private final ZkClient zkClient;

    private SequentialZkNode connectedToNode;
    private String nodename=null;
    public PersistenceClient(int zkPort) {
        this.zkClient = new ZkClient("localhost:" + zkPort);
    }

    private void getcurrentLeader() throws  InterruptedException {

        synchronized (mutex) {


            NavigableSet<SequentialZkNode> nodes = null;

            while ((nodes = ZkUtils.getNodes("/leader", zkClient)).size() == 0)
            {
                //System.out.println("No echo service nodes ...waiting...");
                zkClient.subscribeChildChanges("/leader", this);
                mutex.wait();
            }

            connectedToNode = nodes.first();
            nodename = zkClient.readData(connectedToNode.getPath());

            zkClient.subscribeDataChanges(connectedToNode.getPath(), this);
            zkClient.subscribeStateChanges(this);

        }

    }

    public String getLeader() throws InterruptedException {
            return nodename;
    }



    public void shutdown() {
        synchronized (mutex) {

            zkClient.close();
        }
    }

    @Override
    public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {

    }

    @Override
    public void handleNewSession() throws Exception {}

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {}

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        synchronized (mutex) {
            if (connectedToNode != null && dataPath.equals(connectedToNode.getPath())) {
                System.out.println("Client Received notification that Server has died..notifying to re-connect");

                if (connectedToNode != null) {
                    connectedToNode = null;
                }
            }
            mutex.notify();
        }
    }

    @Override
    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
        getcurrentLeader();
    }
}
