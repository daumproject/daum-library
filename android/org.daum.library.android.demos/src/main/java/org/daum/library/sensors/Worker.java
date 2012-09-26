package org.daum.library.sensors;

import org.kevoree.framework.message.Message;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 24/09/12
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class Worker implements  Runnable {

    private  Thread thread=null;
    private int counter=0;
    private double start;
    private ConcurrentLinkedQueue<Message> queue;
    public  Worker(ConcurrentLinkedQueue<Message> queue){
        thread = new Thread(this);
        thread.start();
        this.queue = queue;
        start = System.currentTimeMillis();
    }
    @Override
    public void run() {
        while(Thread.currentThread().isAlive()){

            if(queue.poll() != null){
                counter++;
            }

            double duree = (System.currentTimeMillis() - start)  / 1000;
            if(duree > 1)
            {
                System.out.println(counter+";"+duree);
                counter = 0;
                start = System.currentTimeMillis();
            }



        }
    }
}
