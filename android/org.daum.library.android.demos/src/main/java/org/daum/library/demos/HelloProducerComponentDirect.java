package org.daum.library.demos;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.kevoree.framework.message.Message;


@Library(name = "JavaSE", names = {"Android"})
@DictionaryType({
        @DictionaryAttribute(name = "helloProductionDelay", defaultValue = "2000", optional = true),
        @DictionaryAttribute(name = "adr", defaultValue = "192.168.1.121", optional = true)       ,
        @DictionaryAttribute(name = "port", defaultValue = "8000", optional = true)
})
@ComponentType
public class HelloProducerComponentDirect extends AbstractComponentType implements  Runnable{


    private Data data;
    private  Thread currentThread= null;
    @Start
    public void startComponent()
    {
        data = new Data();
        currentThread = new Thread(this);

        currentThread.start();
    }

    @Stop
    public void stopComponent() {

    }

    @Update
    public void updateComponent() {
        stopComponent();
        startComponent();
    }


    @Override
    public void run() {
        String adr =     getDictionary().get("adr").toString();
        int port = Integer.parseInt(getDictionary().get("port").toString()) ;
        Message t = new Message();
        t.setContent(data);

        while (Thread.currentThread().isAlive()){

            P2pClient pClient = new P2pClient("remotename",adr,port);
            try {
                pClient.send(t);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            try {
                Thread.sleep(Integer.parseInt(getDictionary().get("helloProductionDelay").toString()));
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }
}
