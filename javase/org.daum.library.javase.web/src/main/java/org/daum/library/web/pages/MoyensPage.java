package org.daum.library.web.pages;

import org.daum.common.model.api.Demand;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.web.WebCache;
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



    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse)
    {

        logger.debug("MoyensPage "+kevoreeHttpRequest.getUrl());

        StringBuilder data = new StringBuilder();
        init();

        String page = "pages/moyens.html";
        String template = new String(WebCache.load(page));

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


        kevoreeHttpResponse.setContent(template.replace("$contenu$", data).replace("$ip$", WebCache.getAddress(getModelService().getLastModel(),getNodeName())));
        return kevoreeHttpResponse;
    }


}
