package org.daum.library.javase.repositoryManager;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.wagon.repository.Repository;
import org.daum.library.javase.repositoryManager.jetty.DownloadRequestHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 25/09/12
 * Time: 14:07
 * To change this template use File | Settings | File Templates.
 */
public class Tester {


    public static void  main(String argv[]) throws Exception {


        Server server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.addConnector(connector);

        HttpProxyMojo httpProxyMojo = new HttpProxyMojo();


        DownloadRequestHandler requestHandler = new DownloadRequestHandler(httpProxyMojo);



        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { requestHandler, new DefaultHandler() });
        server.setHandler(handlers);

        server.start();
        server.join();





    }
}
