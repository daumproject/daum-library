package org.daum.library.android.moyens;

import android.util.Log;
import org.daum.library.android.moyens.model.Demand;
import org.daum.library.android.moyens.view.MoyensView;
import org.daum.library.android.moyens.view.listener.IMoyensListener;
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
        @ProvidedPort(name = "inDemand", type = PortType.MESSAGE)
})
@Requires({
        @RequiredPort(name = "outDemand", type = PortType.MESSAGE)
})
@ComponentType
public class MoyensComponent extends AbstractComponentType implements IMoyensListener {

    // Debugging
    private static final String TAG = "MoyensComponent";
    private static boolean D = true;

    // String constants
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
                    moyensView.addEventListener(MoyensComponent.this);
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

    @Port(name="inDemand")
    public void demandIncoming(final Object inResource) {
        if (D) Log.i(TAG, "BEGIN demandIncoming");
        if (inResource instanceof Demand) {
            moyensView.addDemand((Demand) inResource);

        } else {
            Log.w(TAG, "Cannot handle received object on port \"inDemand\". Received object: "+inResource.getClass().getSimpleName());
        }
        if (D) Log.i(TAG, "END demandIncoming");
    }

    @Override
    public void onDemandAsked(Demand newDemand) {
        getPortByName("outDemand", MessagePort.class).process(newDemand);
        moyensView.addDemand(newDemand);
        if (D) Log.i("Moyens asked: ", newDemand.toString());
    }
}
