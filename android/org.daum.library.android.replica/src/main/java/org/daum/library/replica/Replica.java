package org.daum.library.replica;

import org.daum.library.replica.cache.Cache;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.channel.KChannelImpl;
import org.daum.library.replica.cluster.ClusterImpl;
import org.daum.library.replica.cluster.ICluster;
import org.daum.library.replica.cluster.Node;
import org.daum.library.replica.msg.Message;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 22/05/12
 * Time: 18:00
 *
 * TODO : add power manangement of nodes
 * TODO : add persistence (https://github.com/jankotek/JDBM3)   more generics
 * todo : USE LRU
 * todo
 * TODO : add conflits manager
 * TODO : merge dependencies
 */



@Library(name = "JavaSE", names = {"Android"})
@ComponentType
@Requires({
        @RequiredPort(name = "broadcast", type = PortType.MESSAGE,optional = true),
        @RequiredPort(name = "notification", type = PortType.MESSAGE,optional = true)
})
@DictionaryType({
        @DictionaryAttribute(name = "synchronize", defaultValue = "true", optional = true,vals={"true","false"}) ,
        @DictionaryAttribute(name = "diskPersitence", defaultValue = "true", optional = true,vals={"true","false"}) ,
        @DictionaryAttribute(name = "path_disk", defaultValue = "/tmp/replica", optional = true)
})
@Provides({
        @ProvidedPort(name = "remote", type = PortType.MESSAGE) ,
        @ProvidedPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class)
})
public class Replica extends AbstractComponentType implements ReplicaService {

    private KChannelImpl kChannel=null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ICluster cluster=null;

    @Start
    public void start()
    {
        kChannel   = new KChannelImpl(this);
        Node node = new Node(getNodeName());
        try
        {
            Boolean  synchronize = Boolean.parseBoolean(getDictionary().get("synchronize").toString());
            Boolean  diskPersitence = Boolean.parseBoolean(getDictionary().get("diskPersitence").toString());
            String path_disk =       getDictionary().get("path_disk").toString();
            cluster = new ClusterImpl(node,kChannel,diskPersitence,path_disk);
            if(synchronize)
            {
                // request synchronize
                cluster.synchronize();
            }

        } catch (Exception e)
        {
            logger.warn("Fail get Dictionary synchronize ",e);
        }
    }

    @Stop
    public void stop()
    {
        if(cluster != null)
            cluster.shutdown();
    }

    @Update
    public void update()
    {
        kChannel   = new KChannelImpl(this);
        if(cluster != null){
            cluster.setChanel(kChannel);
            cluster.setPath_disk(getDictionary().get("path_disk").toString());
        }else {
            logger.error("update fail cluster is null ");
        }
    }

    @Port(name = "remote")
    public void remote(Object o)
    {
        if(o instanceof Message)
        {
            cluster.remoteReceived((Message)o);
        }
    }

    @Port(name = "service", method = "getCache")
    @Override
    public Cache getCache(String name) {
        if(cluster == null){ return null; }
        return cluster.getCacheManager().getCache(name);
    }

}
