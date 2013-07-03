package org.daum.library.javase.copterManager.test;

import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 17/06/13
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class Tester {




    public static void main(String argv[]) throws InterruptedException

    {


         WebServer webServer = null;

        webServer = WebServers.createWebServer(9998);
        webServer.add("/echo",new WebSocketChannel());
        webServer.start();

        Thread.sleep(1000000000);

    }
}
