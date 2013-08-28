package org.daum.library.javase.jtouchDB;

import org.apache.http.HttpResponse;
import org.kevoree.Channel;
import org.kevoree.ComponentInstance;
import org.kevoree.ContainerNode;
import org.kevoree.MBinding;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractChannelFragment;
import org.kevoree.framework.ChannelFragmentSender;
import org.kevoree.framework.KevoreePropertyHelper;
import org.kevoree.framework.message.Message;
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 30/11/12
 * Time: 15:04
 */
@Library(name = "JavaSE", names = {"Android"})
@DictionaryType({
        @DictionaryAttribute(name = "dbname", defaultValue = "jed", optional = false, fragmentDependant = false),
        @DictionaryAttribute(name = "login", defaultValue = "", optional = false, fragmentDependant = false),
        @DictionaryAttribute(name = "password", defaultValue = "", optional = false, fragmentDependant = false),
        @DictionaryAttribute(name = "protocol", defaultValue = "http", optional = false, fragmentDependant = false, vals = {"http", "https"})
})
@ChannelType(theadStrategy = ThreadStrategy.SHARED_THREAD)
public class syncTouchDB extends AbstractChannelFragment {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private Runnable requestSync;

    private ArrayList<TouchDBInstance> list_request_sync = new ArrayList<TouchDBInstance>();
    private ArrayList<TouchDBInstance> list_sync = new ArrayList<TouchDBInstance>();

    public enum ACTION {
        ADD,
        REMOVE
    }

    public void requestSync(ACTION evt, ArrayList<TouchDBInstance> list) {
        int count = 0;
        CouchDbClient local = null;
        CouchDbClient remote = null;
        String dbname = getDictionary().get("dbname").toString();
        String protocol = getDictionary().get("protocol").toString();
        String login = getDictionary().get("login").toString();
        String password = getDictionary().get("password").toString();


        ArrayList<TouchDBInstance> sources = new ArrayList<TouchDBInstance>();
        ArrayList<TouchDBInstance> destination = new ArrayList<TouchDBInstance>();
        sources.addAll(list);
        destination.addAll(list);

        count = list.size();
        do {
            for (TouchDBInstance src : sources) {
                try {
                    if (src.adr.length() == 0) {
                        logger.warn("You have define an address " + getNodeName());
                    }
                    local = new CouchDbClient(dbname, true, protocol, src.adr, src.port, login, password);

                    for (TouchDBInstance dest : destination) {
                        if (src != dest) {

                            if (dest.adr.length() == 0) {
                                logger.warn("You have define an address " + getNodeName());
                            }

                            remote = new CouchDbClient(dbname, true, protocol, dest.adr, dest.port, login, password);

                            HttpResponse response = null;

                            if (evt == ACTION.ADD) {
                                logger.info("Sync from " + local.getDBUri() + " to " + remote.getDBUri());
                                response = local.replicator().addreplicatorTouchDB(remote, true);

                            } else {
                                logger.info("Remove from " + local.getDBUri() + " to " + remote.getDBUri());
                                response = local.replicator().cancelreplicatorTouchDB(remote);
                            }
                        }
                    }

                    list_sync.add(src);
                    count--;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            try {
                Thread.sleep(9500);
            } catch (InterruptedException e) {
            }
        } while (count != 0);


    }

    @Start
    public void start() {
        requestSync = new Runnable() {
            @Override
            public void run() {
                requestSync(ACTION.ADD, getTouchDBInstances());
            }
        };

        service.schedule(requestSync, 1, TimeUnit.SECONDS);

    }

    @Stop
    public void stop() {
        service.shutdownNow();
    }


    @Update
    public void updated() {
        requestSync(ACTION.REMOVE, list_sync);
        list_sync.clear();
        service.schedule(requestSync, 1, TimeUnit.SECONDS);
    }

    public ArrayList<TouchDBInstance> getTouchDBInstances() {
        ArrayList<TouchDBInstance> current = new ArrayList<TouchDBInstance>();

        for (MBinding binding : getModelElement().getBindings()) {

            ContainerNode node = (ContainerNode) binding.getPort().eContainer().eContainer();
            ComponentInstance instance = (ComponentInstance) binding.getPort().eContainer();

            Integer port = Integer.parseInt(  KevoreePropertyHelper.instance$.getProperty(instance, "port_db", true, null));

            TouchDBInstance in = new TouchDBInstance();
            in.adr = getAddress(node.getName());
            in.port = port;

            if (in.adr.length() == 0) {
                logger.error("The " + node.getName() + "  has no IP address, it will be ignored.");
            }   else {
                current.add(in);
            }

        }
        return current;
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
        return KevoreePropertyHelper.instance$.getNetworkProperties(getModelService().getLastModel(), remoteNodeName, org.kevoree.framework.Constants.instance$.getKEVOREE_PLATFORM_REMOTE_NODE_IP()).get(0);
    }


    public int parsePortNumber (String nodeName) {
        Channel channelOption = getModelService().getLastModel().findByPath("hubs[" + getName() + "]", Channel.class);
        int port = 8000;
        if (channelOption!=null) {
            String portOption = KevoreePropertyHelper.instance$.getProperty(channelOption, "port", true, nodeName);
            if (portOption != null) {
                try {
                    port = Integer.parseInt(portOption);
                } catch (NumberFormatException e) {
                    logger.warn("Attribute \"port\" of {} is not an Integer, default value ({}) is used.", getName(), port);
                }
            }
        } else {
            logger.warn("There is no channel named {}, default value ({}) is used.", getName(), port);
        }
        return port;
    }



    private class TouchDBInstance {
        private String adr;
        private Integer port;

        public String toString() {
            return adr + "  " + port;
        }
    }

}
