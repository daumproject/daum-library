package org.daum.library.demos;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 24/09/12
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
import java.util.concurrent.*;

public class EventJava2 {
    static ExecutorService queue = Executors.newSingleThreadExecutor();
    static volatile boolean running = true;
    static volatile Event sentinel = new Event(0);

    static class Event {
        final int index;

        public Event(int index) {
            this.index = index;
        }
    }

    static class Consumer implements Runnable {

        private Event evt = null;

        public Consumer(Event e){
            evt = e;
        }

        @Override
        public void run() {
             count++;
        }
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            while (running) {
                queue.submit(new Consumer(new Event(1)));
            }
        }
    }


    static long count = 0;
    static long start = System.currentTimeMillis();

    static void test() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();

        count = 0;
        start = System.currentTimeMillis();

        //pool.submit(new Consumer());
        pool.execute(new Producer());
        pool.execute(new Producer());
        Thread.sleep(10000);
        running = false;

        pool.shutdown();
        pool.awaitTermination(10000000, TimeUnit.DAYS);
        queue.shutdown();
        queue.awaitTermination(10000000, TimeUnit.DAYS);
        long end = System.currentTimeMillis();
        System.out.println("Java AVG:" + count * 1000.0
                / (end - start));
    }

    public static void main(String[] args) throws InterruptedException {
        test();
    }

}