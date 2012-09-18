package org.daum.library.android.sitac;

import android.util.Log;
import org.daum.library.android.sitac.controller.ISITACController;
import org.daum.library.android.sitac.engine.IEngine;
import org.daum.library.android.sitac.view.SITACView;
import org.daum.library.ormH.store.ReplicaStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
import org.kevoree.ContainerRoot;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.KevoreeAndroidService;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 08/06/12
 * Time: 09:36
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Android")
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = false)
})
@Provides({
        @ProvidedPort(name = "notify", type = PortType.MESSAGE)
})
@DictionaryType({
        @DictionaryAttribute(name = "interNum", optional = false),
        @DictionaryAttribute(name = "mapProvider", defaultValue = "http://tile.openstreetmap.org/", optional = false)
})
@ComponentType
public class SITACComponent extends AbstractComponentType {

    private static final String TAG = "SITACComponent";

    private static final String TAB_NAME = "SITAC";

    private KevoreeAndroidService uiService;
    private static ChangeListener singleton=null;
    private SITACView view;
    private ISITACController controller;
    private IEngine engine;

    public static ChangeListener getChangeListenerInstance() {
        if (singleton == null) singleton = new ChangeListener();
        return singleton;
    }


    @Start
    public void start() {
        uiService = UIServiceHandler.getUIService();

        initUI();

        getModelService().registerModelListener(new ModelListener() {
            @Override
            public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public boolean afterLocalUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void modelUpdated() {
                uiService.getRootActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String mapProvider = getDictionary().get("mapProvider").toString();
                            String interNum = getDictionary().get("interNum").toString();

                            ReplicaService replicatingService = getPortByName("service", ReplicaService.class);
                            ReplicaStore store = new ReplicaStore(replicatingService);

                            engine = new SITACEngine(getNodeName(), store);
                            controller.setEngine(engine);
                            controller.setMapProvider(mapProvider);

                        } catch (PersistenceException e) {
                            Log.e(TAG, "Error on component startup", e);
                        }
                    }
                });
            }
        });
    }

    private void initUI() {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view = new SITACView(uiService.getRootActivity());
                controller = view.getController();
                uiService.addToGroup(TAB_NAME, view);
            }
        });
    }

    @Stop
    public void stop() {
        uiService.remove(view);
    }

    @Update
    public void update() {

//        stop();
//        start();
    }

    @Port(name = "notify")
    public void notifiedByReplica(final Object m) {
        uiService.getRootActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SITACComponent.getChangeListenerInstance().receive(m);
            }
        });
    }
}
