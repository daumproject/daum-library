package org.daum.library.web.pages;

import org.daum.common.model.api.Demand;
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
import org.kevoree.library.javase.webserver.FileServiceHelper;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import scala.Option;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/06/12
 * Time: 16:06
 * To change this template use File | Settings | File Templates.
 */
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = false) })
@ComponentType
public class MoyensPage extends AbstractPage {

    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;
    private PersistenceSession s=null;
    private HashMap<String,byte[]> cache = new HashMap<String, byte[]>();

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


    public byte[] load(String url)
    {
        if(cache.containsKey(url))
        {
            return cache.get(url);
        } else {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try
            {
                InputStream res =    getClass().getClassLoader().getResourceAsStream(url);
                if(res != null){
                    int nRead;
                    byte[] data = new byte[16384];
                    while ((nRead = res.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                    cache.put(url,buffer.toByteArray());
                } else
                {
                    return "404".getBytes();
                }

            } catch (IOException e) {
                logger.error("",e);
                return "404".getBytes();
            }
            return buffer.toByteArray();
        }

    }

    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse)
    {

        logger.debug("MoyensPage "+kevoreeHttpRequest.getUrl());

        StringBuilder data = new StringBuilder();
        init();

        String template =   new String(load("pages/moyens.html"));

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
                        Demand demand = demands.get(key);
                        data.append("<tr class='gradeX'>");
                        data.append(" <td>"+demand.getId()+"</td>      ");
                        data.append(" <td>"+demand.getType()+"  </td> ");
                        data.append(" <td>"+demand.getGh_crm()+"  </td> ");
                        data.append(" <td>"+demand.getGh_demande()+"  </td> ");
                        data.append("  </tr>     ");

                    }
                }


            }
        } catch (PersistenceException ex)
        {
            logger.error("",ex);
            replicaService = null;
        }
        finally
        {
            if (session != null) session.close();

        }


        kevoreeHttpResponse.setContent(template.replace("$contenu$", data).replace("$ip$", getAddress(getNodeName())));
        return kevoreeHttpResponse;
    }


}
