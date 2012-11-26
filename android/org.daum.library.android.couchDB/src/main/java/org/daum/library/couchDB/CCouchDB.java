package org.daum.library.couchDB;

import android.content.Context;
import android.util.Log;
import com.couchbase.listener.TDListener;
import com.couchbase.touchdb.TDServer;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 23/11/12
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "JavaSE", names = {"Android"})
@DictionaryType({
        @DictionaryAttribute(name = "port", defaultValue = "8888", optional = false)
})
@ComponentType
public class CCouchDB extends AbstractComponentType {
    private static final String TAG ="CCouchDB" ;
    private TDListener listener=null;
    private TDServer server=null;
    private  int port;
    @Start
    public void start()
    {
        Context ctx =     UIServiceHandler.getUIService().getRootActivity();
        String filesDir = ctx.getFilesDir().getAbsolutePath();
        try
        {
            server = new TDServer(filesDir);
             port = Integer.parseInt(getDictionary().get("port").toString());
            listener = new TDListener(server, port);
            listener.start();
        } catch (IOException e) {
            Log.e(TAG, "Unable to create TDServer", e);
        }
    }

    @Stop
    public void stop()
    {
        if(listener!=null){
            listener.stop();
        }
       if(server !=null){
           server.close();
       }
    }


    @Update
    public void update()
    {
        int port_tmp = Integer.parseInt(getDictionary().get("port").toString());
        if(port != port_tmp){
            stop();
            start();
        }
    }


}
