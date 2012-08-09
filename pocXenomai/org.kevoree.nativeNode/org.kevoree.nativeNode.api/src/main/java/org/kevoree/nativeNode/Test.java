package org.kevoree.nativeNode;




public class Test {

    public static void main(String[] args) throws Exception {

        int ipc_key = 25181;

        String binary =   "/home/jed/DAUM_PROJECT/daum-library/pocXenomai/org.kevoree.nativeNode/org.kevoree.native.testcomponent/target/org.kevoree.native.testcomponent.uexe";

        final Handler  poc = new Handler(ipc_key,9018,binary);

        poc.create_input("input_port");

        poc.create_output("output_port");


        /*
         System.out.println(poc.GenerateInputsPorts());
        System.out.println(poc.generatorPorts());    */

        poc.addEventListener(new NativePortsListener() {

            @Override
            public void disptach(NativePortEvent event, String port_name, String msg)
            {
                System.out.println("DISPATCH from "+port_name+" ="+msg);
            }
        });

        poc.start();

        poc.update();

               for(int i=0;i<6000;i++)
               {
                   poc.enqueue("input_port","hello world "+i);
                   Thread.sleep(5);
               }


        poc.stop();
        System.exit(0);

    }

}
