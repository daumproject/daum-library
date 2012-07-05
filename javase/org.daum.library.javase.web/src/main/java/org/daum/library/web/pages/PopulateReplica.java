package org.daum.library.web.pages;

import org.daum.common.model.api.Demand;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.framework.AbstractComponentType;
import org.sitac.*;
import org.sitac.impl.AgentImpl;
import org.sitac.impl.InterventionImpl;
import org.sitac.impl.MoyenImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 04/07/12
 * Time: 13:42
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "JavaSE")
@ComponentType
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = false) })
public class PopulateReplica extends AbstractComponentType {

    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


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
                configuration.addPersistentClass(AgentImpl.class);
                configuration.addPersistentClass(InterventionImpl.class);
                configuration.addPersistentClass(MoyenImpl.class);
                factory = configuration.getPersistenceSessionFactory();


            } catch (PersistenceException e)
            {
                logger.error("init : ",e);
            }
        }
    }


    @Stop
    public void stop()
    {


    }


    @Update
    public void update()
    {


    }
    @Start
    public void start()
    {
        getModelService().registerModelListener(new ModelListener() {
            @Override
            public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void modelUpdated() {
                init();

                PersistenceSession session = null;
                try
                {
                    session = factory.getSession();

                    if(session != null)
                    {

                        SitacModel sitacModel = new SitacModel();



                        Agent agentnoel =SitacFactory.createAgent();

                        agentnoel.setNom("PLOUZEAU");
                        agentnoel.setPrenom("NOEL");
                        sitacModel.addPersonnes(agentnoel);

                        Agent agentERWAN =  SitacFactory.createAgent();
                        agentERWAN.setNom("DAUBERT");
                        agentERWAN.setPrenom("Erwan");

                        sitacModel.addPersonnes(agentERWAN);

                        Agent agentMaxime = SitacFactory.createAgent();
                        agentMaxime.setNom("TRICOIRE");
                        agentMaxime.setPrenom("Maxime");

                        sitacModel.addPersonnes(agentMaxime);

                        Agent agentjed = SitacFactory.createAgent();
                        agentjed.setNom("DARTOIS");
                        agentjed.setPrenom("JEAN-EMILE");
                        agentjed.setMatricule("monmatricule");


                        sitacModel.addPersonnes(agentjed);



                      session.save(sitacModel);



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
            }
        });
    }
}
