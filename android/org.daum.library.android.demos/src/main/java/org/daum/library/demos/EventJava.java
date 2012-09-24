package org.daum.library.demos;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 24/09/12
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class EventJava {
    static BlockingQueue<Event> queue = new LinkedBlockingQueue<EventJava.Event>();
    static volatile boolean running = true;
    static volatile Event sentinel = new Event(0);

    static class Event {
        final int index;

        public Event(int index) {
            this.index = index;
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            long count = 0;
            long start = System.currentTimeMillis();
            while (true) {
                try {
                    Event event = queue.take();
                    if (event == sentinel) {
                        long end = System.currentTimeMillis();
                        System.out.println("Java AVG:" + count * 1000.0
                                / (end - start));
                        break;
                    }
                    count++;
                } catch (InterruptedException e) {
                }
            }
        }
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            while (running) {
                queue.add(new Event(1));
            }

        }
    }

    static void test() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();

        ExecutorService pool2 = Executors.newSingleThreadExecutor();

        pool2.submit(new Consumer());
        pool.execute(new Producer());
        pool.execute(new Producer());
        Thread.sleep(10000);
        running = false;
        queue.add(sentinel);
        pool.shutdown();
        pool2.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        test();
    }

}