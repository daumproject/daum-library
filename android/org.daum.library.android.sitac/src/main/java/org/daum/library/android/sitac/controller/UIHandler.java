package org.daum.library.android.sitac.controller;

import java.util.Hashtable;

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
import org.daum.library.android.sitac.view.entity.ArrowEntity;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.entity.IModelFactory;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;

import android.view.MotionEvent;

public class UIHandler implements OnOverlayEventListener, OnSelectedEntityEventListener, OnMenuViewEventListener {
	
	private SITACMapView mapView;
	private SITACSelectedEntityView selectedEntityView;
	private IEntity selectedEntity;
	private IModelFactory factory;
	private SITACEngine engine;
	private Hashtable<IEntity, IModel> map;
	
	public UIHandler(IModelFactory factory, SITACEngine engine) {
		this.factory = factory;
		this.engine = engine;
		this.map = new Hashtable<IEntity, IModel>();
	}
	
	@Override
	public void onMenuItemSelected(IEntity entity) {
		if (selectedEntityView != null) {
			// updating the selectedEntityView
			selectedEntity = entity;
			String txt = selectedEntity.getType()+selectedEntity.getMessage();
			selectedEntityView.updateView(entity.getIcon(), txt);
			selectedEntityView.setState(SITACSelectedEntityView.STATE_SELECTION);
			selectedEntityView.show();
		}
	}

	@Override
	public void onSelectedEntityButtonClicked() {
		if (mapView != null && selectedEntityView != null) {
			if (selectedEntityView.getState() == SITACSelectedEntityView.STATE_DELETION) {
				IModel m = factory.build(selectedEntity);
				CmdManager.getInstance(engine).execute(DeleteModelCommand.class, m, selectedEntity);
				
			} else if (selectedEntityView.getState() == SITACSelectedEntityView.STATE_SELECTION_CONFIRM) {
				// TODO save entity in the engine
				// if we are here, it means that the user just confirm that his zone/arrow action is
				// ok as it is right now, he do not want more points to be added to it 
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
			
			// if it's a new entity, add it to the engine
			if (selectedEntity.getState() == IEntity.STATE_NEW) {
				IModel m = factory.build(selectedEntity);
				map.put(selectedEntity, m);
				CmdManager.getInstance(engine).execute(AddModelCommand.class, m, selectedEntity);
				
			// if the state is EDITED, then update the corresponding data in the engine
			} else if (selectedEntity.getState() == IEntity.STATE_EDITED) {
				IModel m;
				if (!map.contains(selectedEntity)) {
					m = factory.build(selectedEntity);
					map.put(selectedEntity, m);
				} else {
					m = map.get(selectedEntity);
				}
				CmdManager.getInstance(engine).execute(UpdateModelCommand.class, m, selectedEntity);
			}
			
			// if it's an arrowEntity
			if (selectedEntity instanceof ArrowEntity) {
				// add the singleTap point to the entity
				((ArrowEntity) selectedEntity).addPoint(geoP);
				
				// if the arrowEntity has at least one line, ask for drawing ending
				if (((ArrowEntity) selectedEntity).hasOneLine()) {
					// display the "terminate action" button
					selectedEntityView.setState(SITACSelectedEntityView.STATE_SELECTION_CONFIRM);
					selectedEntityView.show();
				}
				
			} else {
				// the entity has been moved, we can hide the selectedEntityView
				selectedEntityView.hide();
				selectedEntity = null;	
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
			selectedEntityView.setState(SITACSelectedEntityView.STATE_DELETION);
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
