package org.daum.library.android.touchDB;


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
        @DictionaryAttribute(name = "port", defaultValue = "8888", optional = false)
})
@Requires({
        @RequiredPort(name = "cluster", type = PortType.MESSAGE,theadStrategy = ThreadStrategy.NONE, optional = true)
})
@ComponentType
public class CTouchDB extends AbstractComponentType
{
    private TDListener listener;
    private static final String TAG ="CTouchDB";
    private  TDServer server = null;
    private NativeLibraryLoader nativeLibraryLoader= null;
    private Integer port=8888;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Start
    public void start()
    {
        try
        {
            nativeLibraryLoader = new NativeLibraryLoader();
            nativeLibraryLoader.load();
       //     Context ctx =     UIServiceHandler.getUIService().getRootActivity();   ctx.getFilesDir().getAbsolutePath();
            String filesDir ="TODO";
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
            if(nativeLibraryLoader !=null){
                nativeLibraryLoader.unload();
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
            logger.debug(TAG, "Port change need update CTouchDB");
            stop();
            start();
        }
    }


}
