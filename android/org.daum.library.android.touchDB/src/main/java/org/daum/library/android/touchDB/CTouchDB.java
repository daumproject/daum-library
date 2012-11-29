package org.daum.library.android.touchDB;

import android.content.Context;
import android.util.Log;
import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.listener.TDListener;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

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
@ComponentType
public class CTouchDB extends AbstractComponentType
{
    private TDListener listener;
    private static final String TAG ="CTouchDB";
    private  TDServer server = null;
    private NativeLibraryLoader nativeLibraryLoader= null;
    private Integer port=8888;
    @Start
    public void start()
    {
        try
        {
            nativeLibraryLoader = new NativeLibraryLoader();
            nativeLibraryLoader.load();
            Context ctx =     UIServiceHandler.getUIService().getRootActivity();
            String filesDir = ctx.getFilesDir().getAbsolutePath();
            server = new TDServer(filesDir);
            port = Integer.parseInt(getDictionary().get("port").toString());
            listener = new TDListener(server, port);
            listener.start();
        } catch (Exception e) {
            Log.e(TAG, "Unable to create CTouchDB", e);
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
            Log.e(TAG, "Unable to stop CTouchDB", e);
        }
    }


    @Update
    public void update()
    {
      int port_tmp =  port = Integer.parseInt(getDictionary().get("port").toString());
        if(port != port_tmp)
        {
            Log.d(TAG, "Port change need update CTouchDB");
            stop();
            start();
        }
    }


}
