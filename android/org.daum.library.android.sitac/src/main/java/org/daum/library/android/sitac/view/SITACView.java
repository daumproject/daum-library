package org.daum.library.android.sitac.view;

import org.daum.library.android.sitac.controller.ISITACController;
import org.daum.library.android.sitac.controller.SITACController;

import android.content.Context;
import android.widget.RelativeLayout;

public class SITACView extends RelativeLayout {
	
	private Context ctx;
	private SITACController sitacCtrl;
	private SITACMapView mapView;
	private SITACMenuView menuView;
	private SITACSelectedEntityView currentEntityView;

	public SITACView(Context context) {
		super(context);
		this.ctx = context;
		this.sitacCtrl = SITACController.getInstance(ctx);
		initUI();
		configUI();
		defineCallbacks();
	}
	
	private void initUI() {
		mapView = new SITACMapView(ctx, sitacCtrl);
		sitacCtrl.registerMapView(mapView);
		menuView = new SITACMenuView(ctx);
		sitacCtrl.registerMenuView(menuView);
		currentEntityView = new SITACSelectedEntityView(ctx);
		sitacCtrl.registerSelectedEntityView(currentEntityView);
	}
	
	private void configUI() {
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		addView(mapView);
		addView(menuView);
		
		RelativeLayout.LayoutParams entityParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		entityParams.addRule(ALIGN_PARENT_TOP);
		entityParams.addRule(ALIGN_PARENT_RIGHT);
		addView(currentEntityView, entityParams);
	}
	
	private void defineCallbacks() {
		mapView.setOnOverlayEventListener(sitacCtrl.getUIHandler());
		menuView.setOnMenuViewEventListener(sitacCtrl.getUIHandler());
		currentEntityView.setOnSelectedEntityEventListener(sitacCtrl.getUIHandler());
	}
	
	public ISITACController getController() {
		return sitacCtrl;
	}
}
