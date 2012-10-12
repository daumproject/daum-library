  /*
  *
  *
  *
  *                          THIS IS A GENERATED FILE, DO NOT EDIT
  *
  *
  *
  *
  *
  *
  **/
 package org.kevoree.nativeN.HelloWorld;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.script.KevScriptEngineException;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.KevoreeXmiHelper;
import org.kevoree.framework.MessagePort;
import org.kevoree.nativeN.core.NativeEventPort;
import org.kevoree.nativeN.core.NativeListenerPorts;
import org.kevoree.nativeN.core.NativeManager;
import org.kevoree.tools.aether.framework.AetherUtil;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * this is a generated file, DO NOT EDIT
 * User: jed
 * Date: 08/10/12
 * Time: 08:10
 */
@DictionaryType({
        @DictionaryAttribute(name = "ipc_key", optional = false),
        @DictionaryAttribute(name = "portEvents_tcp", optional = false),
        @DictionaryAttribute(name = "portEvents_udp", optional = false)
})
@ComponentType
@Provides({ @ProvidedPort(name = "input_port", type = PortType.MESSAGE,theadStrategy = ThreadStrategy.NONE)
})
@Requires({ @RequiredPort(name = "output_port", type = PortType.MESSAGE,optional = true,theadStrategy = ThreadStrategy.NONE)
})
@Library(name = "Native")
public class HelloWorld extends AbstractComponentType {

    private  int ipc_key = 251102;
    private int portEvents_tcp = 9865;
    private NativeManager nativeManager = null;
    private boolean started = false;


    @Start
    public void start () {
        try
        {

            ipc_key  = Integer.parseInt(getDictionary().get("ipc_key").toString());
            portEvents_tcp =       Integer.parseInt(getDictionary().get("portEvents_tcp").toString());

            ArrayList<String> repos = new ArrayList<String>();
            repos.add("http://maven.kevoree.org/release/");
            repos.add("http://maven.kevoree.org/snapshots/");

            /*
                 for(Repository repo :  getModelService().getLastModel().getRepositoriesForJ())
                 {
                        repos.add(repo.getUrl());
                 }
            */

           // loading model from jar
           ContainerRoot model = KevoreeXmiHelper.loadStream(getClass().getResourceAsStream("/KEV-INF/lib.kev"));


           // todo
           File binary =   AetherUtil.resolveMavenArtifact4J("org.kevoree.nativeN.HelloWorld_native"+getOs(), "org.kevoree.nativeN", "2.1-SNAPSHOT", "uexe", repos);

           if(!binary.canExecute())
           {
                binary.setExecutable(true);
           }

           nativeManager = new NativeManager(ipc_key,portEvents_tcp,"HelloWorld",binary.getPath(),model);

           nativeManager.addEventListener(new NativeListenerPorts() {

                @Override
                public void disptach(NativeEventPort event, String port_name, String msg)
                {
                    MessagePort port = (MessagePort) getPortByName(port_name);
                    port.process(msg);
                }
            });

            started = nativeManager.start();


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Stop
    public void stop () {
        if(started)
        {
            try
            {
                if(nativeManager !=null)
                    nativeManager.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }

    @Update
    public void update () {
        ipc_key  = Integer.parseInt(getDictionary().get("ipc_key").toString());
        portEvents_tcp =       Integer.parseInt(getDictionary().get("portEvents_tcp").toString());
        nativeManager.update();
    }


        public boolean is64() {
            String os = System.getProperty("os.arch").toLowerCase();
            return (os.contains("64"));
        }

        public  String getOs() {
            if (System.getProperty("os.name").toLowerCase().contains("nux")) {
                if (is64()) {
                    return "-nix64";
                } else {
                    return "-nix32";
                }
            }
            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                return "-osx";
            }
            return null;
        }

        @Port(name = "input_port")
    public void input_port(Object o)
    {
        if(nativeManager != null)
        {
            nativeManager.push("input_port",o.toString());
            
        }   else 
        {
            System.err.println("Error processing message");
        }
    }

}