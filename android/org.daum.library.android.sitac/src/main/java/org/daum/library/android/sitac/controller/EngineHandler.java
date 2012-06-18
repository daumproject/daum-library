package org.daum.library.android.sitac.controller;

import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;
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
	
	public EngineHandler(IEntityFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void onAdd(IModel m) {
		if (D) Log.i(TAG, "onAdd(): "+m);
		IEntity e = factory.build(m);
		e.setState(IEntity.STATE_EDITED);
		
		if (e instanceof DemandEntity) {
			if (e.getGeoPoint() == null) {
				// the entity has no location, add it to the menu
				menuView.addEntityWithNoLocation((DemandEntity) e);
				return;
			}
		}
		
		// the entity has a location, add it to the map
		mapView.addEntity(e);
	}
	
	@Override
	public void onUpdate(IModel m) {
		if (D) Log.i(TAG, "onUpdate(): "+m);
		IEntity e = factory.build(m);
		e.setState(IEntity.STATE_EDITED);
		mapView.addEntity(e);
	}
	
	@Override
	public void onDelete(IModel m) {
		// TODO
		if (D) Log.i(TAG, "onDelete(): "+m);
	}

    public void registerMenuView(SITACMenuView menuView) {
		this.menuView = menuView;
	}

	public void registerMapView(SITACMapView mapView) {
		this.mapView = mapView;
	}
}
