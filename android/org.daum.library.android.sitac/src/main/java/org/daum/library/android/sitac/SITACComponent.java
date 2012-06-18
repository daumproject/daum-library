package org.daum.library.android.sitac;

import android.util.Log;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.VehicleType;
import org.daum.library.android.sitac.controller.ISITACController;
import org.daum.library.android.sitac.view.SITACView;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 08/06/12
 * Time: 09:36
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Android")
@Provides({
        @ProvidedPort(name = "inDemand", type = PortType.MESSAGE)
})
@Requires({
        @RequiredPort(name = "outDemand", type = PortType.MESSAGE)
})
@ComponentType
public class SITACComponent extends AbstractComponentType {

    private static final String TAG = "STIACComponent";

    private KevoreeAndroidService uiService;
    private ISITACController sitacCtrl;

    @Start
    public void start() {
        uiService = UIServiceHandler.getUIService();
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SITACView sitacView = new SITACView(uiService.getRootActivity());
                sitacCtrl = sitacView.getController();
                uiService.addToGroup("SITAC", sitacView);
            }
        });
    }

    @Stop
    public void stop() {

    }

    @Update
    public void update() {
        stop();
        start();
    }

    @Port(name = "inDemand")
    public void demandReceived(final Object newDemand) {
        if (newDemand instanceof Demand) {
            uiService.getRootActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sitacCtrl.addDemand((Demand) newDemand);
                }
            });
        } else {
            Log.w(TAG, "Object type can't be handled (inDemand supposed to be a Demand object)");
        }
    }
}
