package org.daum.library.couchDB;

import android.util.Log;
import com.couchbase.listener.TDListener;
import com.couchbase.touchdb.TDServer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/11/12
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */
public class Tester2 {



    public static void  main(String argv[])
    {

         TDListener listener=null;
         TDServer server=null;
    try
    {
        server = new TDServer("/home/jed/DAUM_PROJECT/daum-library/android/org.daum.library.android.couchDB/src/main/resources/x86");
        listener = new TDListener(server, 8888);
        listener.start();
    } catch (IOException e) {
e.printStackTrace();
    }

    }
}
