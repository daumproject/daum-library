package org.daum.library.replica.cluster;

import org.daum.library.replica.channel.Channel;
import org.daum.library.replica.channel.KChannelImpl;
import org.daum.library.replica.msg.Message;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 07/06/12
 * Time: 14:15
 */
public interface ICluster {

    public List<Node> getNodesOfCluster();
    public Node getCurrentNode();
    public void addNode(Node node);
    public void shutdown();
    public Channel getChannel();
    public void synchronize();



    public ICacheManger getCacheManager();
    public void remoteReceived(Message o);
    void setChanel(KChannelImpl kChannel);
}
