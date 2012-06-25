package org.daum.library.fakeDemo;

import org.daum.library.fakeDemo.pojos.HeartMonitor;
import org.daum.library.fakeDemo.pojos.Moyen;
import org.daum.library.fakeDemo.pojos.TemperatureMonitor;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.utils.SystemTime;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 30/05/12
 * Time: 16:47
 * <p/>
 * Gateway arduino to HashMap
 */

@Library(name = "JavaSE", names = {"Android"})
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true)
})
@Provides({
        @ProvidedPort(name = "inputvalue", type = PortType.MESSAGE)
})
@DictionaryType({
        @DictionaryAttribute(name = "mode", defaultValue = "temperature", optional = true, vals = {"temperature"})
}
)
@ComponentType
public class ArduinoGW extends AbstractComponentType {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public PersistenceConfiguration configuration = null;
    private PersistenceSessionFactoryImpl factory = null;
    private ReplicaService replicatingService = null;
    private PersistenceSession s = null;

    @Start
    public void start() {
        try {
            configuration = new PersistenceConfiguration(getNodeName());

            configuration.addPersistentClass(TemperatureMonitor.class);
            configuration.addPersistentClass(Moyen.class);
            configuration.addPersistentClass(HeartMonitor.class);

        } catch (PersistenceException e) {
            logger.error("", e);
        }

    }

    @Stop
    public void stop() {
        configuration = null;
    }

    @Update
    public void update() {
        stop();
        start();
    }


    @Port(name = "inputvalue")
    public void inputvalue(Object msg) {
        if (replicatingService == null) {
            replicatingService = this.getPortByName("service", ReplicaService.class);
            StoreImpl storeImpl = new StoreImpl(replicatingService);
            configuration.setStore(storeImpl);
            factory = configuration.getPersistenceSessionFactory();
        }

        try {
            s = factory.getSession();
            SystemTime systemTime = new SystemTime();
            try {
                //c/28
                String[] values = msg.toString().split(",");


                //System.out.println(values.length);;
                for (int i = 0; i < values.length; i++) {
                    String[] lvl = values[i].split("=");

                    if (lvl.length >= 2) {
                        Double value = Double.parseDouble(lvl[1]);

                        if (getDictionary().get("mode").toString().equals("temperature")) {

                            TemperatureMonitor temperatureMonitor = new TemperatureMonitor();
                            temperatureMonitor.setDate(systemTime.getCurrentDate());

                            temperatureMonitor.setValue(value);

                            s.save(temperatureMonitor);


                            Set<Object> dates = (Set<Object>) s.getAll(TemperatureMonitor.class).keySet();
                            if (dates.size() > 3) {
                                //delete first
                                Date d = systemTime.getDatemin(dates);
                                s.delete(s.get(TemperatureMonitor.class, d));

                            }

                        }

                    }
                }
            } catch (Exception e) {
                logger.warn("ArduinoGW bad message => ", e.getMessage());
                replicatingService = null;
            } finally {
                s.close();
            }

        } catch (Exception e) {
            logger.error("could not save message ", e);
            replicatingService = null;
        }


    }
}
