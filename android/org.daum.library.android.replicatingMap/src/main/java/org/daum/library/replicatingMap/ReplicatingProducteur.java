package org.daum.library.replicatingMap;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@ComponentType
public class ReplicatingProducteur  extends AbstractComponentType implements  Runnable{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean  alive = true;
    private int period = 5000;

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

    }


    @Override
    public void run() {
        int count = 0;

        while(alive)
        {



            ReplicatingService c =  this.getPortByName("service", ReplicatingService.class);


            Cache cache =     c.getCache("test");

            cache.put(count,"Hello Word"+count);


            count++;
            logger.debug("ReplicatingProducteur ");


            try {
                Thread.sleep(period);
            } catch (Exception e2) {
                //ignore
            }
        }
    }
}