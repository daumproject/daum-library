package org.daum.library.android.sitac.controller;

import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.Target;
import org.daum.library.android.sitac.command.AddDangerCommand;
import org.daum.library.android.sitac.command.AddDemandCommand;
import org.daum.library.android.sitac.command.AddTargetCommand;
import org.daum.library.android.sitac.command.UpdateDemandCommand;
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
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.entity.IEntityFactory;
import org.daum.library.android.sitac.view.entity.TargetEntity;
import org.daum.library.android.sitac.view.entity.ZoneEntity;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;

import android.view.MotionEvent;

public class UIHandler implements OnOverlayEventListener, OnSelectedEntityEventListener, OnMenuViewEventListener {
	
	private SITACMapView mapView;
	private SITACSelectedEntityView selectedEntityView;
	private IEntity selectedEntity;
	private IEntityFactory factory;
	private SITACEngine engine;
	
	public UIHandler(IEntityFactory factory, SITACEngine engine) {
		this.factory = factory;
		this.engine = engine;
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
				
			// if the state is EDITED, then update the corresponding data in the engine
			} else if (selectedEntity.getState() == IEntity.STATE_EDITED) {
				mapView.deleteEntity(selectedEntity);
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

	public void registerMapView(SITACMapView mapView) {
		this.mapView = mapView;
	}

	public void registerMenuView(SITACMenuView menuView) {
				
	}

	public void registerSelectedEntityView(SITACSelectedEntityView view) {
		this.selectedEntityView = view;
	}

}
