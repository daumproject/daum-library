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
@DictionaryType({
        @DictionaryAttribute(name = "period", optional = false,defaultValue = "2000",fragmentDependant = false)
}
)
@ComponentType
public class ReaderDaum extends AbstractComponentType implements Runnable{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean  alive = true;
    private int period = 1000;
    private SystemTime systemTime = new SystemTime();

    private PersistenceConfiguration configuration=null;
    private PersistenceSessionFactoryImpl factory=null;
    private  FrameMoyens frameMoyens;
    @Start
    public void start()
    {
        try {
            configuration = new PersistenceConfiguration();
            configuration.addPersistentClass(TemperatureMonitor.class);
            configuration.addPersistentClass(Moyen.class);
            new Thread(this). start ();
            frameMoyens  = new FrameMoyens(getNodeName());
        } catch (PersistenceException e) {
            logger.error("",e);
        }

    }


    @Stop
    public void stop() {
        alive  = false;
    }

    @Update
    public void update() {
        try {
            period = Integer.parseInt(getDictionary().get("period").toString());
        } catch (Exception e){
            logger.error("Updating dictionnary ",e);
        }
    }


    @Override
    public void run() {
        period = Integer.parseInt(getDictionary().get("period").toString());
        ReplicatingService replicatingService =  this.getPortByName("service", ReplicatingService.class);
        StoreImpl storeImpl = new StoreImpl(replicatingService);
        configuration.setConnectionConfiguration(storeImpl);
        factory = configuration.getPersistenceSessionFactory();

        PersistenceSession s = null;



        // Random Générator temperature
        while(alive)
        {
            try
            {
                s  = factory.openSession();
                Set<Object> dates =  s.getAll(TemperatureMonitor.class).keySet();
                Date last =  systemTime.getDatemin(dates);
                TemperatureMonitor temp = (TemperatureMonitor) s.get(TemperatureMonitor.class,last);

                if(temp != null)
                {
                    String format = "temperature="+temp.getValue();
                    this.getPortByName("temperature", MessagePort.class).process(format);
                }


                Map<Object,Object> moyens = s.getAll(Moyen.class);
                logger.warn("Number of moyens "+moyens.size());
                frameMoyens.updateMoyens(moyens);



                try
                {
                    Thread.sleep(period);
                }   catch (Exception e)
                {

                }
                s.close();
            }

            catch (Exception e){
                logger.error("could not openSession ",e);
                try{
                    Thread.sleep(period);
                }   catch (Exception e2){

                }
            }


        }



    }


}