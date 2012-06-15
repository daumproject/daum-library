package org.daum.library.android.sitac.controller;

import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.Target;
import org.daum.library.android.sitac.command.AddDangerCommand;
import org.daum.library.android.sitac.command.AddDemandCommand;
import org.daum.library.android.sitac.command.AddTargetCommand;
import org.daum.library.android.sitac.command.UpdateDemandCommand;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;
import org.daum.library.android.sitac.listener.OnMenuViewEventListener;
import org.daum.library.android.sitac.listener.OnOverlayEventListener;
import org.daum.library.android.sitac.listener.OnSelectedEntityEventListener;
import org.daum.library.android.sitac.model.SITACEngine;
import org.daum.library.android.sitac.view.SITACMapView;
import org.daum.library.android.sitac.view.SITACMenuView;
import org.daum.library.android.sitac.view.SITACSelectedEntityView;
import org.daum.library.android.sitac.view.entity.ArrowEntity;
import org.daum.library.android.sitac.view.entity.DangerEntity;
import org.daum.library.android.sitac.view.entity.DemandEntity;
import org.daum.library.android.sitac.view.entity.EntityFactory;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.entity.IEntityFactory;
import org.daum.library.android.sitac.view.entity.TargetEntity;
import org.daum.library.android.sitac.view.entity.ZoneEntity;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class SITACController implements OnOverlayEventListener, OnSelectedEntityEventListener, OnMenuViewEventListener, OnEngineStateChangeListener {
	
	private static final String TAG = "SITACController";
	private static final boolean D = true;
	
	private static SITACController instance;
	
	private Context ctx;
	private SITACMapView mapView;
	private SITACMenuView menuView;
	private SITACSelectedEntityView selectedEntityView;
	private SITACEngine engine;
	private LocationManager locMgr;
	private IEntity selectedEntity;
	private IEntityFactory factory;
	
	private SITACController(Context context) {
		this.ctx = context;
		this.factory = new EntityFactory(ctx);
		this.locMgr = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		
		this.engine = new SITACEngine();
		this.engine.setOnEngineStateChangeListener(this);
	}
	
	public static SITACController getInstance(Context ctx) {
		if (instance == null) {
			instance = new SITACController(ctx);
		}
		return instance;
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
	public void onDemandUpdated(Demand d) {
		if (D) Log.i(TAG, "onDemandUpdated(): "+d);
		DemandEntity e = factory.buildEntity(d);
		e.setState(IEntity.STATE_EDITED);
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
	public void onMenuItemSelected(IEntity entity) {
		if (selectedEntityView != null) {
			// updating the selectedEntityView
			selectedEntity = entity;
			selectedEntityView.updateView(entity.getIcon(), selectedEntity.getMessage());
			selectedEntityView.setState(SITACSelectedEntityView.STATE_SELECTION);
			selectedEntityView.show();
		}
	}
	
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e, MapView mapV) {
		if (mapView != null && selectedEntity != null) {
			// get the entity location on the map
			Projection prj = mapV.getProjection();
			IGeoPoint geoP = prj.fromPixels(e.getX(), e.getY());
			selectedEntity.setGeoPoint(new GeoPoint(geoP.getLatitudeE6(), geoP.getLongitudeE6()));
			
			// if it's a new entity, add it to the engine
			if (selectedEntity.getState() == IEntity.STATE_NEW) {
				if (selectedEntity instanceof DemandEntity) {
					Demand d = factory.buildDemand((DemandEntity) selectedEntity);
					CmdManager.getInstance(engine).execute(AddDemandCommand.class, d);
					
				} else if (selectedEntity instanceof DangerEntity) {
					Danger d = factory.buildDanger((DangerEntity) selectedEntity);
					CmdManager.getInstance(engine).execute(AddDangerCommand.class, d);
					
				} else if (selectedEntity instanceof TargetEntity) {
					Target t = factory.buildTarget((TargetEntity) selectedEntity);
					CmdManager.getInstance(engine).execute(AddTargetCommand.class, t);
					
				} else if (selectedEntity instanceof ArrowEntity) {
//					LineAction la = factory.buildDanger((ArrowEntity) selectedEntity);
//					CmdManager.getInstance(engine).execute(AddLineActionCommand.class, la);
					
				} else if (selectedEntity instanceof ZoneEntity) {
//					ZoneAction za = factory.buildDanger((ZoneEntity) selectedEntity);
//					CmdManager.getInstance(engine).execute(AddZoneActionCommand.class, za);
				}
				
			// if the state is EDITED, then update the corresping data in the engine
			} else if (selectedEntity.getState() == IEntity.STATE_EDITED) {
				if (selectedEntity instanceof DemandEntity) {
					Demand d = factory.buildDemand((DemandEntity) selectedEntity);
					CmdManager.getInstance(engine).execute(UpdateDemandCommand.class, d);
					
				} else if (selectedEntity instanceof DangerEntity) {
//					Danger d = factory.buildDanger((DangerEntity) selectedEntity);
//					CmdManager.getInstance(engine).execute(UpdateDangerCommand.class, d);
					
				} else if (selectedEntity instanceof TargetEntity) {
//					Target t = factory.buildTarget((TargetEntity) selectedEntity);
//					CmdManager.getInstance(engine).execute(UpdateTargetCommand.class, t);
					
				} else if (selectedEntity instanceof ArrowEntity) {
//					LineAction la = factory.buildDanger((ArrowEntity) selectedEntity);
//					CmdManager.getInstance(engine).execute(UpdateLineActionCommand.class, la);
					
				} else if (selectedEntity instanceof ZoneEntity) {
//					ZoneAction za = factory.buildDanger((ZoneEntity) selectedEntity);
//					CmdManager.getInstance(engine).execute(UpdateZoneActionCommand.class, za);
				}
			}
			
			// if it's an arrowEntity that has more than one point drawn
			if (selectedEntity instanceof ArrowEntity) {
				((ArrowEntity) selectedEntity).addPoint(geoP);
				if (((ArrowEntity) selectedEntity).hasOneLine()) {
					// display the "terminate action" button
					selectedEntityView.setState(SITACSelectedEntityView.STATE_SELECTION_CONFIRM);
					selectedEntityView.show();
				}
				// consume the event
				return true;
			}
			
			// the entity has been moved, we can hide the selectedEntityView
			selectedEntityView.hide();
			selectedEntity = null;
			return true;
		}
		return false;
	}
	
	@Override
	public void onEntitySelected(IEntity e) {
		if (mapView != null && selectedEntityView != null) {
			// updating the selectedEntityView
			selectedEntity = e;
			selectedEntityView.updateView(selectedEntity.getIcon(), selectedEntity.getMessage());
			selectedEntityView.setState(SITACSelectedEntityView.STATE_DELETION);
			selectedEntityView.show();
		}
	}
	
	@Override
	public void onSelectedEntityButtonClicked() {
		if (mapView != null && selectedEntityView != null) {
			if (selectedEntityView.getState() == SITACSelectedEntityView.STATE_DELETION) {
				mapView.deleteEntity(selectedEntity);
			}
			// hiding the selectedEntityView
			selectedEntityView.hide();
			selectedEntity = null;
		}
	}

	/**
	 * The map will be centered automatically to the device current position
	 * 
	 * @param minTime
	 *            the minimum time interval for notifications, in milliseconds.
	 *            This field is only used as a hint to conserve power, and
	 *            actual time between location updates may be greater or lesser
	 *            than this value.
	 * @param minDistance
	 *            the minimum distance interval for notifications, in meters
	 */
	public void requestLocationUpdates(long minTime, long minDistance) {
		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
				minDistance, locationListener);
	}
	
	/**
	 * Removes the registration for location updates of the current SITACView.
	 * Following this call, updates will no longer occur for the map.
	 */
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
	}
	
	public void registerMenuView(SITACMenuView menuView) {
		this.menuView = menuView;
	}
	
	public void registerSelectedEntityView(SITACSelectedEntityView view) {
		this.selectedEntityView = view;
	}
	
	public SITACEngine getEngine() {
		return engine;
	}
	
	public void setEntityFactory(IEntityFactory factory) {
		this.factory = factory;
	}
}
