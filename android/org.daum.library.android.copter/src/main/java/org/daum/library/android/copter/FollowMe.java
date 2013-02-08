package org.daum.library.android.copter;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/07/12
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Android")
@Requires({
        @RequiredPort(name = "followme", type = org.kevoree.annotation.PortType.MESSAGE)
})

@DictionaryType({
        @DictionaryAttribute(name = "userid",  optional = false) ,
        @DictionaryAttribute(name = "enable", defaultValue = "false", optional = false, vals = {"true","false"}) ,
        @DictionaryAttribute(name = "safety_distance", defaultValue = "0", optional = false) ,
        @DictionaryAttribute(name = "altitude", defaultValue = "10", optional = false)
})
@ComponentType
public class FollowMe extends AbstractComponentType {

    private static final String TAG = "FollowMe";
    private static final Logger logger = LoggerFactory.getLogger(FollowMe.class);

    private KevoreeAndroidService uiService;
    private LocationManager locationMgr	= null;
    private LocationListener onLocationChange=null;


    public String addJson(String id,Object value){
          return "\"id\""+":"+"\""+value+"\"";
    }

    @Start
    public void start() {
        uiService = UIServiceHandler.getUIService();


        onLocationChange	= new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                double lat =    location.getLatitude();
                double lon =   location.getLongitude();
                float accuracy = location.getAccuracy();
                float altitude = Float.parseFloat(getDictionary().get("altitude").toString());
                float safety_distance = Float.parseFloat(getDictionary().get("safety_distance").toString());

                try
                {
                    if(Boolean.parseBoolean(getDictionary().get("enable").toString()))
                    {
                        String json = "{"+addJson("lat",lat)+addJson("lon",lon)+addJson("accuracy",accuracy)+addJson("altitude",altitude)+addJson("safety_distance",safety_distance)+"}";

                        //{ "firstName":"John" , "lastName":"Doe" }
                        getPortByName("followme", MessagePort.class).process(json);
                    }
                }   catch (Exception e)
                {
                    logger.error("",e);
                }


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        }  ;


        locationMgr = (LocationManager) uiService.getRootActivity().getSystemService(Context.LOCATION_SERVICE);
        //requestLocationUpdates(String provider, long minTime, float minDistance, PendingIntent intent)
        locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1, onLocationChange);
        locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500,0, onLocationChange);


    }



    @Stop
    public void stop() {

        //    uiService.remove(view);
    }

    @Update
    public void update() {


        stop();
        start();
    }

}
