package org.daum.library.web;

import org.webbitserver.WebServers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/06/12
 * Time: 11:00
 * To change this template use File | Settings | File Templates.
 */
public class Tester {
    private HashMap<String,byte[]> cache = new HashMap<String, byte[]>();
    public byte[] load(String url)
    {
        if(cache.containsKey(url))
        {
            return cache.get(url);
        } else {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try
            {
                InputStream res =    getClass().getClassLoader().getResourceAsStream(url);
                if(res != null){
                    int nRead;
                    byte[] data = new byte[16384];
                    while ((nRead = res.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                    cache.put(url,buffer.toByteArray());
                } else
                {
                    return "404".getBytes();
                }

            } catch (IOException e) {

                return "404".getBytes();
            }
            return buffer.toByteArray();
        }

    }

    public static void  main(String argv[]){

        final WebSocketChannel    webSocketChannel = new WebSocketChannel();
        WebServers.createWebServer(8082)
                .add("/jed", webSocketChannel).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
             while(true){
                 webSocketChannel.broadcast("update");
                 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                 }
             }
            }
        })      .start();
    }
}
