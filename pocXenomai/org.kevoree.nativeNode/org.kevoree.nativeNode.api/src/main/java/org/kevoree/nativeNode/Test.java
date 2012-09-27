package org.kevoree.nativeNode;




public class Test {

    public static void main(String[] args) throws Exception {

        Generator gen = new Generator();


        gen.setPathfile("ports");

        gen.create_input("input_port");
        gen.create_output("output_port");

        //phase compile
        System.out.println(gen.generateStepPreCompile());


         /*
        int ipc_key = 25181;
        int event_port = 9500;
        String binary =   "/home/jed/DAUM_PROJECT/daum-library/pocXenomai/org.kevoree.nativeNode/org.kevoree.native.testcomponent/target/org.kevoree.native.testcomponent.uexe";


        final Handler  poc = new Handler(ipc_key,event_port,binary);

        poc.setInputs_ports(gen.getInputs_ports());
        poc.setOuputs_ports(gen.getOuputs_ports());



        poc.addEventListener(new NativePortsListener() {

            @Override
            public void disptach(NativePortEvent event, String port_name, String msg)
            {
                System.out.println("DISPATCH from "+port_name+" ="+msg);
            }
        });

        poc.start();

        poc.update();

        for(int i=0;i<5;i++)
        {
            poc.enqueue("input_port","hello world "+i);
            Thread.sleep(5);
        }


        poc.stop();

        */
        System.exit(0);

    }

}
