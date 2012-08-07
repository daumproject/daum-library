package org.kevoree.nativeNode;




public class Test {

    public static void main(String[] args) throws Exception {

        int ipc_key = 256181;

        String binary =   "/home/jed/DAUM_PROJECT/daum-library/pocXenomai/org.kevoree.nativeNode/org.kevoree.native.testcomponent/target/org.kevoree.native.testcomponent.uexe";

        final Handler  poc = new Handler(ipc_key,binary);

        poc.create_input("input_port");
        poc.create_output("output_port");


        poc.addEventListener(new NativePortsListener() {

            @Override
            public void disptach(NativePortEvent event, String msg)
            {
                System.out.println("NATIVE SAYS="+msg);
            }
        });

        poc.start();

        poc.update();

        Thread t = new Thread(new Runnable() {
           @Override
           public void run()
           {
               for(int i=0;i<50;i++)
               {
                   poc.enqueue("input_port","hello world hello worldhello worldhello worldhello worldhello world hello worldhello worldhello worldhello worldhello worldhello world "+i);
               }
           }
       });
        t.start();

        t.join();


        poc.stop();
        System.exit(0);

    }

}
