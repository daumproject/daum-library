package org.daum.library.sensors;

import org.daum.common.genmodel.GpsPoint;
import org.daum.common.genmodel.SitacFactory;
import org.daum.common.gps.api.GeoConstants;
import org.daum.common.gps.api.MathConstants;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 06/11/12
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "Android", names = {"JavaSE"})
@ComponentType
@DictionaryType(
        {
                @DictionaryAttribute(name="period",defaultValue="6000",optional=true),
                @DictionaryAttribute(name="min",defaultValue="60",optional=true),
                @DictionaryAttribute(name="max",defaultValue="110",optional=true)
        }
)
@Requires({
        @RequiredPort(name = "gen", type = PortType.MESSAGE,optional = true)
})
public class HeartRateFake extends AbstractComponentType implements  Runnable{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean  alive = true;
    private int period = 1000;
    private int min = 60;
    private int max = 100;

    @Start
    public void start()
    {
        new Thread(this). start ();
    }


    @Stop
    public void stop() {
        alive  = false;
    }

    @Update
    public void update() {
        try {
            period =  Integer.parseInt(getDictionary().get("period").toString());
            min =Integer.parseInt(getDictionary().get("min").toString());
            max =Integer.parseInt(getDictionary().get("max").toString());
        }   catch (Exception e){
            logger.error("Update fail ",e);
        }
    }


    @Override
    public void run() {
        int count = 0;

        while(alive)
        {
            java.util.Random rand = new java.util.Random();

            int val = min + rand.nextInt(max - min);

            getPortByName("gen", MessagePort.class).process(val);
            try
            {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                //ignore
            }
        }
    }

}