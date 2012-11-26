package com.couchbase.listener;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.router.TDURLStreamHandlerFactory;

import java.io.IOException;

public class TDListener implements Runnable {

    private Handler handler;
    private HandlerThread handlerThread;
    private Thread thread;
    private TDServer server;
    private TDHTTPServer httpServer;

    //static inializer to ensure that touchdb:// URLs are handled properly
    {
        TDURLStreamHandlerFactory.registerSelfIgnoreError();
    }

    public TDListener(TDServer server, int port) {
        this.server = server;
        this.httpServer = new TDHTTPServer();
        this.httpServer.setServer(server);
        this.httpServer.setListener(this);
        this.httpServer.setPort(port);
    }

    @Override
    public void run() {
        try {
            httpServer.serve();
        }
        finally {
            handlerThread.quit();
            handlerThread = null;
            handler = null;
        }
    }

    public void start() {
        handlerThread = new HandlerThread("TDListenerHandlerThread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        handler = new Handler(looper);
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        try {
            httpServer.notifyStop();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void onServerThread(Runnable r) {
        handler.post(r);
    }

    public String getStatus() {
        String status = this.httpServer.getServerInfo();
        return status;
    }

}
