package org.daum.library.fakeDemo;

import org.daum.common.genmodel.Agent;
import org.daum.common.genmodel.Capteurs;
import org.daum.common.genmodel.DatedValue;
import org.daum.common.genmodel.SitacFactory;
import org.daum.common.genmodel.impl.AgentImpl;
import org.daum.common.genmodel.impl.DatedValueImpl;
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
import java.util.Map;
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
        @DictionaryAttribute(name = "mode", defaultValue = "temperature", optional = true, vals = {"temperature","heartmonitor"}) ,
        @DictionaryAttribute(name = "agent", defaultValue = "jedartois")
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
            for (Class c : SitacFactory.classes()) configuration.addPersistentClass(c);
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

                        if (getDictionary().get("mode").toString().equals("temperature"))
                        {
                            TemperatureMonitor temperatureMonitor = new TemperatureMonitor();
                            temperatureMonitor.setDate(systemTime.getCurrentDate());

                            temperatureMonitor.setValue(value);

                            s.save(temperatureMonitor);

                            Set<Object> dates = (Set<Object>) s.getAll(TemperatureMonitor.class).keySet();
                            if (dates.size() > 3)
                            {
                                //delete first
                                Date d = systemTime.getDatemin(dates);
                                s.delete(s.get(TemperatureMonitor.class, d));
                            }

                        }else  if (getDictionary().get("mode").toString().equals("heartmonitor"))
                        {

                            Map<String, AgentImpl> agents = s.getAll(AgentImpl.class);
                            for (Agent agent : agents.values()) {
                                if (agent.getMatricule().equals(getDictionary().get("agent").toString())) {

                                   if(agent.getCapteurs().get("heartmonitor") == null){
                                       agent.getCapteurs().put("heartmonitor",new DatedValueImpl());
                                   }

                                    DatedValueImpl value1 = (DatedValueImpl) agent.getCapteurs().get("heartmonitor");
                                    if(value1 != null)
                                    {
                                        value1.addValue(value);
                                    }else {
                                        logger.warn("DatedValueImpl is null ");
                                    }

                                    s.update(agent);

                                }
                            }


                        }

                    }
                }
            } catch (Exception e) {
                logger.warn("ArduinoGW bad message => ", e);
            } finally {
                s.close();
            }

        } catch (Exception e) {
            logger.error("could not save message ", e);
            replicatingService = null;
        }


    }
}
