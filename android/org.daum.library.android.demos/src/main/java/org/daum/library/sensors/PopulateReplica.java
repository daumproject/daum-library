package org.daum.library.sensors;

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
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true) })
public class PopulateReplica extends AbstractComponentType
{
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
            public boolean afterLocalUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void modelUpdated() {
                init();

                if(called == false && factory !=null)
                {
                    PersistenceSession session = null;
                    try
                    {
                        session = factory.getSession();

                        if(session != null)
                        {

                            SitacModel sitacModel = SitacFactory.createSitacModel();



                            GpsPoint po = SitacFactory.createGpsPoint();
                            po.setLat(48120282);
                            po.setLong(-1650592);



                            Agent agentnoel = SitacFactory.createAgent();
                            agentnoel.setNom("PLOUZEAU");
                            agentnoel.setPrenom("NOEL");
                            agentnoel.setMatricule("nplouzeau");
                            agentnoel.setPassword("nplouzeau");
                            sitacModel.addPersonnes(agentnoel);
                            agentnoel.setAutorisation(AutorisationType.ALL);
                            agentnoel.setposRef(po);



                            GpsPoint po2 = SitacFactory.createGpsPoint();
                            po2.setLat(48115411);
                            po2.setLong(-1648253);


                            Agent agentjed = SitacFactory.createAgent();
                            agentjed.setNom("DARTOIS");
                            agentjed.setPrenom("JEAN-EMILE");
                            agentjed.setMatricule("jed");
                            agentjed.setPassword("jed");
                            agentjed.setposRef(po2);

                            sitacModel.addPersonnes(agentjed);

                            GpsPoint po3 = SitacFactory.createGpsPoint();
                            po3.setLat(48118892);
                            po3.setLong(-1638275);


                            Agent agentolivier = SitacFactory.createAgent();
                            agentolivier.setNom("Barais");
                            agentolivier.setPrenom("Olivier");
                            agentolivier.setMatricule("obarais");
                            agentolivier.setPassword("barias");
                            agentolivier.setposRef(po3);


                            sitacModel.addPersonnes(agentolivier);

                            Personne requerant = SitacFactory.createPersonne();
                            requerant.setNom("Demo");
                            requerant.setPrenom("Demo");


                            Intervention interventionfake =SitacFactory.createIntervention();

                            interventionfake.setRequerant(requerant);





                            Vehicule fpt2 =SitacFactory.createVehicule();
                            fpt2.setVehiculeType(VehiculeType.FPT);

                            Vehicule fpt =SitacFactory.createVehicule();
                            fpt.setVehiculeType(VehiculeType.FPT);


                            Moyens   moyens = SitacFactory.createMoyens();
                            moyens.addAgent(agentjed);
                            moyens.addAgent(agentolivier);
                            moyens.addMateriel(fpt);
                            moyens.addMateriel(fpt2);




                            Detachement detachement = SitacFactory.createDetachement();
                            detachement.setChef((agentnoel));



                            Affectation affectation = SitacFactory.createAffectation();
                            affectation.setMoyen(moyens);
                            detachement.addAffectation(affectation);


                            interventionfake.addDetachements(detachement);
                            interventionfake.setDescription("il y a une voiture en flammes sur le bas coté ! L'homme est visiblement encore au volant de sa voiture  ");
                            CodeSinistre codeSinistre = SitacFactory.createInterventionType();
                            codeSinistre.setCode("332");

                            interventionfake.setType(codeSinistre);
                            sitacModel.addInterventions(interventionfake);


                            GpsPoint position = SitacFactory.createGpsPoint();
                            position.setLong(4811534);
                            position.setLat(-1638336);


                            interventionfake.setPosition(position);


                            session.save(sitacModel);


                            logger.debug("Agent size => "+session.getAll(AgentImpl.class).size());

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

            @Override
            public void preRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void postRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });



    }
}
