package org.daum.library.android.moyens;

import android.util.Log;
import org.daum.library.android.moyens.model.ResourcesList;
import org.daum.library.android.moyens.view.MoyensView;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;


/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 15/05/12
 * Time: 14:59
 */



@Library(name = "Android")
@Provides({
        @ProvidedPort(name = "inResources", type = PortType.MESSAGE)
})
@Requires({
        @RequiredPort(name = "outResourceDemands", type = PortType.MESSAGE, optional = true)
})
@ComponentType
public class MoyensComponent extends AbstractComponentType {

    // Debugging
    private static final String TAG = "MoyensComponent";
    private static boolean D = true;

    private static final String TAB_NAME = "Moyens";

    private KevoreeAndroidService uiService;
    private MoyensView moyensView;

    @Start
    public void start() {
        if (D) Log.i(TAG, "BEGIN start");
        this.uiService = UIServiceHandler.getUIService();
        initUI();
        if (D) Log.i(TAG, "END start");
    }

    private void initUI() {
        try {
            moyensView = new MoyensView(uiService.getRootActivity());
            uiService.addToGroup(TAB_NAME, moyensView);
        } catch (Exception e) {
            Log.e(TAG, "Creating view failed", e);
        }
    }

    @Stop
    public void stop() {
        uiService.remove(moyensView);
    }

    @Update
    public void update() {
        stop();
        start();
    }

    @Port(name="inResources")
    public void resourcesIncoming(final Object inResources) {
        if (D) Log.i(TAG, "BEGIN resourcesIncoming");
        if (inResources instanceof ResourcesList) {

        } else {
            Log.w(TAG, "Cannot handle received object on port \"inResources\". Received object: "+inResources.getClass().getSimpleName());
        }
        if (D) Log.i(TAG, "END resourcesIncoming");
    }
}
