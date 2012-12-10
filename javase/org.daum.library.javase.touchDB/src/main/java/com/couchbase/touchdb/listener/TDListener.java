package com.couchbase.touchdb.listener;



import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.router.TDURLStreamHandlerFactory;

public class TDListener implements Runnable {

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

        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        if(httpServer !=null)
            httpServer.notifyStop();
        if(thread !=null)
            thread.interrupt();


    }


    public String getStatus() {
        String status = this.httpServer.getServerInfo();
        return status;
    }

}
