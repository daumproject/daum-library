package org.kevoree.library.ormHM.leader;

import org.I0Itec.zkclient.IDefaultNameSpace;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkServer;
import org.apache.zookeeper.CreateMode;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 16/05/12
 * Time: 17:51
 */

/*http://sleeplessinslc.blogspot.fr/2012/01/apache-zookeeper-maven-example.html */
public class Tester {
    private static final int ZK_PORT = 29980;


    public static  void main(String[] arg){
        ZkServer zkServer;
        zkServer = new ZkServer("/tmp/zkExample/data", "/tmp/zkExample/log", new IDefaultNameSpace() {
            @Override
            public void createDefaultNameSpace(ZkClient zkClient) {}
        }, ZK_PORT);
        zkServer.start();

        // Delete existing node if any and create the service node
        ZkClient client = new ZkClient("localhost:" + ZK_PORT);
        client.deleteRecursive("/echo");
        client.create("/echo", new byte[0], CreateMode.PERSISTENT);
        client.close();
        PersistenceClient _client=null;
        PersistanceServer serverA=null;
        PersistanceServer serverB=null;
        try{


            _client = new PersistenceClient(ZK_PORT);
            serverA = new PersistanceServer("node0", ZK_PORT);
            serverA.start();
            serverB = new PersistanceServer("node1", ZK_PORT);
            serverB.start();

        }catch (Exception e){
            e.printStackTrace();
        }
        try{

            for (int i = 0; i < 10; i++) {

                System.out.println(_client.getLeader());

            }

            if (serverA.isLeader()) {
                serverA.shutdown();
            } else {
                serverB.shutdown();
            }

            for (int i = 0; i < 10; i++) {

                System.out.println(_client.getLeader());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        serverA.shutdown();
        serverB.shutdown();
        System.out.println("Finish");


    }
}
