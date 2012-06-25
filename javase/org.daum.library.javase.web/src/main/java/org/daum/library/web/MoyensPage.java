package org.daum.library.web;

import org.daum.common.model.api.Demand;
import org.daum.library.fakeDemo.pojos.TemperatureMonitor;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.PortType;
import org.kevoree.annotation.RequiredPort;
import org.kevoree.annotation.Requires;
import org.kevoree.framework.KevoreePropertyHelper;
import org.kevoree.framework.NetworkHelper;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import scala.Option;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/06/12
 * Time: 16:06
 * To change this template use File | Settings | File Templates.
 */
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true) })
@ComponentType
public class MoyensPage extends AbstractPage {

    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;
    private PersistenceSession s=null;


    public void init()
    {
        if(replicaService == null )
        {
            try
            {
                configuration = new PersistenceConfiguration(getNodeName());
                replicaService =   this.getPortByName("service", ReplicaService.class);
                ReplicaStore store = new ReplicaStore(replicaService);
                configuration.setStore(store);
                configuration.addPersistentClass(Demand.class);
                factory = configuration.getPersistenceSessionFactory();

            } catch (PersistenceException e)
            {
                logger.error("init : ",e);
            }
        }
    }

    public String getAddress (String remoteNodeName)
    {
        String ip = "127.0.0.1";
        Option<String> ipOption = NetworkHelper.getAccessibleIP(KevoreePropertyHelper
                .getStringNetworkProperties(this.getModelService().getLastModel(), remoteNodeName, org.kevoree.framework.Constants.KEVOREE_PLATFORM_REMOTE_NODE_IP()));
        if (ipOption.isDefined()) {
            ip = ipOption.get();
        }
        return ip;
    }



    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse)
    {
        logger.debug("MoyensPage");
        init();
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        PersistenceSession session = null;
        try
        {
            session = factory.getSession();

            if(session != null)
            {
                Map<Object,Demand> demands= (Map<Object, Demand>) session.getAll(Demand.class);

                if(demands != null)
                {
                    for(Object key : demands.keySet())
                    {
                        Demand d = demands.get(key);
                        html.append(d+"<br>");
                    }
                }


            }
        } catch (PersistenceException ex)
        {
            logger.error("",ex);
        }
        finally
        {
            if (session != null) session.close();

        }

        html.append("</html>");
        kevoreeHttpResponse.setContent(html.toString());
        return kevoreeHttpResponse;
    }


}
