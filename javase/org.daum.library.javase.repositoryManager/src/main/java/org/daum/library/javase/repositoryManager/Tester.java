package org.daum.library.javase.repositoryManager;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.wagon.repository.Repository;
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

        Server  httpServer = new Server();

    SelectChannelConnector connector = new SelectChannelConnector();
    connector.setPort(8080);
    httpServer.addConnector(connector);

    ResourceHandler resource_handler = new ResourceHandler();
    resource_handler.setDirectoriesListed(true);
    resource_handler.setWelcomeFiles(new String[]{ "index.html" });

    resource_handler.setResourceBase(".");


    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
    httpServer.setHandler(handlers);

    httpServer.start();
    httpServer.join();


    }
}
