package org.daum.library.fakeDemo;

import org.daum.common.genmodel.Agent;
import org.daum.common.genmodel.Moyens;
import org.daum.common.genmodel.SitacFactory;
import org.daum.common.genmodel.impl.AgentImpl;
import org.daum.common.genmodel.impl.InterventionImpl;
import org.daum.common.genmodel.impl.MoyensImpl;
import org.daum.library.fakeDemo.pojos.HeartMonitor;
import org.daum.library.fakeDemo.pojos.Moyen;
import org.daum.library.fakeDemo.pojos.TemperatureMonitor;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.daum.library.replica.utils.SystemTime;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
//import org.kevoree.library.ui.layout.KevoreeLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 29/05/12
 * Time: 17:08
 */

@Library(name = "JavaSE", names = {"Android"})
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true)  ,
        @RequiredPort(name = "value", type = PortType.MESSAGE, optional = true)
})
@Provides({
        @ProvidedPort(name = "notify", type = PortType.MESSAGE)
})

@DictionaryType({
        @DictionaryAttribute(name = "mode", defaultValue = "temperature", optional = true, vals = {"temperature", "moyens","heart","sitactest"})
}
)
@ComponentType
public class ReaderDaum extends AbstractComponentType {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private ReplicaService replicatingService =  null;
    private  PersistenceSession s=null;


    @Start
    public void start()
    {
        try
        {
            configuration = new PersistenceConfiguration(getNodeName());

            configuration.addPersistentClass(TemperatureMonitor.class);
            configuration.addPersistentClass(Moyen.class);
            configuration.addPersistentClass(HeartMonitor.class);
            configuration.addPersistentClass(AgentImpl.class);
            configuration.addPersistentClass(InterventionImpl.class);
            for (Class c : SitacFactory.classes()) configuration.addPersistentClass(c);

            replicatingService =   this.getPortByName("service", ReplicaService.class);
            StoreImpl storeImpl = new StoreImpl(replicatingService);
            configuration.setStore(storeImpl);
            factory = configuration.getPersistenceSessionFactory();

        } catch (PersistenceException e)
        {
            logger.error("",e);
        }

        // listeners
        ChangeListener.getInstance("READERDAUM").addEventListener(TemperatureMonitor.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent e)
            {
                switch (e.getEvent())
                {
                    case ADD:
                        processTemperature(e.getId());
                        break;

                    case UPDATE:
                        processTemperature(e.getId());
                        break;
                }
            }
        });


        ChangeListener.getInstance("READERDAUM").addEventListener(HeartMonitor.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent e) {
                switch (e.getEvent())
                {
                    case ADD:
                        processHeartMonitor(e.getId());
                        break;

                    case UPDATE:
                        processHeartMonitor(e.getId());
                        break;
                }

            }
        });

        ChangeListener.getInstance("READERDAUM").addEventListener(AgentImpl.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent e) {

                processAgent(e.getId());


            }
        });


        ChangeListener.getInstance("READERDAUM").addEventListener(MoyensImpl.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent e) {


                processMoyenImpl(e.getId());

            }
        });

    }


    @Stop
    public void stop() {

    }

    @Update
    public void update() {
        stop();
    }





    public void processAgent(Object key){
        Agent temp = null;
        try
        {
            s  = factory.getSession();
            if(s != null)
            {

                temp = (Agent) s.get(AgentImpl.class,key);
                s.close();
               logger.warn(temp.getNom()+" "+temp.getPrenom());
                logger.warn("------------------------------------");

             for(Object key2:  s.getAll(MoyensImpl.class).keySet()){

                 for(Agent a: ((MoyensImpl)s.get(MoyensImpl.class,key2)).getAgentsForJ()){

                     logger.warn(a.getNom()+" "+a.getPrenom());
                 }
             }
            }


        } catch (PersistenceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            if( s != null){
                s.close();
            }
        }
    }



    public void processMoyenImpl(Object key){
        MoyensImpl temp = null;
        try
        {
            s  = factory.getSession();
            if(s != null)
            {
                temp = (MoyensImpl) s.get(MoyensImpl.class,key);
                s.close();
                for(Agent a: temp.getAgentsForJ())
                {
                    logger.warn(a.getNom()+" "+a.getPrenom());
                }
            }

        } catch (PersistenceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally
        {
            if( s != null){
                s.close();
            }
        }
    }

    public void processTemperature(Object key){
        TemperatureMonitor temp = null;
        try
        {
            s  = factory.getSession();
            if(s != null)
            {
                temp = (TemperatureMonitor) s.get(TemperatureMonitor.class,key);
                s.close();
                if(temp != null)
                {
                    String format = "temperature="+temp.getValue();
                    getPortByName("value", MessagePort.class).process(format);
                }
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        finally {
            if( s != null){
                s.close();
            }
        }
    }

    public void processHeartMonitor(Object key){
        HeartMonitor temp = null;

        try
        {
            s  = factory.getSession();
            temp = (HeartMonitor) s.get(HeartMonitor.class,key);
            s.close();
            if(temp != null)
            {
                String format = "HeartMonitor="+temp.getValue();
                getPortByName("value", MessagePort.class).process(format);
            }

        } catch (PersistenceException e) {
            e.printStackTrace();
        }   finally {
            if( s != null){
                s.close();
            }
        }
    }

    @Port(name = "notify")
    public void notifybyReplica(Object m)
    {
        ChangeListener.getInstance("READERDAUM").receive(m);
    }


}