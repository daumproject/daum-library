package org.daum.library.replicatingMap;

import org.daum.library.replicatingMap.msg.Message;

import java.util.List;

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
