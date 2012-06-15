package org.daum.library.android.sitac.controller;

import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.Target;
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
	public void onDemandAdded(Demand d) {
		if (D) Log.i(TAG, "onDemandAdded(): "+d);
		DemandEntity e = factory.buildEntity(d);
		e.setState(IEntity.STATE_EDITED);
		if (e.getGeoPoint() == null) {
			// the entity has no location, add it to the menu
			menuView.addEntityWithNoLocation(e);
		} else {
			// the entity has a location, add it to the map
			mapView.addEntity(e);
		}
	}

	@Override
	public void onDangerAdded(Danger d) {
		if (D) Log.i(TAG, "onDangerAdded()"+d);
		IEntity e = factory.buildEntity(d);
		mapView.addEntity(e);
		e.setState(IEntity.STATE_EDITED);
	}

	@Override
	public void onTargetAdded(Target t) {
		if (D) Log.i(TAG, "onTargetAdded()"+t);
		IEntity e = factory.buildEntity(t);
		mapView.addEntity(e);
		e.setState(IEntity.STATE_EDITED);
	}

	@Override
	public void onDemandUpdated(Demand d) {
		if (D) Log.i(TAG, "onDemandUpdated(): "+d);
		DemandEntity e = factory.buildEntity(d);
		mapView.addEntity(e);
		e.setState(IEntity.STATE_EDITED);
	}

	public void registerMenuView(SITACMenuView menuView) {
		this.menuView = menuView;
		
	}

	public void registerMapView(SITACMapView mapView) {
		this.mapView = mapView;
		
	}

}
