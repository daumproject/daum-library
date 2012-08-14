package org.daum.library.demos;

import org.daum.common.genmodel.*;
import org.daum.common.genmodel.impl.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Some;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 04/07/12
 * Time: 13:42
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "JavaSE", names = {"Android"})
@ComponentType
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = false) })
public class PopulateReplica extends AbstractComponentType {

    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean  called= false;


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

                for (Class c : SitacFactory.classes()) configuration.addPersistentClass(c);

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
                return true;
            }

            @Override
            public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public void modelUpdated() {
                init();

                if(called == false)
                {
                PersistenceSession session = null;
                try
                {
                    session = factory.getSession();

                    if(session != null)
                    {

                        SitacModel sitacModel = SitacFactory.createSitacModel();

                        Agent agentnoel = SitacFactory.createAgent();
                        agentnoel.setNom("PLOUZEAU");
                        agentnoel.setPrenom("NOEL");
                        agentnoel.setMatricule("nplouzeau");
                        agentnoel.setPassword("nplouzeau");
                        sitacModel.addPersonnes(agentnoel);
                        agentnoel.setAutorisation(AutorisationType.ALL);



                        Agent agentERWAN =  SitacFactory.createAgent();
                        agentERWAN.setNom("DAUBERT");
                        agentERWAN.setPrenom("Erwan");
                        agentERWAN.setMatricule("edaubert");
                        agentERWAN.setPassword("edaubert");
                        sitacModel.addPersonnes(agentERWAN);


                        Agent agentjed = SitacFactory.createAgent();
                        agentjed.setNom("DARTOIS");
                        agentjed.setPrenom("JEAN-EMILE");
                        agentjed.setMatricule("jed");
                        agentjed.setPassword("jed");
                        sitacModel.addPersonnes(agentjed);

                        Personne requerant = SitacFactory.createPersonne();
                        requerant.setNom("Fouquet");
                        requerant.setPrenom("François");


                        Intervention interventionfake =SitacFactory.createIntervention();

                        interventionfake.setRequerant(requerant);




                        Vehicule fpt2 =SitacFactory.createVehicule();
                        fpt2.setVehiculeType(VehiculeType.FPT);

                        Vehicule fpt =SitacFactory.createVehicule();
                        fpt.setVehiculeType(VehiculeType.FPT);


                        Moyens   moyens = SitacFactory.createMoyens();
                        moyens.addAgent(agentERWAN);
                        moyens.addAgent(agentjed);
                        moyens.addMateriel(fpt);
                        moyens.addMateriel(fpt2);



                        Detachement detachement = SitacFactory.createDetachement();
                        detachement.setChef((agentnoel));


                        Affectation affectation = SitacFactory.createAffectation();
                        affectation.setMoyen(moyens);
                        detachement.addAffectation(affectation);


                        interventionfake.addDetachements(detachement);
                        interventionfake.setDescription("l y a une voiture en flammes sur le bas coté ! L'homme est visiblement encore au volant de sa voiture  ");
                                                 CodeSinistre codeSinistre = SitacFactory.createInterventionType();
                        codeSinistre.setCode("332");

                        interventionfake.setType(codeSinistre);
                        sitacModel.addInterventions(interventionfake);


                        GpsPoint position = SitacFactory.createGpsPoint();
                        position.setLong(4811534);
                        position.setLat(-1638336);


                        interventionfake.setPosition(position);


                        session.save(sitacModel);


                        logger.warn("Agent size => "+session.getAll(AgentImpl.class).size());

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
                    called = true;
            }
            }
        });



    }
}
