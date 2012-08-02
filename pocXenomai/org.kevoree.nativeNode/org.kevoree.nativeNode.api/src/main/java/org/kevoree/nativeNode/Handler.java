package org.kevoree.nativeNode;

import java.io.IOException;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 01/08/12
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
public class Handler {

    private NativeHandler nativeHandler;
    private String path;
    private  int key;
    private TreeMap<String,Integer> ports = new TreeMap<String, Integer>();

    public Handler(final int key,final String path)
    {
        this.key = key;
        this.path =path;
        nativeHandler = new NativeHandler();
        nativeHandler.configureCL();
    }

    public  void start()  {
        nativeHandler.start(key,path);
    }

    public void stop(){
        nativeHandler.stop(key);
    }


    public void update(){
        nativeHandler.update(key);
    }

    public void create_queue(String name){
       int id= nativeHandler.create_queue(key,name);
        if(id < 0){ System.out.println("ERROR"); }
        ports.put(name,id);
    }
    public void enqueue(String port,String msg)
    {
        nativeHandler.enqueue(key,ports.get(port),msg);
    }
}


