package org.daum.library.android.channelCouchDB;

import org.kevoree.annotation.*;
import org.kevoree.framework.*;
import org.kevoree.framework.message.Message;
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 30/11/12
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "JavaSE", names = {"Android"})
@DictionaryType({
        @DictionaryAttribute(name = "port", defaultValue = "5984", optional = false, fragmentDependant = true),
        @DictionaryAttribute(name = "dbname", defaultValue = "jed", optional = false, fragmentDependant = false),
        @DictionaryAttribute(name = "refresh", defaultValue = "9000", optional = false, fragmentDependant = false),
        @DictionaryAttribute(name = "login", defaultValue = "", optional = false, fragmentDependant = false),
        @DictionaryAttribute(name = "password", defaultValue = "", optional = false, fragmentDependant = false),
        @DictionaryAttribute(name = "protocol", defaultValue = "", optional = false, fragmentDependant = false,vals={"http","https"})
})
@org.kevoree.annotation.ChannelType(theadStrategy = ThreadStrategy.SHARED_THREAD )
public class ChannelCouchDB extends AbstractChannelFragment implements Runnable
{
    private Thread t_current;
    private ArrayList<String> mastersCOUCHDB = new ArrayList<String>();
    private Logger logger  = LoggerFactory.getLogger(getClass());

    @Start
    public void start()
    {
        update_remote_couchDB();
        t_current = new Thread(this);
        t_current.start();
    }
    @Stop
    public void stop()
    {
        if(t_current !=null){
            t_current.interrupt();
        }
    }

    public  void update_remote_couchDB(){
        synchronized (mastersCOUCHDB) {
            mastersCOUCHDB.clear();
            for (KevoreeChannelFragment cf : getOtherFragments())
            {
                mastersCOUCHDB.add(cf.getNodeName());
            }
        }
    }
    @Update
    public void updated()
    {
        update_remote_couchDB();
    }
    @Override
    public Object dispatch(Message message) {
        return null;
    }

    @Override
    public ChannelFragmentSender createSender(String s, String s1) {
        return null;
    }

    public String getAddress(String remoteNodeName)
    {
        String ip = KevoreePlatformHelper.getProperty(getModelService().getLastModel(), remoteNodeName,
                org.kevoree.framework.Constants.KEVOREE_PLATFORM_REMOTE_NODE_IP());
        if (ip == null || ip.equals("")) {
            ip = "";
        }
        return ip;
    }



    public int parsePortNumber(String nodeName) throws IOException {
        try {
            //logger.debug("look for port on " + nodeName);
            return KevoreeFragmentPropertyHelper
                    .getIntPropertyFromFragmentChannel(this.getModelService().getLastModel(), this.getName(), "port",
                            nodeName);
        } catch (NumberFormatException e) {
            throw new IOException(e.getMessage());
        }
    }


    @Override
    public void run() {
        try
        {
            // wait that the couchdb server is started to  avoid exception
            Thread.sleep(2000);
        } catch (InterruptedException ignore) {

        }
        ArrayList<String> cluster =  new ArrayList<String>();

        while (Thread.currentThread().isAlive())
        {
            cluster.clear();
            synchronized (mastersCOUCHDB)
            {
                cluster.addAll(mastersCOUCHDB);
            }
            for (String remoteCouchDBNodeName : cluster)
            {
                try
                {
                    String db = getDictionary().get("dbname").toString();
                    CouchDbClient couchDB_local = new CouchDbClient(db,true,"http",getAddress(getNodeName()),parsePortNumber(getNodeName()),"","");
                    CouchDbClient couchDB_remote = new CouchDbClient(db,true,"http",getAddress(remoteCouchDBNodeName),parsePortNumber(remoteCouchDBNodeName),"","");
                    // replicate
                    couchDB_local.replicator().addReplicatorCouchDB(couchDB_remote,false);

                } catch (Exception e)
                {
                    logger.debug("CouchDB instance on "+remoteCouchDBNodeName+" is not available");
                }
            }
            try
            {
                Thread.sleep(Integer.parseInt(getDictionary().get("refresh").toString()));
            } catch (InterruptedException ignore) {

            }
        }
    }
}
