package org.kevoree.nativeN;


import org.kevoree.ContainerRoot;
import org.kevoree.nativeN.core.NativeEventPort;
import org.kevoree.nativeN.core.NativeListenerPorts;
import org.kevoree.nativeN.core.NativeManager;
import org.kevoree.nativeN.utils.KevScriptLoader;

public class Test {





    public static void main(String[] args) throws Exception {

        int ipc_key = 25182;
        int event_port = 8500;

        String path_to_component = "/home/jed/DAUM_PROJECT/daum-library/pocXenomai/org.kevoree.nativeN/org.kevoree.nativeN.testcomponent";
        String binary =   path_to_component+"/target/org.kevoree.nativeN.testcomponent.uexe";
        String kevScript = path_to_component+"/src/main/c/HelloWorld.kevs";

        NativeManager poc = new NativeManager(ipc_key,event_port,binary,kevScript);


        poc.addEventListener(new NativeListenerPorts() {

            @Override
            public void disptach(NativeEventPort event, String port_name, String msg)
            {
                System.out.println("DISPATCH from "+port_name+" ="+msg);
            }
        });

        poc.start();



        for(int i=0;i<100;i++)
        {
            poc.enqueue("input_port","hello world "+i);

            Thread.sleep(5);
        }


        poc.stop();



        System.exit(0);
    }


}
