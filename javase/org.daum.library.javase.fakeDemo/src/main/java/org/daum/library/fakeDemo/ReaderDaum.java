package org.daum.library.fakeDemo;

import org.daum.library.fakeDemo.pojos.Moyen;
import org.daum.library.fakeDemo.pojos.TemperatureMonitor;
import org.daum.library.fakeDemo.views.FrameMoyens;
import org.daum.library.ormHM.persistence.PersistenceConfiguration;
import org.daum.library.ormHM.persistence.PersistenceSession;
import org.daum.library.ormHM.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormHM.utils.PersistenceException;
import org.daum.library.replicatingMap.ReplicatingService;
import org.daum.library.replicatingMap.utils.SystemTime;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
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
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicatingService.class, optional = true)  ,
        @RequiredPort(name = "temperature", type = PortType.MESSAGE, optional = true)
})
@Provides({
        @ProvidedPort(name = "notify", type = PortType.MESSAGE)
})
/*
@DictionaryType({
        @DictionaryAttribute(name = "period", optional = false,defaultValue = "2000",fragmentDependant = false)
}
)   */
@ComponentType
public class ReaderDaum extends AbstractComponentType {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SystemTime systemTime = new SystemTime();

    public PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private  FrameMoyens frameMoyens;
    private ReplicatingService replicatingService =  null;
    private  PersistenceSession s=null;
    @Start
    public void start()
    {
        try {
            configuration = new PersistenceConfiguration();
            configuration.addPersistentClass(TemperatureMonitor.class);
            configuration.addPersistentClass(Moyen.class);
            frameMoyens  = new FrameMoyens(getNodeName(),this);
        } catch (PersistenceException e) {
            logger.error("",e);
        }

    }


    @Stop
    public void stop() {

    }

    @Update
    public void update() {

    }


    @Port(name = "notify")
    public void notifybyReplica(Object msg) {

        if(replicatingService == null)
        {
            replicatingService =   this.getPortByName("service", ReplicatingService.class);
            StoreImpl storeImpl = new StoreImpl(replicatingService);

            configuration.setConnectionConfiguration(storeImpl);
            factory = configuration.getPersistenceSessionFactory();
        }



        try
        {
            s  = factory.openSession();


            Set<Object> dates =  s.getAll(TemperatureMonitor.class).keySet();
            if(dates.size() > 0)
            {
                Date last =  systemTime.getDatemin(dates);
                TemperatureMonitor temp = (TemperatureMonitor) s.get(TemperatureMonitor.class,last);
                if(temp != null)
                {
                    String format = "temperature="+temp.getValue();
                    this.getPortByName("temperature", MessagePort.class).process(format);
                }
            }

            Map<Object,Object> moyens = s.getAll(Moyen.class);
            if(moyens.size() >0)
            {
                frameMoyens.updateMoyens(moyens);

            }

            s.close();
        }
        catch (Exception e){
            logger.error("could not openSession ",e);
        }
    }


}