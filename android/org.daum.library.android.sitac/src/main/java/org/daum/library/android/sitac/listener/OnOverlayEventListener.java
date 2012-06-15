package org.daum.library.android.sitac.listener;

import org.daum.library.android.sitac.view.entity.IEntity;
import org.osmdroid.views.MapView;

import android.view.MotionEvent;

public interface OnOverlayEventListener {

	/**
	 * Called when a single tap event is handled by the overlay
	 * @param e the MotionEvent associated with the event
	 * @param mapView the actual mapView
	 */
	boolean onSingleTapConfirmed(MotionEvent e, MapView mapView);
	
	/**
	 * Called when an entity has been long pressed on the overlay
	 * @param e the selected entity
	 */
	void onEntitySelected(IEntity e);
}
