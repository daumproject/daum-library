package org.daum.library.p2pSock;

import org.kevoree.Channel;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.framework.AbstractChannelFragment;
import org.kevoree.framework.ChannelFragmentSender;
import org.kevoree.framework.KevoreeChannelFragment;
import org.kevoree.framework.KevoreePropertyHelper;
import org.kevoree.framework.message.Message;
import org.kevoree.log.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 08/08/12
 * Time: 11:42
 */

@Library(name = "JavaSE", names = {"Android"})
@ChannelType(theadStrategy = ThreadStrategy.SHARED_THREAD )
@DictionaryType({
        @DictionaryAttribute(name = "port", defaultValue = "", optional = true, fragmentDependant = true) ,
        @DictionaryAttribute(name = "size_queue", defaultValue = "50", optional = false),
        @DictionaryAttribute(name = "timer", defaultValue = "2000", optional = false),
        @DictionaryAttribute(name = "replay", defaultValue = "true", optional = false, vals = {"true", "false"})
})
public class P2pSock extends AbstractChannelFragment implements ModelListener{

    private P2pServer server;

    private Thread t_server;
    private Semaphore sender = new Semaphore(1);
    private  HashMap<String,P2pClient> cache_clients = new HashMap<String,P2pClient>();
    private  QueueMsg backupOnError ;
    private  int portServer;
    private ContainerRoot model;
    private class  InfoClient{
        String adr;
        int port;
    }
    private  HashMap<String,InfoClient> cache = new HashMap<String,InfoClient>();

    @Start
    public void startp()
    {
        try
        {
            model = getModelService().getLastModel();
            Integer maximum_size_messaging = Integer.parseInt(getDictionary().get("size_queue").toString());
            Integer timer = Integer.parseInt(getDictionary().get("timer").toString());
            backupOnError = new QueueMsg(this,timer,maximum_size_messaging);

            server = new P2pServer(this,parsePortNumber(getNodeName().toString()));
            t_server = new Thread(server);
            t_server.start();

        } catch (Exception e) {
            Log.error("Starting ", e);
        }

    }

    @Stop
    public void stopp(){
        if(server !=null){
            server.stop();
        }
        if(t_server != null){
            t_server.interrupt();
        }
        if(backupOnError != null){
            backupOnError.stopProcess();
        }
    }

    @Update
    public void updatep()
    {
        try
        {

            cache.clear();
            model = getModelService().getLastModel();

            if (portServer != parsePortNumber(getNodeName()))
            {
                stopp();
                Thread.sleep(2500);
                startp(); // TODO CHECK MSG IN QUEUE
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public Object dispatch (Message message) {
        for (org.kevoree.framework.KevoreePort p : getBindedPorts()) {
            forward(p, message);
        }
        for (KevoreeChannelFragment cf : getOtherFragments()) {
            if (!message.getPassedNodes().contains(cf.getNodeName())) {
                forward(cf, message);
            }
        }
        return null;
    }



    public P2pClient getOrCreateClient(String node,String host,Integer port) throws IOException {
        P2pClient client = null;

        if (cache_clients.containsKey(host+port))
        {
            //  Log.debug("the link exist");
            client = cache_clients.get(host+port);
        } else {
            //   Log.debug("no link in cache");
            client = new P2pClient(node,host, port);
            cache_clients.put(host+port, client);
        }
        return client;
    }

    public  void remoteClientFromCache(String host,Integer port){
        cache_clients.remove(host+port);
    }

    @Override
    public ChannelFragmentSender createSender (final String remoteNodeName, final String remoteChannelName) {
        return new ChannelFragmentSender() {
            @Override
            public Object sendMessageToRemote (Message message) {
                try
                {
                    sender.acquire();
                    if(!remoteNodeName.equals(getNodeName()))
                    {
                        if (!message.getPassedNodes().contains(getNodeName())) {
                            message.getPassedNodes().add(getNodeName());
                        }
                        String adr;
                        int port;
                        if(cache.containsKey(remoteNodeName))
                        {
                            adr = cache.get(remoteChannelName).adr;
                            port = cache.get(remoteChannelName).port;
                        } else {
                            adr =    getAddress(remoteNodeName);
                            port =     parsePortNumber(remoteNodeName);
                            InfoClient c = new InfoClient();
                            c.adr = adr;
                            c.port = port;
                            cache.put(remoteChannelName,c);
                        }
                        message.setDestNodeName(remoteNodeName);


                        if(adr.length() >0){
                            P2pClient client = new P2pClient(remoteNodeName,adr, port);
                            client.send(message);
                        }else
                        {
                            Log.warn("You need to specify an adress to "+remoteNodeName);
                            if(cache.containsKey(remoteNodeName))
                            {
                                cache.remove(remoteChannelName);
                            }

                        }

                    }

                } catch (Exception e) {
                    Log.debug("Error while sending message to " + remoteNodeName + "-" + getAddress(remoteNodeName));
                    if(getDictionary().get("replay").toString().equals("true")){
                        backupOnError.enqueue(message);
                    }

                }finally
                {
                    sender.release();
                }
                return null;
            }
        };
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
                    Log.warn("Attribute \"port\" of {} is not an Integer, default value ({}) is used.", getName(), port);
                }
            }
        } else {
            Log.warn("There is no channel named {}, default value ({}) is used.", getName(), port);
        }
        return port;
    }



    @Override
    public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
        return true;
    }

    @Override
    public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {

        return true;
    }

    @Override
    public boolean afterLocalUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {

        return true;
    }



    @Override
    public void modelUpdated() {
        cache.clear();
        model = getModelService().getLastModel();
    }

    @Override
    public void preRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
    }

    @Override
    public void postRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
    }

    public ContainerRoot getModel() {
        return model;
    }
}
