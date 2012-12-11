package org.daum.library.javase.touchDB;


import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.listener.TDListener;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 23/11/12
 * Time: 15:31
 */
@Library(name = "JavaSE", names = {"Android"})
@DictionaryType({
        @DictionaryAttribute(name = "port", defaultValue = "8888", optional = false),
        @DictionaryAttribute(name = "path", defaultValue = "/tmp/", optional = false),
})
@Requires({
        @RequiredPort(name = "cluster", type = PortType.MESSAGE,theadStrategy = ThreadStrategy.NONE, optional = true)
})
@ComponentType
public class JTouchDB extends AbstractComponentType
{
    private TDListener listener;
    private static final String TAG ="JTouchDB";
    private  TDServer server = null;
    private Integer port=8888;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Start
    public void start()
    {
        try
        {
            String filesDir =getDictionary().get("path").toString();
            server = new TDServer(filesDir);
            port = Integer.parseInt(getDictionary().get("port").toString());
            listener = new TDListener(server, port);
            listener.start();
        } catch (Exception e) {
            logger.error(TAG, "Unable to create CTouchDB", e);
        }
    }


    @Stop
    public void stop()
    {
        try
        {
            if(server != null){
                server.close();
            }
            if(listener !=null){
                listener.stop();
            }
        } catch (Exception e) {
            logger.error(TAG, "Unable to stop CTouchDB", e);
        }
    }


    @Update
    public void update()
    {
      int port_tmp =  port = Integer.parseInt(getDictionary().get("port").toString());
        if(port != port_tmp)
        {
            logger.debug(TAG, "Port change need update JTouchDB");
            stop();
            start();
        }
    }


}
