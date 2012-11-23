package org.daum.library.couchDB;

import android.content.*;
import com.couchbase.android.CouchbaseMobile;
import com.couchbase.android.ICouchbaseDelegate;
import com.couchbase.android.Intents;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 23/11/12
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class KCouchDB implements ICouchDB {

    private ServiceConnection couchServiceConnection;
    private Context ctx=null;

    public KCouchDB(Context ctx){
      this.ctx = ctx;
    }

    public void startCouchbase()
    {
        CouchbaseMobile couch = new CouchbaseMobile(ctx, mDelegate);
        couchServiceConnection = couch.startCouchbase();
        ctx.registerReceiver(mReceiver, new IntentFilter(Intents.CouchbaseStarted.ACTION));
        ctx.registerReceiver(mReceiver, new IntentFilter(Intents.CouchbaseError.ACTION));
    }




    public void stopCouchbase(){
          ctx.unbindService(couchServiceConnection);
    }


    private final ICouchbaseDelegate mDelegate = new ICouchbaseDelegate() {
        @Override
        public void couchbaseStarted(String host, int port)
        {
            System.out.println("Host "+host+" "+port);

        }

        @Override
        public void exit(String error)
        {
            System.out.println("EXIT");

        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(Intents.CouchbaseStarted.ACTION.equals(intent.getAction())) {
                String host = Intents.CouchbaseStarted.getHost(intent);
                int port = Intents.CouchbaseStarted.getPort(intent);
            }
            else if(Intents.CouchbaseError.ACTION.equals(intent.getAction()))
            {
                String message = Intents.CouchbaseError.getMessage(intent);
                System.out.println(message);
            }
        }
    };

}
