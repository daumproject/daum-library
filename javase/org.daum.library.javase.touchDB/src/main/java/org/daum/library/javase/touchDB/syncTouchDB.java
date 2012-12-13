package org.daum.library.javase.touchDB;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractChannelFragment;
import org.kevoree.framework.ChannelFragmentSender;
import org.kevoree.framework.KevoreePlatformHelper;
import org.kevoree.framework.KevoreePropertyHelper;
import org.kevoree.framework.message.Message;
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 30/11/12
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "Android")
@DictionaryType({
        @DictionaryAttribute(name = "dbname", defaultValue = "jed", optional = false, fragmentDependant = false),
        @DictionaryAttribute(name = "port", defaultValue = "8888", optional = false, fragmentDependant = true),
        @DictionaryAttribute(name = "refresh", defaultValue = "9000", optional = false, fragmentDependant = false),
        @DictionaryAttribute(name = "login", defaultValue = "", optional = false, fragmentDependant = false),
        @DictionaryAttribute(name = "password", defaultValue = "", optional = false, fragmentDependant = false),
        @DictionaryAttribute(name = "protocol", defaultValue = "", optional = false, fragmentDependant = false,vals={"http","https"})
})
@org.kevoree.annotation.ChannelTypeFragment(theadStrategy = ThreadStrategy.SHARED_THREAD )
public class syncTouchDB extends AbstractChannelFragment implements Runnable
{
    private Thread t_current;
    private Logger logger  = LoggerFactory.getLogger(getClass());

    private boolean alive = false;
    @Start
    public void start()
    {
        alive=true;
        t_current = new Thread(this);
        try
        {
            // wait local server start
            Thread.sleep(5000);
        } catch (InterruptedException ignore)
        {
        }
        t_current.start();
    }
    @Stop
    public void stop()
    {
        alive = false;
        if(t_current !=null){
            t_current.interrupt();
        }
    }


    @Update
    public void updated()
    {

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

    public int getPortChannel (String nodeName) throws IOException {
        try {
            //logger.debug("look for port on " + nodeName);
            Option<Integer> portOption = KevoreePropertyHelper.getIntPropertyForChannel(getModelService().getLastModel(), this.getName(), "port", true, nodeName);
            if (portOption.isDefined()) {
                return portOption.get();
            } else {
                return 8888;
            }
        } catch (NumberFormatException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public void run() {


        while (Thread.currentThread().isAlive() && alive==true)
        {

            String db = getDictionary().get("dbname").toString();
            CouchDbClient dblocal = null;
            try
            {
                dblocal = new CouchDbClient(db,true,getDictionary().get("protocol").toString(),getAddress(getNodeName()),getPortChannel(getNodeName()),getDictionary().get("login").toString(),getDictionary().get("password").toString());

                // query all component bind


                dblocal.replicator().addreplicatorTouchDB(null, true);


                Thread.sleep(Integer.parseInt(getDictionary().get("refresh").toString()));
            } catch (Exception ignore) {

            }
        }
    }
}
