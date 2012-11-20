package org.daum.library.android.sitac.controller;

import java.util.Hashtable;

import org.daum.library.android.sitac.engine.IEngine;
import org.daum.library.android.sitac.view.SITACMapView;
import org.daum.library.android.sitac.view.SITACMenuLayout;
import org.daum.library.android.sitac.view.SITACSelectedEntityView;
import org.daum.library.android.sitac.view.entity.EntityFactory;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.entity.IEntityFactory;
import org.daum.library.android.sitac.view.entity.IModelFactory;
import org.daum.library.android.sitac.view.entity.ModelFactory;
import org.osmdroid.util.GeoPoint;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import org.daum.common.genmodel.*;

public class SITACController implements ISITACController {
	
	private static final String TAG = "SITACController";
	private static final boolean D = true;
	
	private Context ctx;
	private SITACMapView mapView;
    private UIHandler uiHandler;
    private EngineHandler engineHandler;
	private LocationManager locMgr;
	private IEntityFactory entityFactory;
	private IModelFactory modelFactory;
	private Hashtable<IEntity, IModel> modelMap;
	
	public SITACController(Context context) {
		this.ctx = context;
		this.entityFactory = new EntityFactory(ctx);
		this.modelFactory = new ModelFactory();
		this.locMgr = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		
		this.modelMap = new Hashtable<IEntity, IModel>();

        // delegate the engine updates to the engineHandler
        this.engineHandler = new EngineHandler(entityFactory, modelMap);

        // delegate the UI events to the UIHandler
        this.uiHandler = new UIHandler(modelFactory, modelMap);
	}

    @Override
    public void setEngine(IEngine engine) {
        engine.setHandler(engineHandler);
        uiHandler.setEngine(engine);
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
	
	public void registerMenuView(SITACMenuLayout menuView) {
		engineHandler.registerMenuView(menuView);
	}
	
	public void registerSelectedEntityView(SITACSelectedEntityView view) {
		uiHandler.registerSelectedEntityView(view);
	}

    @Override
	public void setEntityFactory(IEntityFactory factory) {
		if (factory != null) this.entityFactory = factory;
	}

    @Override
	public void setModelFactory(IModelFactory factory) {
		if (factory != null) this.modelFactory = factory;
	}

    @Override
    public void setMapProvider(String url) {
        mapView.setMapProvider(url);
    }

    public UIHandler getUIHandler() {
		return uiHandler;
	}
}
