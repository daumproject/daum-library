package org.kevoree.nativeNode;

import javax.swing.event.EventListenerList;
import java.util.TreeMap;

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
    private TreeMap<String,Integer> ports = new TreeMap<String, Integer>();

    public Handler(final int key,final String path)
    {
        this.key = key;
        this.path =path;
        nativeHandler = new NativeJNI(this);
        nativeHandler.configureCL();
        nativeHandler.init(key);
        nativeHandler.register();
    }


    public  void start() throws Exception {
        if(nativeHandler.start(key,path) != 0){

        }
        //todo check started  remove sleep
        Thread.sleep(2000);
    }

    public void stop() throws InterruptedException
    {
        Thread.sleep(3500);
        nativeHandler.stop(key);
    }


    public void addEventListener (NativePortsListener listener) {
        listenerList.add(NativePortsListener.class, listener);
    }

    public void removeEventListener (NativePortsListener listener) {
        listenerList.remove(NativePortsListener.class, listener);
    }


    public void fireEvent(NativePortEvent evt,String msg)
    {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2)
        {
            if (evt instanceof NativePortEvent)
            {
                ((NativePortsListener) listeners[i + 1]).disptach(evt,msg);
            }
        }
    }
    public void update(){
        nativeHandler.update(key);
    }

    public void create_input(String name)
    {
        int id= nativeHandler.create_input(key,name);
        if(id < 0)
        {
            System.out.println("ERROR");
        }
        ports.put(name,id);
    }

    public void create_output(String name){
        int id= nativeHandler.create_output(key, name);
        if(id < 0)
        {
            System.out.println("ERROR");
        }
        ports.put(name,id);
    }
    public void enqueue(String port,String msg)
    {
        if(nativeHandler.enqueue(key,ports.get(port),msg) != 0){
            //error
            System.out.println("error");
        }
    }
}


