package org.kevoree.nativeN;

import javax.swing.event.EventListenerList;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 01/08/12
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
public class Handler {

    protected EventListenerList listenerList = new EventListenerList();
    private NativeJNI nativeHandler;
    private String path;
    private  int key;
    private LinkedHashMap<String,Integer> inputs_ports =null;
    private LinkedHashMap<String,Integer> ouputs_ports = null;

    public Handler(final int key,int port,final String path)
    {
        this.key = key;
        this.path =path;
        nativeHandler = new NativeJNI(this);
        nativeHandler.configureCL();
        nativeHandler.init(key,port);
        nativeHandler.register();
    }

    public  void start() throws Exception {

        for(String name: inputs_ports.keySet()){
            nativeHandler.create_input(key,name);
        }

        for(String name: ouputs_ports.keySet()){
            nativeHandler.create_output(key,name);
        }

        if(nativeHandler.start(key,path) != 0){

        }
        //todo check started  remove sleep
        Thread.sleep(2000);
    }

    public void stop() throws InterruptedException
    {
        Thread.sleep(2000);
        nativeHandler.stop(key);
    }


    public void addEventListener (NativePortsListener listener) {
        listenerList.add(NativePortsListener.class, listener);
    }

    public void removeEventListener (NativePortsListener listener) {
        listenerList.remove(NativePortsListener.class, listener);
    }


    public void fireEvent(NativePortEvent evt,String queue,String msg)
    {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2)
        {
            if (evt instanceof NativePortEvent)
            {
                ((NativePortsListener) listeners[i + 1]).disptach(evt,queue,msg);
            }
        }
    }
    public void update(){
        nativeHandler.update(key);
    }

    public void setInputs_ports(LinkedHashMap<String, Integer> inputs_ports) {
        this.inputs_ports = inputs_ports;
    }

    public void setOuputs_ports(LinkedHashMap<String, Integer> ouputs_ports) {
        this.ouputs_ports = ouputs_ports;
    }



    public void enqueue(String port,String msg)
    {
        if(nativeHandler.enqueue(key,inputs_ports.get(port),msg) != 0){
            //error
            System.out.println("error");
        }
    }
}


