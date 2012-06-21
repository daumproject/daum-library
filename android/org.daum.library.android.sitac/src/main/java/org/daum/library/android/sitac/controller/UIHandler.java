package org.daum.library.android.sitac.controller;

import java.util.Hashtable;

import org.daum.common.gps.impl.GpsPoint;
import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.command.AddModelCommand;
import org.daum.library.android.sitac.command.DeleteModelCommand;
import org.daum.library.android.sitac.command.UpdateModelCommand;
import org.daum.library.android.sitac.listener.OnMenuViewEventListener;
import org.daum.library.android.sitac.listener.OnOverlayEventListener;
import org.daum.library.android.sitac.listener.OnSelectedEntityEventListener;
import org.daum.library.android.sitac.model.SITACEngine;
import org.daum.library.android.sitac.view.SITACMapView;
import org.daum.library.android.sitac.view.SITACMenuView;
import org.daum.library.android.sitac.view.SITACSelectedEntityView;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.entity.IModelFactory;
import org.daum.library.android.sitac.view.entity.IShapedEntity;
import org.daum.library.android.sitac.view.entity.ShapedEntity;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;

import android.view.MotionEvent;

public class UIHandler implements OnOverlayEventListener, OnSelectedEntityEventListener, OnMenuViewEventListener {
	
	private static final String TAG = "UIHandler";
	
	private SITACMapView mapView;
	private SITACSelectedEntityView selectedEntityView;
	private IEntity selectedEntity;
	private IModelFactory factory;
	private SITACEngine engine;
	private Hashtable<IEntity, IModel> modelMap;
	
	public UIHandler(IModelFactory factory, SITACEngine engine, Hashtable<IEntity, IModel> modelMap) {
		this.factory = factory;
		this.engine = engine;
		this.modelMap = modelMap;
	}
	
	@Override
	public void onMenuItemSelected(IEntity entity) {
		if (selectedEntityView != null) {
			// updating the selectedEntityView
			selectedEntity = entity;
			String txt = selectedEntity.getType()+selectedEntity.getMessage();
			selectedEntityView.updateView(entity.getIcon(), txt);
			selectedEntityView.setState(SITACSelectedEntityView.State.SELECTION);
			selectedEntityView.show();
		}
	}

	@Override
	public void onSelectedEntityButtonClicked() {
		if (mapView != null && selectedEntityView != null) {
			IModel m;
			switch (selectedEntityView.getState()) {
				case DELETION:
					m = modelMap.get(selectedEntity);
					CmdManager.getInstance(engine).execute(DeleteModelCommand.class, m, selectedEntity);
					break;
					
				case CONFIRM:
					// if we are here, it means that the user just confirm that his zone/arrow action is
					// ok as it is right now, he do not want more points to be added to it
					// so now we can add it to the engine
					m = factory.build(selectedEntity);
					// delete the IShapedEntity because it is on the map just to render
					// locally to the user what he was doing
					mapView.deleteEntity(selectedEntity);
					modelMap.put(selectedEntity, m);
					CmdManager.getInstance(engine).execute(AddModelCommand.class, m, selectedEntity);
					break;
			}

			// hiding the selectedEntityView
			selectedEntityView.hide();
			selectedEntity = null;
		}
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e, MapView mapV) {
		if (mapView != null && selectedEntity != null) {
			// get the entity location on the map
			Projection prj = mapV.getProjection();
			IGeoPoint geoP = prj.fromPixels(e.getX(), e.getY());
			selectedEntity.setGeoPoint(new GeoPoint(geoP.getLatitudeE6(), geoP.getLongitudeE6()));

			IModel m;
			switch (selectedEntity.getState()) {
				case NEW:
					if (selectedEntity instanceof IShapedEntity) {
						// add the singleTap point to the IShapedEntity
						((ShapedEntity) selectedEntity).addPoint(geoP);
						// but do not add it to the engine until the user
						// confirm that the entity is okay, instead display
						// it on the mapView to ensure that the user sees
						// what he is doing
						mapView.addEntity(selectedEntity);
						// set the entity state to ON_MAP to ensure that it 
						// will be added only once to the mapView
						selectedEntity.setState(IEntity.State.ON_MAP);
						
					} else {
						m = factory.build(selectedEntity);
						modelMap.put(selectedEntity, m);
						CmdManager.getInstance(engine).execute(AddModelCommand.class, m, selectedEntity);
						// the entity has been added to the engine, we can hide the selectedEntityView
						selectedEntityView.hide();
						// discard the selectedEntity
						selectedEntity = null;
					}
					break;
					
				case SAVED:
					if (!(selectedEntity instanceof IShapedEntity)) {
						// check if we already have the model associated with this entity
						if (!modelMap.containsKey(selectedEntity)) {
							// create the model associated with this entity
							m = factory.build(selectedEntity);
							modelMap.put(selectedEntity, m);
							
						} else {
							// we already have the model in the map
							m = modelMap.get(selectedEntity);
							// update the model location with the singleTap point
							m.setLocation(new GpsPoint(geoP.getLatitudeE6(), geoP.getLongitudeE6()));
						}
						// update the model on the engine
						CmdManager.getInstance(engine).execute(UpdateModelCommand.class, m, selectedEntity);
						// the entity has been moved on map and its model has been updated
						// so we can hide the selectedEntityView
						selectedEntityView.hide();
						// discard the selectedEntity
						selectedEntity = null;
					}
					break;
					
				case ON_MAP:
					// just add the singleTap point to the IShapedEntity
					// because it is already displayed on the map
					((ShapedEntity) selectedEntity).addPoint(geoP);
					
					if (((ShapedEntity) selectedEntity).isPersistable()) {
						selectedEntityView.setState(SITACSelectedEntityView.State.CONFIRM);
						selectedEntityView.show();
					}
					
					break;
			}
				
			return true;
		}
		return false;
	}

	@Override
	public void onEntitySelected(IEntity e) {
		if (mapView != null && selectedEntityView != null) {
			// updating the selectedEntityView
			selectedEntity = e;
			selectedEntityView.updateView(selectedEntity.getIcon(), selectedEntity.getType()+selectedEntity.getMessage());
			selectedEntityView.setState(SITACSelectedEntityView.State.DELETION);
			selectedEntityView.show();
		}
	}

	public void registerMapView(SITACMapView mapView) {
		this.mapView = mapView;
	}

	public void registerMenuView(SITACMenuView menuView) {
				
	}

	public void registerSelectedEntityView(SITACSelectedEntityView view) {
		this.selectedEntityView = view;
	}

}
