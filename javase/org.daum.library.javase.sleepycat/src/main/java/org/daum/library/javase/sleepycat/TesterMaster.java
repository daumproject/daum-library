package org.daum.library.javase.sleepycat;

import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.rep.ReplicatedEnvironment;
import com.sleepycat.je.rep.ReplicationConfig;
import com.sleepycat.je.rep.StateChangeEvent;
import com.sleepycat.je.rep.StateChangeListener;
import com.sleepycat.je.rep.util.DbEnableReplication;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/12/12
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public class TesterMaster {


    public static void  main(String argv[]) throws IOException, InterruptedException {

        File envHome = new File("/tmp/master/");
        envHome.mkdirs();

        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        envConfig.setTransactional(true);

        // Identify the node
        ReplicationConfig repConfig = new ReplicationConfig();
        repConfig.setGroupName("PlanetaryRepGroup");
        repConfig.setNodeName("Mercury");
        repConfig.setNodeHostPort("0.0.0.0:5001");

        // This is the first node, so its helper is itself
        repConfig.setHelperHosts("0.0.0.0:5001");

        ReplicatedEnvironment repEnv =     new ReplicatedEnvironment(envHome, repConfig, envConfig);




        System.out.println("getGroup "+repEnv.getGroup());
        System.out.println("getNodeName "+repEnv.getNodeName());

        Thread.sleep(10000000);

        repEnv.close();


    }
}
