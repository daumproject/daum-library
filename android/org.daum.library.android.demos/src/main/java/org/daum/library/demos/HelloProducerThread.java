package org.daum.library.demos;

import java.util.ArrayList;


public class HelloProducerThread extends Thread {

    private boolean stopped = false;
    private long delay = 2000;
    private ArrayList<HelloProductionListener> listeners = new ArrayList<HelloProductionListener>();


    public HelloProducerThread() {}

    public HelloProducerThread(long delay) {
        this.delay = delay;
    }

    public void addHelloProductionListener(HelloProductionListener lst) {
        listeners.add(lst);
    }

    public void halt() {
        stopped = true;
    }

    public boolean isStopped(){return stopped;}

    public void run() {
        while(!stopped) {
            produceHello();

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void produceHello() {
        for(final HelloProductionListener listener : listeners) {
            new Thread(new Runnable(){
                public void run() {listener.helloProduced("Hello time ");}
            }).start();
        }
    }

}
