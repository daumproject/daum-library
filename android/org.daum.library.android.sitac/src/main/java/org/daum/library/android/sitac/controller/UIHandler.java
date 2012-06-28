package org.daum.library.android.sitac.controller;

import java.util.Date;
import java.util.Hashtable;

import android.util.Log;
import org.daum.common.gps.impl.GpsPoint;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.command.RedoCommand;
import org.daum.library.android.sitac.command.UndoCommand;
import org.daum.library.android.sitac.command.engine.AddModelCommand;
import org.daum.library.android.sitac.command.engine.DeleteModelCommand;
import org.daum.library.android.sitac.command.engine.UpdateModelCommand;
import org.daum.library.android.sitac.command.ui.AddPointCommand;
import org.daum.library.android.sitac.engine.SITACEngine;
import org.daum.library.android.sitac.engine.UndoRedoEngine;
import org.daum.library.android.sitac.listener.OnMenuViewEventListener;
import org.daum.library.android.sitac.listener.OnOverlayEventListener;
import org.daum.library.android.sitac.listener.OnSelectedEntityEventListener;
import org.daum.library.android.sitac.manager.CmdManager;
import org.daum.library.android.sitac.manager.EngineCmdManager;
import org.daum.library.android.sitac.manager.UICmdManager;
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
	private SITACEngine modelEngine;
	private UndoRedoEngine undoRedoEngine;
	private Hashtable<IEntity, IModel> modelMap;
	
	public UIHandler(IModelFactory factory, SITACEngine modelEngine, Hashtable<IEntity, IModel> modelMap) {
		this.factory = factory;
		this.modelEngine = modelEngine;
		this.undoRedoEngine = new UndoRedoEngine();
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
	public void onDeleteButtonClicked() {
		IModel m = modelMap.get(selectedEntity);
		if (m instanceof Demand) {
            ((Demand) m).setGh_desengagement(new Date());
            EngineCmdManager.getInstance(modelEngine).execute(UpdateModelCommand.class, m);
        } else {
            EngineCmdManager.getInstance(modelEngine).execute(DeleteModelCommand.class, m);
        }
		
		// hiding the selectedEntityView
		selectedEntityView.hide();
		selectedEntity = null;
	}
	
	@Override
	public void onConfirmButtonClicked() {
		// if we are here, it means that the user just confirm that his zone/arrow action is
		// ok as it is right now, he do not want more points to be added to it
		// so now we can add it to the engine
		IModel m = factory.build(selectedEntity);
		// delete the IShapedEntity because it is on the map just to render
		// locally to the user what he was doing
		mapView.deleteEntity(selectedEntity);
		modelMap.put(selectedEntity, m);
		EngineCmdManager.getInstance(modelEngine).execute(AddModelCommand.class, m);
		
		// putting selectedEntityView's state back to SELECTION
		// to enable entity selection on map again
		selectedEntityView.setState(SITACSelectedEntityView.State.SELECTION);
		
		// hiding the selectedEntityView
		selectedEntityView.hide();
		selectedEntity = null;
	}
	
	@Override
	public void onRedoButtonClicked() {
		CmdManager.getInstance(undoRedoEngine).execute(RedoCommand.class);
	}
	
	@Override
	public void onUndoButtonClicked() {
		CmdManager.getInstance(undoRedoEngine).execute(UndoCommand.class);
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
						UICmdManager.getInstance(undoRedoEngine).execute(AddPointCommand.class, selectedEntity, geoP);
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
						EngineCmdManager.getInstance(modelEngine).execute(AddModelCommand.class, m);
						// the entity has been added to the engine, we can hide the selectedEntityView
						selectedEntityView.hide();
						// discard the selectedEntity
						selectedEntity = null;
					}
					break;
					
				case SAVED:
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
                    EngineCmdManager.getInstance(modelEngine).execute(UpdateModelCommand.class, m);
                    // the entity has been moved on map and its model has been updated
                    // so we can hide the selectedEntityView
                    selectedEntityView.hide();
                    // discard the selectedEntity
                    selectedEntity = null;
					break;
					
				case ON_MAP:
					// just add the singleTap point to the IShapedEntity
					// because it is already displayed on the map
					UICmdManager.getInstance(undoRedoEngine).execute(AddPointCommand.class, selectedEntity, geoP);

                    // check if the entity has enough information to be saved
                    // if it is, display the confirm button in the SITACSelectedEntityView
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
			switch (selectedEntityView.getState()) {
				case CONFIRM:
                    // do nothing because we need to wait for the user to confirm that his entity
                    // is ready to be saved and drawn
					break;

				default:
					// updating the selectedEntityView
					selectedEntity = e;
					selectedEntityView.updateView(selectedEntity.getIcon(), selectedEntity.getType()+selectedEntity.getMessage());
					selectedEntityView.setState(SITACSelectedEntityView.State.DELETION);
					selectedEntityView.show();
					break;
			}
		}
	}

	public void registerMapView(SITACMapView mapView) {
		this.mapView = mapView;
	}

	public void registerSelectedEntityView(SITACSelectedEntityView view) {
		this.selectedEntityView = view;
	}

}
