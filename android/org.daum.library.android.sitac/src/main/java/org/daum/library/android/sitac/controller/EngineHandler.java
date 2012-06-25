package org.daum.library.android.sitac.controller;

import java.util.Hashtable;

import org.daum.common.model.api.Demand;
import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;
import org.daum.library.android.sitac.view.DrawableFactory;
import org.daum.library.android.sitac.view.SITACMapView;
import org.daum.library.android.sitac.view.SITACMenuView;
import org.daum.library.android.sitac.view.entity.DemandEntity;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.entity.IEntityFactory;

import android.util.Log;

/**
 * Handle engine's events and updates views accordingly
 * This class should be used as a delegate of the SITACController
 * @author max
 *
 */
public class EngineHandler implements OnEngineStateChangeListener {
	
	private static final String TAG = EngineHandler.class.getSimpleName();
	private static final boolean D = true;

	private SITACMapView mapView;
	private SITACMenuView menuView;
	private IEntityFactory factory;
	private Hashtable<IEntity, IModel> modelMap;
	
	public EngineHandler(IEntityFactory factory, Hashtable<IEntity, IModel> modelMap) {
		this.factory = factory;
		this.modelMap = modelMap;
	}
	
	@Override
	public void onAdd(IModel m, IEntity e) {
		if (D) Log.i(TAG, "onAdd(): "+m);
		if (e == null) e = factory.build(m);
		
		modelMap.put(e, m);
		
		// the model is now saved, so we should update the associated entity state
		e.setState(IEntity.State.SAVED);
		
		if (e.getGeoPoint() == null) {
			// the entity has no location, add it to the noLocationMenu
			menuView.addEntityWithNoLocation((DemandEntity) e);
		}
		
		//add it to the map
		mapView.addEntity(e);
	}
	
	@Override
	public void onUpdate(IModel m, IEntity e) {
		if (D) Log.i(TAG, "onUpdate(): "+m);

        if (m instanceof Demand) {
            Demand d = (Demand) m;
            // change drawable according to Demand state
            if (d.getGh_engage() != null && d.getGh_desengagement() == null) {
                // vehicle is supposed to be arrived in da place
                if (modelMap.containsKey(e)) {
                    modelMap.remove(e);
                    mapView.deleteEntity(e);
                }
                e = factory.build(m);
                modelMap.put(e, m);
                mapView.addEntity(e);

            } else if (d.getGh_desengagement() != null) {
                // vehicle has left, remove it from the map
                mapView.deleteEntity(e);
                modelMap.remove(e);
            }
        }
	}
	
	@Override
	public void onDelete(IEntity e) {
		mapView.deleteEntity(e);
		modelMap.remove(e);
	}

    public void registerMenuView(SITACMenuView menuView) {
		this.menuView = menuView;
	}

	public void registerMapView(SITACMapView mapView) {
		this.mapView = mapView;
	}
}
