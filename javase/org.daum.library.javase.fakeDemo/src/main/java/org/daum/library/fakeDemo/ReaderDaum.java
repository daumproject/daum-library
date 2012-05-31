package org.daum.library.fakeDemo;

import org.daum.library.fakeDemo.pojos.HeartMonitor;
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

@Library(name = "JavaSE")
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicatingService.class, optional = true)  ,
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
    private  FrameMoyens frameMoyens=null;
    private ReplicatingService replicatingService =  null;
    private  PersistenceSession s=null;


    @Start
    public void start()
    {
        try
        {
            configuration = new PersistenceConfiguration();

            configuration.addPersistentClass(TemperatureMonitor.class);
            configuration.addPersistentClass(Moyen.class);
            configuration.addPersistentClass(HeartMonitor.class);

            manageMoyens();
        } catch (PersistenceException e) {
            logger.error("",e);
        }
    }


    @Stop
    public void stop() {
        if(frameMoyens != null){
            frameMoyens.dispose();
        }
    }

    @Update
    public void update() {

        manageMoyens();

    }

    public void manageMoyens(){

        if(getDictionary().get("mode").toString().equals("moyens"))
        {
            frameMoyens  = new FrameMoyens(getNodeName(),this);
        }  else
        {
            if(frameMoyens != null){
                frameMoyens.dispose();
            }
        }

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


            if(getDictionary().get("mode").toString().equals("temperature"))
            {
                Set<Object> dates =  s.getAll(TemperatureMonitor.class).keySet();
                if(dates.size() > 0)
                {
                    Date last =  systemTime.getDatemin(dates);
                    TemperatureMonitor temp = (TemperatureMonitor) s.get(TemperatureMonitor.class,last);
                    if(temp != null)
                    {
                        String format = "temperature="+temp.getValue();
                        this.getPortByName("value", MessagePort.class).process(format);
                    }
                }

            }else if(getDictionary().get("mode").toString().equals("moyens"))
            {
                Map<Object,Object> moyens = s.getAll(Moyen.class);
                if(moyens.size() >0)
                {
                    frameMoyens.updateMoyens(moyens);

                }
            }  else if(getDictionary().get("mode").toString().equals("heart"))
            {

                Set<Object> dates =  s.getAll(HeartMonitor.class).keySet();
                if(dates.size() > 0)
                {
                    Date last =  systemTime.getDatemin(dates);
                    HeartMonitor temp = (HeartMonitor) s.get(HeartMonitor.class,last);
                    if(temp != null)
                    {
                        String format = "heart="+temp.getValue();
                        this.getPortByName("value", MessagePort.class).process(format);
                    }
                }

            }


            s.close();
        }
        catch (Exception e){
            logger.error("could not openSession ",e);
        }
    }


}