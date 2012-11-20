package org.daum.library.android.sitac.view;

import org.daum.library.android.sitac.controller.ISITACController;
import org.daum.library.android.sitac.controller.SITACController;

import android.content.Context;
import android.widget.RelativeLayout;

public class SITACView extends RelativeLayout {
	
	private Context ctx;
	private SITACController sitacCtrl;
	private SITACMapView mapView;
	private SITACMenuLayout menuView;
	private SITACSelectedEntityView selectedEntityView;

	public SITACView(Context context) {
		super(context);
		this.ctx = context;
		this.sitacCtrl = new SITACController(ctx);
		initUI();
		configUI();
		defineCallbacks();
	}
	
	private void initUI() {
		mapView = new SITACMapView(ctx);
		sitacCtrl.registerMapView(mapView);
		menuView = new SITACMenuLayout(ctx);
		sitacCtrl.registerMenuView(menuView);
		selectedEntityView = new SITACSelectedEntityView(ctx);
		sitacCtrl.registerSelectedEntityView(selectedEntityView);
	}
	
	private void configUI() {
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		addView(mapView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(menuView);

		RelativeLayout.LayoutParams entityParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		entityParams.addRule(ALIGN_PARENT_TOP);
		entityParams.addRule(ALIGN_PARENT_RIGHT);
		addView(selectedEntityView, entityParams);
		// hide the selectedEntityView by default because no entities are selected
		selectedEntityView.hide();
	}
	
	private void defineCallbacks() {
		mapView.setOnOverlayEventListener(sitacCtrl.getUIHandler());
		menuView.setOnMenuViewEventListener(sitacCtrl.getUIHandler());
		selectedEntityView.setOnSelectedEntityEventListener(sitacCtrl.getUIHandler());
	}
	
	public ISITACController getController() {
		return sitacCtrl;
	}
}
