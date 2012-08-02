package org.kevoree.nativeNode;




public class Test {

    public static void main(String[] args) throws Exception {

        int ipc_key = 994962;
        String binary =   "/home/jed/pocXenomai/org.kevoree.nativeNode/org.kevoree.native.testcomponent/target/org.kevoree.native.testcomponent.uexe";

        Handler  poc = new Handler(ipc_key,binary);
        poc.create_queue("input_port");
        Thread.sleep(1000);
        poc.start();

        Thread.sleep(2000);
        poc.enqueue("input_port","hello world");

        poc.stop();
        System.exit(0);

    }

}
