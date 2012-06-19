package org.daum.library.android.sitac.controller;

import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.command.AddModelCommand;
import org.daum.library.android.sitac.model.SITACEngine;
import org.daum.library.android.sitac.view.SITACMapView;
import org.daum.library.android.sitac.view.SITACMenuView;
import org.daum.library.android.sitac.view.SITACSelectedEntityView;
import org.daum.library.android.sitac.view.entity.EntityFactory;
import org.daum.library.android.sitac.view.entity.IEntityFactory;
import org.daum.library.android.sitac.view.entity.IModelFactory;
import org.daum.library.android.sitac.view.entity.ModelFactory;
import org.osmdroid.util.GeoPoint;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class SITACController implements ISITACController {
	
	private static final String TAG = "SITACController";
	private static final boolean D = true;
	
	private static SITACController instance;
	
	private Context ctx;
	private SITACMapView mapView;
    private SITACEngine engine;
    private UIHandler uiHandler;
    private EngineHandler engineHandler;
	private LocationManager locMgr;
	private IEntityFactory entityFactory;
	private IModelFactory modelFactory;
	
	private SITACController(Context context) {
		this.ctx = context;
		this.entityFactory = new EntityFactory(ctx);
		this.modelFactory = new ModelFactory();
		this.locMgr = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

		// delegate the engine updates to the engineHandler
		this.engineHandler = new EngineHandler(entityFactory);
		
		this.engine = new SITACEngine(engineHandler);
		
		// delegate the UI events to the UIHandler
		this.uiHandler = new UIHandler(modelFactory, engine);
	}
	
	public static SITACController getInstance(Context ctx) {
		if (instance == null) instance = new SITACController(ctx);
		return instance;
	}

    @Override
    public void addModel(IModel m) {
        CmdManager.getInstance(engine).execute(AddModelCommand.class, m);
    }

    @Override
	public void requestLocationUpdates(long minTime, long minDistance) {
		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
				minDistance, locationListener);
	}
	
    @Override
	public void removeLocationUpdates() {
		locMgr.removeUpdates(locationListener);
	}
	
	private final LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
		@Override
		public void onProviderEnabled(String provider) {}
		
		@Override
		public void onProviderDisabled(String provider) {}
		
		@Override
		public void onLocationChanged(Location location) {
			mapView.setCenter(new GeoPoint(location));
		}
	};

	public void registerMapView(SITACMapView mapView) {
		this.mapView = mapView;
		uiHandler.registerMapView(mapView);
		engineHandler.registerMapView(mapView);
	}
	
	public void registerMenuView(SITACMenuView menuView) {
		engineHandler.registerMenuView(menuView);
	}
	
	public void registerSelectedEntityView(SITACSelectedEntityView view) {
		uiHandler.registerSelectedEntityView(view);
	}
	
	public void setEntityFactory(IEntityFactory factory) {
		this.entityFactory = factory;
	}
	
	public void setModelFactory(IModelFactory factory) {
		this.modelFactory = factory;
	}
	
	public UIHandler getUIHandler() {
		return uiHandler;
	}
}
