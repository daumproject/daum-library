package org.daum.library.p2pSock;

import org.kevoree.annotation.*;
import org.kevoree.framework.*;
import org.kevoree.framework.message.Message;
import org.slf4j.LoggerFactory;
import scala.Option;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 08/08/12
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "JavaSE", names = {"Android"})
@org.kevoree.annotation.ChannelTypeFragment
@DictionaryType({
        @DictionaryAttribute(name = "port", defaultValue = "9000", optional = true, fragmentDependant = true)
})
public class P2pSock extends AbstractChannelFragment {

    private P2pServer server;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private Thread t_server;
        @Start
    public void startp()
    {
        try {
         server = new P2pServer(this,parsePortNumber(getNodeName().toString()));
            t_server = new Thread(server);
            t_server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Stop
    public void stopp(){
        t_server.interrupt();
    }

    @Update
    public void updatep()
    {

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





      @Override
    public ChannelFragmentSender createSender (final String remoteNodeName, final String remoteChannelName) {
        return new ChannelFragmentSender() {
            @Override
            public Object sendMessageToRemote (Message message) {
                try {
                    if (!message.getPassedNodes().contains(getNodeName())) {
                        message.getPassedNodes().add(getNodeName());
                    }
                    P2pClient client = new P2pClient(getAddress(remoteNodeName), parsePortNumber(remoteNodeName));
                    client.send(message);
                    //    clientBootStrap.connect(new InetSocketAddress(getAddress(remoteNodeName),parsePortNumber(remoteNodeName)));
                } catch (IOException e) {
                    logger.debug("Error while sending message to " + remoteNodeName + "-" + remoteChannelName);
                }
                return null;
            }
        };
    }
    public String getAddress (String remoteNodeName) {
        String ip = "127.0.0.1";
        Option<String> ipOption = NetworkHelper.getAccessibleIP(KevoreePropertyHelper
                .getStringNetworkProperties(this.getModelService().getLastModel(), remoteNodeName, org.kevoree.framework.Constants.KEVOREE_PLATFORM_REMOTE_NODE_IP()));
        if (ipOption.isDefined()) {
            ip = ipOption.get();
        }
        return ip;
    }


    public int parsePortNumber (String nodeName) throws IOException {
        try {
            //logger.debug("look for port on " + nodeName);
            Option<Integer> portOption = KevoreePropertyHelper.getIntPropertyForChannel(this.getModelService().getLastModel(), this.getName(), "port", true, nodeName);
            if (portOption.isDefined()) {
                return portOption.get();
            } else {
                return 9000;
            }
        } catch (NumberFormatException e) {
            throw new IOException(e.getMessage());
        }
    }
}
