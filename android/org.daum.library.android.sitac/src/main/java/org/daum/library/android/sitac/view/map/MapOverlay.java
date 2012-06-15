package org.daum.library.android.sitac.view.map;

import java.util.ArrayList;

import org.daum.library.android.sitac.listener.OnOverlayEventListener;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class MapOverlay extends Overlay {

	private static final String TAG = "MapOverlay";
	
	private OnOverlayEventListener listener;
	private Paint paint;
	private ArrayList<IEntity> entities;
	
	public MapOverlay(Context context) {
		super(context);
		this.paint = new Paint();
		this.paint.setColor(Color.CYAN);
		this.paint.setAntiAlias(true);
		this.paint.setStrokeWidth(7);
		
		this.entities = new ArrayList<IEntity>();
	}
	
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
		if (listener != null) return listener.onSingleTapConfirmed(e, mapView);
		return super.onSingleTapConfirmed(e, mapView);
	}
	
	@Override
	public boolean onLongPress(MotionEvent e, MapView mapView) {
		if (listener != null) {			
			IGeoPoint geoP = mapView.getProjection().fromPixels(e.getX(), e.getY());
			for (IEntity ent : entities) {
				if (ent.containsPoint(geoP, mapView)) {
					listener.onEntitySelected(ent);
					return true;
				}
			}
		}
		return super.onLongPress(e, mapView);
	}

	@Override
	protected void draw(Canvas canvas, MapView mapV, boolean shadow) {
		if (!shadow) {
			for (IEntity ent : entities) {
				ent.draw(canvas, mapV);
			}
		}
	}
	
	public void setOnOverlayEventListener(OnOverlayEventListener listener) {
		this.listener = listener;
	}

	public void addEntity(IEntity entity) {
		entities.add(entity);
	}

	public void deleteEntity(IEntity entity) {
		entities.remove(entity);
	}
}
