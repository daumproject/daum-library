package org.daum.library.demos;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 08/08/12
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */

import org.kevoree.framework.message.Message;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class P2pServer  implements Runnable{

    protected int          serverPort   = 8000;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected ExecutorService threadPool = Executors.newFixedThreadPool(50);

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private Worker worker;
    private ConcurrentLinkedQueue<Message> linkedBlockingQueue = new ConcurrentLinkedQueue< Message >();

    public P2pServer(int port){
        this.serverPort = port;
        worker  = new Worker(linkedBlockingQueue);
    }

    public void run(){
        synchronized(this)
        {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped())
                {
                    logger.debug("Server Stopped ",e);
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }

            try
            {

                InputStream input  = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();
                ObjectInputStream obj = new ObjectInputStream(input);
                Message  msg = (Message)obj.readObject();
                linkedBlockingQueue.add(msg);

                output.close();
                input.close();
            } catch (Exception e) {

            }

        }
        this.threadPool.shutdown();
        logger.debug("Server Stopped ");
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop()
    {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket()
    {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);

        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8000", e);
        }
    }
}