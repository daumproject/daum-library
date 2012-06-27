package org.daum.library.android.sitac.controller;

import java.util.*;

import org.daum.common.gps.api.IGpsPoint;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;
import org.daum.library.android.sitac.view.SITACMapView;
import org.daum.library.android.sitac.view.SITACMenuView;
import org.daum.library.android.sitac.view.entity.DemandEntity;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.entity.IEntityFactory;

import android.util.Log;
import org.daum.library.android.sitac.visitor.EntityUpdateVisitor;
import org.daum.library.android.sitac.visitor.IVIsitor;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

/**
 * Handle engine's events and updates views accordingly
 * This class should be used as a delegate of the SITACController
 * @author max
 *
 */
public class EngineHandler implements OnEngineStateChangeListener {
	
	private static final String TAG = EngineHandler.class.getSimpleName();

	private SITACMapView mapView;
	private SITACMenuView menuView;
	private IEntityFactory factory;
    private IVIsitor visitor;
	private Map<IEntity, IModel> modelMap;
	
	public EngineHandler(IEntityFactory factory, Hashtable<IEntity, IModel> modelMap) {
		this.factory = factory;
		this.modelMap = Collections.synchronizedMap(modelMap);
        this.visitor = new EntityUpdateVisitor();
	}

    @Override
    public void onLocalAdd(IModel m, IEntity e) {
        Log.d(TAG, "onLocalAdd(IModel, IEntity)");
        for (IModel model : modelMap.values()) {
            if (model.getId().equals(m.getId()))  {
                add(m, e);
                update(m, e);
                return;
            }
        }
        add(m, e);
    }

    @Override
    public void onLocalUpdate(IModel m, IEntity e) {
        Log.d(TAG, "onLocalUpdate(IModel, IEntity)");
        for (IModel model : modelMap.values()) {
            if (model.getId().equals(m.getId()))  {
                update(m, e);
                return;
            }
        }
        add(m, e);
    }

    @Override
    public void onLocalDelete(IModel m, IEntity e) {
        Log.d(TAG, "onLocalDelete(IModel, IEntity)");
         delete(e);
    }

    @Override
    public void onRemoteAdd(IModel m) {
        Log.d(TAG, "onRemoteAdd(IModel)");
        for (IModel model : modelMap.values()) {
            if (model.getId().equals(m.getId()))  {
                update(m);
                return;
            }
        }
        add(m);
    }

    @Override
    public void onRemoteUpdate(IModel m) {
        Log.d(TAG, "onRemoteUpdate(IModel) "+m);
        for (IModel model : modelMap.values()) {
            if (model.getId().equals(m.getId()))  {
                update(m);
                return;
            }
        }
        add(m);
    }

    @Override
    public void onRemoteDelete(IModel m) {
        Log.d(TAG, "onRemoteDelete(IModel)");
        delete(getEntityWithModel(m));
    }

    @Override
    public void onReplicaSynced(ArrayList<IModel> data) {
        Log.d(TAG, "onReplicaSynced(ArrayList<IModel>)");
        for (IModel m : data) {
            for (IModel model : modelMap.values()) {
                if (model.getId().equals(m.getId()))  {
                    update(m);
                    break;
                }
            }
            add(m);
        }
    }

    private void add(IModel m) {
        Log.d(TAG, "add(IModel)");
        IEntity e = factory.build(m);
        add(m, e);
    }

    private void add(IModel m, IEntity e) {
        Log.d(TAG, "add(IModel, IEntity)");
        modelMap.put(e, m);
        e.setState(IEntity.State.SAVED);
        add(e);
    }

    private void add(IEntity e) {
        Log.d(TAG, "add(IEntity)");
        if (e.getGeoPoint() == null) {
            // in case the entity has no location on map, add it to the menu
            menuView.addEntityWithNoLocation((DemandEntity) e);
        }
        mapView.addEntity(e);

        Log.d(TAG, ">>> entity >> "+e);
        for (IModel model : modelMap.values()) {
            Log.d(TAG, ">>> map >> "+model);
        }
    }

    private void update(IModel m, IEntity e) {
        Log.d(TAG, "update(IModel, IEntity)");
        modelMap.put(e, m); // update model in modelMap
        e.accept(visitor, m);

        Log.d(TAG, ">>> entity >> "+e);
        for (IModel model : modelMap.values()) {
            Log.d(TAG, ">>> map >> "+model);
        }
    }

    private void update(IModel m) {
        Log.d(TAG, "update(IModel)");
        IEntity e = getEntityWithModel(m);
        modelMap.put(e, m); // update model in modelMap
        e.accept(visitor, m);

        Log.d(TAG, ">>> entity >> "+e);
        for (IModel model : modelMap.values()) {
            Log.d(TAG, ">>> map >> "+model);
        }
    }

    private void delete(IEntity e) {
        Log.d(TAG, "delete(IEntity)");
        modelMap.remove(e);
        mapView.deleteEntity(e);
    }

    /**
     * Returns the IEntity associated with the IModel in modelMap and
     * replace the old IModel value with the one given in parameter.
     * To find the IModel object it will search based on IModel ids.
     *
     * @param m model you want to find the entity associated with
     * @return IEntity associated with the model
     */
    private IEntity getEntityWithModel(IModel m) {
        Set<Map.Entry<IEntity, IModel>> entries = modelMap.entrySet();
        synchronized (modelMap) {
            for (Map.Entry<IEntity, IModel> entry: entries) {
                if (entry.getValue().getId().equals(m.getId())) {
                    entry.setValue(m); // update model
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public void registerMenuView(SITACMenuView menuView) {
		this.menuView = menuView;
	}

	public void registerMapView(SITACMapView mapView) {
		this.mapView = mapView;
	}
}
