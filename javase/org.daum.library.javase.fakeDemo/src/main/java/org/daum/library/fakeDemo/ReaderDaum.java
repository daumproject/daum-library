package org.daum.library.fakeDemo;

import org.daum.library.fakeDemo.pojos.HeartMonitor;
import org.daum.library.fakeDemo.pojos.Moyen;
import org.daum.library.fakeDemo.pojos.TemperatureMonitor;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.ormH.utils.StoreHelper;
import org.daum.library.replica.*;
import org.daum.library.replica.msg.NotifyUpdate;
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
        @DictionaryAttribute(name = "mode", defaultValue = "temperature", optional = true, vals = {"temperature", "moyens","heart"})
}
)
@ComponentType
public class ReaderDaum extends AbstractComponentType {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SystemTime systemTime = new SystemTime();
    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    //  private  FrameMoyens frameMoyens=null;
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

            replicatingService =   this.getPortByName("service", ReplicaService.class);
            StoreImpl storeImpl = new StoreImpl(replicatingService);
            configuration.setStore(storeImpl);
            factory = configuration.getPersistenceSessionFactory();

        } catch (PersistenceException e) {
            logger.error("",e);
        }


        // listeners

        ChangeListener.getInstance().addEventListener(TemperatureMonitor.class,new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent propertyChangeEvent) {

                if(propertyChangeEvent.getCmd().equals(StoreCommand.ADD))
                {
                    processTemperature(propertyChangeEvent.getKey());
                }

            }
        }  );


        ChangeListener.getInstance().addEventListener(HeartMonitor.class,new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent propertyChangeEvent) {

                if(propertyChangeEvent.getCmd().equals(StoreCommand.ADD))
                {
                    processHeartMonitor(propertyChangeEvent.getKey());
                }

            }
        }  );

    }


    @Stop
    public void stop() {

    }

    @Update
    public void update() {
        stop();
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Port(name = "notify")
    public void notifybyReplica(Object m)
    {
        ChangeListener.getInstance().receive(m);
    }


}