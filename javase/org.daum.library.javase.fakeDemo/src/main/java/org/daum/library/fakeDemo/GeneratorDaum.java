package org.daum.library.fakeDemo;

import org.daum.library.fakeDemo.pojos.Moyen;
import org.daum.library.fakeDemo.pojos.MoyenType;
import org.daum.library.fakeDemo.pojos.TemperatureMonitor;
import org.daum.library.ormHM.persistence.PersistenceConfiguration;
import org.daum.library.ormHM.persistence.PersistenceSession;
import org.daum.library.ormHM.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormHM.utils.PersistenceException;
import org.daum.library.replicatingMap.ReplicatingService;
import org.daum.library.replicatingMap.utils.SystemTime;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;
import java.util.Set;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 13:11
 */
@Library(name = "JavaSE", names = {"Android"})
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicatingService.class, optional = true)
})
@DictionaryType({
        @DictionaryAttribute(name = "period", optional = false,defaultValue = "200",fragmentDependant = false) ,
        @DictionaryAttribute(name = "MaxEntries", optional = false,defaultValue = "10",fragmentDependant = false)
}
)
@ComponentType
public class GeneratorDaum extends AbstractComponentType implements Runnable{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean  alive = true;
    private int period = 1000;
    private int MaxEntries = 40;

    PersistenceConfiguration configuration=null;
    PersistenceSessionFactoryImpl factory=null;


    @Start
    public void start()
    {

        try {
            configuration = new PersistenceConfiguration();
            configuration.addPersistentClass(TemperatureMonitor.class);
            configuration.addPersistentClass(Moyen.class);
            new Thread(this). start ();
        } catch (PersistenceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
            MaxEntries =   Integer.parseInt(getDictionary().get("MaxEntries").toString());
        } catch (Exception e){
            logger.error("Updating dictionnary ",e);
        }
    }


    @Override
    public void run() {


        period = Integer.parseInt(getDictionary().get("period").toString());
        MaxEntries =   Integer.parseInt(getDictionary().get("MaxEntries").toString());


        int range_min=25;
        int range_max=40;
        ReplicatingService replicatingService =  this.getPortByName("service", ReplicatingService.class);



        StoreImpl storeImpl = new StoreImpl(replicatingService);

        configuration.setConnectionConfiguration(storeImpl);
        factory = configuration.getPersistenceSessionFactory();

        SystemTime systemTime = new SystemTime();
        Random random  = new Random();


        Moyen m1 = new Moyen(new MoyenType(1), "FPT", 1);
        m1.setCaserne("RENNES");
        Moyen m2 = new Moyen(new MoyenType(2), "VSAV", 2);
        m1.setCaserne("RENNES");
        Moyen m3 = new Moyen(new MoyenType(1), "FPT", 3);
        m3.setCaserne("BRUZ");
        Moyen m4 = new Moyen(new MoyenType(1), "FPT", 4);
        m4.setCaserne("PACE");
        Moyen m5 = new Moyen(new MoyenType(2), "VSAV", 5);
        m5.setCaserne("BETTON");

        try
        {
            PersistenceSession s = factory.openSession();
            s.save(m1);
            s.save(m2);
            s.save(m3);
            s.save(m4);
            s.save(m5);
            s.close();
        }catch (Exception e){
             logger.error("Fail to initiate moyens ",e);

        }

        while(alive)
        {
            try
            {
                PersistenceSession s = factory.openSession();

                try
                {
                    // generate Random
                    TemperatureMonitor temperatureMonitor = new TemperatureMonitor();
                    temperatureMonitor.setDate(systemTime.getCurrentDate());
                    int heatbeat_value = range_min + random.nextInt(range_max - range_min);
                    temperatureMonitor.setValue(heatbeat_value);

                    // save
                    s.save(temperatureMonitor);

                    Set<Object> dates =  s.getAll(TemperatureMonitor.class).keySet();
                    if(dates.size() > MaxEntries-1)
                    {
                        //delete first
                        Date d =  systemTime.getDatemin(dates);
                        s.delete(s.get(TemperatureMonitor.class,d));

                    }

                } catch (PersistenceException e) {
                    e.printStackTrace();
                }



                try{
                    Thread.sleep(period);
                }   catch (Exception e){

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