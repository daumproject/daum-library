package org.daum.library.android.daumauth;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.daum.library.android.daumauth.controller.IController;
import org.daum.library.android.daumauth.listener.OnConnectionListener;
import org.daum.library.android.daumauth.listener.OnInterventionSelectedListener;
import org.daum.library.android.daumauth.util.GenerateModelHelper;
import org.daum.library.android.daumauth.view.DaumAuthView;

import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.android.daumauth.DaumAuthEngine.OnStoreSyncedListener;

import org.kevoree.*;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.DictionaryAttribute;
import org.kevoree.annotation.DictionaryType;
import org.kevoree.annotation.Port;
import org.kevoree.annotation.PortType;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.api.service.core.script.KevScriptEngine;
import org.kevoree.framework.AbstractComponentType;

import org.daum.common.genmodel.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import scala.Option;

import java.util.concurrent.Semaphore;


/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/07/12
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Android")
@Requires({
        @RequiredPort(name = "service", type = PortType.MESSAGE)
})

@DictionaryType({
        @DictionaryAttribute(name = "distance", defaultValue = "0", optional = false)
})
@ComponentType
public class FollowMe extends AbstractComponentType    {

    private static final String TAG = "FollowMe";
    private static final Logger logger = LoggerFactory.getLogger(FollowMe.class);

    private KevoreeAndroidService uiService;

    @Start
    public void start() {
        uiService = UIServiceHandler.getUIService();


    }



    @Stop
    public void stop() {
        Log.w(TAG, "COMP method: stop()");
    //    uiService.remove(view);
    }

    @Update
    public void update() {
        Log.w(TAG, "COMP method: update()");

        stop();
        start();
    }

}
