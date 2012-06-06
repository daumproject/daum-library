package org.daum.library.android.moyens;

import android.util.Log;
import org.daum.library.android.moyens.model.IResource;
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
        @ProvidedPort(name = "inResource", type = PortType.MESSAGE)
})
@Requires({
        @RequiredPort(name = "outResourceDemand", type = PortType.MESSAGE)
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
            uiService.getRootActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    moyensView = new MoyensView(uiService.getRootActivity());
                    uiService.addToGroup(TAB_NAME, moyensView);
                }
            });
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

    @Port(name="inResource")
    public void resourcesIncoming(final Object inResource) {
        if (D) Log.i(TAG, "BEGIN resourceIncoming");
        if (inResource instanceof IResource) {
            moyensView.addResource((IResource) inResource);

        } else {
            Log.w(TAG, "Cannot handle received object on port \"inResources\". Received object: "+inResource.getClass().getSimpleName());
        }
        if (D) Log.i(TAG, "END resourceIncoming");
    }
}
