package org.daum.library.sensors;

import android.util.Log;
import org.daum.common.genmodel.*;
import org.daum.common.genmodel.impl.AgentImpl;
import org.daum.common.genmodel.impl.GpsPointImpl;
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
import org.kevoree.framework.MessagePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/11/12
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "JavaSE", names = {"Android"})
@ComponentType

@Provides({
        @ProvidedPort(name = "heartmonitor", type = PortType.MESSAGE),
        @ProvidedPort(name = "position",type = PortType.MESSAGE)
})

@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = false) })

@DictionaryType({
        @DictionaryAttribute(name = "idAgent", defaultValue = "nplouzeau")
})
public class ManagerFireFighter  extends AbstractComponentType {

    private static final String TAG = "ManagerFireFighter";
    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicaService =  null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean  called= false;
    private double  lastheart=-1;

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


    public Agent findAgent(String id){

        if(factory != null){
            PersistenceSession s = null;
            try {
                s = factory.getSession();
                Map<String, AgentImpl> agents = s.getAll(AgentImpl.class);
                for (Agent agent : agents.values())
                {
                    if (agent.getMatricule().equals(id))
                    {
                        return agent;
                    }
                }

            } catch (PersistenceException e) {
                Log.e(TAG, "PersistenceException ", e);
            }

        }
        return null;
    }



    @Port(name = "heartmonitor")
    public void heartmonitor(Object o)
    {
        try{
            Double heart =  Double.parseDouble(o.toString());
            if(heart != lastheart){
                PersistenceSession s = null;
                try
                {
                    if(factory != null)
                    {
                        s = factory.getSession();
                        DatedValue values=null;
                        Agent agent =   findAgent(getDictionary().get("idAgent").toString());
                        if(agent != null){
                            values = (DatedValue) agent.getCapteur("heart");
                            values.addValue(heart);
                            s.update(agent);
                            s.close();
                            Log.e(TAG,"update heart "+agent.getMatricule()+" "+values.lastUpdate());
                        }   else
                        {
                            Log.e(TAG, "The Agent is not available  "+getDictionary().get("idAgent").toString());
                        }
                    }  else {
                        Log.d(TAG, "The factory is not available");
                    }


                } catch (PersistenceException e) {
                    Log.e(TAG, "PersistenceException ", e);
                }  finally {
                    if(s != null){
                        s.close();
                    }
                }
            }
            lastheart = heart;


        }   catch (Exception e){
            e.printStackTrace();
        }

    }
    @Port(name = "position")
    public void position(Object o)
    {
        if(o instanceof GpsPointImpl)
        {
            if(factory != null){
                PersistenceSession s = null;
                try {
                    s = factory.getSession();
                    Agent agent =   findAgent(getDictionary().get("idAgent").toString());
                    if(agent != null){
                        agent.setposRef((Position)o);
                        s.update(agent);
                        logger.debug("update pos "+agent.getMatricule()+" "+agent.getPosRef());
                    }  else         {
                        Log.e(TAG, "The Agent is not available  "+getDictionary().get("idAgent").toString());
                    }


                } catch (PersistenceException e) {
                    Log.e(TAG, "PersistenceException ", e);
                } finally
                {
                    if(s !=null)
                    {
                        s.close();
                    }
                }

            }
        }else {
            Log.e(TAG, "Factor is null");
        }
    }
}
