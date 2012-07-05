package org.daum.library.android.sitac.view.map;

import java.util.ArrayList;

import android.util.Log;
import org.daum.library.android.sitac.listener.OnOverlayEventListener;
import org.daum.library.android.sitac.view.entity.AbstractShapedEntity;
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
    public void onDetach(MapView mapView) {
        super.onDetach(mapView);
        Log.w(TAG, "On detach in overlay dude");
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
			IEntity ent;
			for (int i = entities.size()-1; i>= 0; i--) {
				ent = entities.get(i);
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
				if (ent.isDrawable()) ent.draw(canvas, mapV);
			}
		}
	}
	
	public void setOnOverlayEventListener(OnOverlayEventListener listener) {
		this.listener = listener;
	}

	public void addEntity(IEntity entity) {
		// this little trick do two things
		// - first it makes AbstractShapedEntity objects
		// 	be drawn on the "bottom" of the entity stack
		// - second it makes AbstractShapedEntity objects
		// 	be "clicked" after other entities in the priority order
		// which means that for example if a DemandEntity
		// is in a ZoneEntity, the DemandEntity will
		// answer first and consume the longPress event
		if (entity instanceof AbstractShapedEntity) {
			entities.add(0, entity);
		} else entities.add(entity);
	}

    public boolean hasEntity(IEntity entity) {
        return entities.contains(entity);
    }

	public void deleteEntity(IEntity entity) {
		entities.remove(entity);
	}
}
