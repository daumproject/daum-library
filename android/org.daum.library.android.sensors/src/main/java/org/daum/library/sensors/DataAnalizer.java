package org.daum.library.sensors;

import org.daum.common.genmodel.GpsPoint;
import org.daum.common.genmodel.Motion;
import org.daum.common.genmodel.impl.GpsPointImpl;
import org.kevoree.annotation.*;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 27/09/12
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Android")
@Provides({
        @ProvidedPort(name = "motion", type = PortType.MESSAGE),
        @ProvidedPort(name = "position",type = PortType.MESSAGE)
})
@ComponentType
public class DataAnalizer
{
    private LRUMap<Date, GpsPointImpl> position = new LRUMap<Date, GpsPointImpl>(100);
    private LRUMap<Date, Motion> motions = new LRUMap<Date, Motion>(100);

    @Start
    public void start()
    {

    }

    @Stop
    public void stop(){

    }

    @Update
    public void update(){


    }

    @Port(name = "motion")
    public void x(Object o)
    {
        if(o instanceof Motion){
            motions.put(new Date(),(Motion)o);
        }
    }


    @Port(name = "position")
    public void position(Object o)
    {
        if(o instanceof GpsPointImpl){
            position.put(new Date(),(GpsPointImpl)o);
        }
    }

    /*
    Idle: The user is holding the phone in  an idle state
    Walking: The user is walking
    Running: The user is running
    Jumping: The user is jumping
    Descending Stairs: The user is descending stairs
    Climbing Stairs: The user is climbing  stairs
    */
    public void analyzeMotion(){

        for(Date d: motions.keySet())
        {
           Motion m = motions.get(d);

            //acceleration vector by taking the euclidean magnitude of the three individual
            double v = Math.sqrt(m.getX()*m.getX())+(m.getY()*m.getY())+(m.getZ()*m.getZ());
        }


    }

}
