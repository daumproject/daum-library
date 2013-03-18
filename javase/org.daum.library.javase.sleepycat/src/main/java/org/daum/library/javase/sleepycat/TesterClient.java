package org.daum.library.javase.sleepycat;

import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.rep.ReplicatedEnvironment;
import com.sleepycat.je.rep.ReplicationConfig;
import com.sleepycat.je.rep.ReplicationNode;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import org.daum.library.javase.sleepycat.pojo.Employee;
import scala.testing.SUnit;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 04/03/13
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public class TesterClient {


    public static void  main(String argv[]) throws Exception
    {
        File envHome = new File("/tmp/jed");
        envHome.mkdirs();

        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        envConfig.setTransactional(true);

        // Identify the node
        ReplicationConfig repConfig = new ReplicationConfig("PlanetaryRepGroup", "Jupiter", "localhost:5002");

        // Use the node at mercury.acme.com:5001 as a helper to find the rest
        // of the group.
        repConfig.setHelperHosts("localhost:5001");

        ReplicatedEnvironment repEnv =  new ReplicatedEnvironment(envHome, repConfig, envConfig);


        for(ReplicationNode n :        repEnv.getGroup().getNodes() ){

            System.out.println(n.getName());


        }

        repEnv.getGroup().

        /* Open a transactional EntityStore. */
        StoreConfig storeConfig = new StoreConfig();
        storeConfig.setAllowCreate(true);
        storeConfig.setTransactional(false);
        EntityStore store = new EntityStore(repEnv, "storetest", storeConfig);



        PrimaryIndex<Integer, Employee> employeePrimaryIndex = store.getPrimaryIndex(Integer.class, Employee.class);
        employeePrimaryIndex.putNoReturn(new Employee(12,"maude", "ostyn", null));

        Employee employee = employeePrimaryIndex.get(12);
        //SUnit.Assert.assertNotNull(employee);
        employeePrimaryIndex.delete(12);
        employee = employeePrimaryIndex.get(12);
      //  Assert.assertNull(employee);

        store.close();


        repEnv.close();



    }
}
