package org.daum.library.android.sitac;

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
@ComponentType
public class SITACComponent extends AbstractComponentType {

    private KevoreeAndroidService uiService;

    @Start
    public void start() {
        uiService = UIServiceHandler.getUIService();
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SITACView sitacView = new SITACView(uiService.getRootActivity());
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
}
