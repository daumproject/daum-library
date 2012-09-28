package org.daum.library.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import org.daum.common.genmodel.GpsPoint;
import org.daum.common.genmodel.Motion;
import org.daum.common.genmodel.SitacFactory;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/09/12
 * Time: 14:09
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Android")
@Requires({
        @RequiredPort(name = "motion", type = PortType.MESSAGE,optional = true)
})
@ComponentType
public class MotionSensor  extends AbstractComponentType   {


    private SensorManager mSensorManager=null;
    private Sensor mSensor=null;
    private   SensorEventListener mSensorListener=null;
    @Start
    public void start()
    {

        mSensorManager = (SensorManager) UIServiceHandler.getUIService().getRootActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorListener = new SensorEventListener()
        {
            public void onSensorChanged(SensorEvent se)
            {
                float x = se.values[0];
                float y = se.values[1];
                float z = se.values[2];
                Motion motion  = new Motion();
                motion.setX(x);
                motion.setY(y);
                motion.setZ(z);
                getPortByName("motion", MessagePort.class).process(motion);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }


        };
        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);


    }
    @Stop
    public void stop()
    {
        if(mSensorListener != null && mSensorManager !=null)
            mSensorManager.unregisterListener(mSensorListener);
    }

    @Update
    public void update()
    {

    }







}



