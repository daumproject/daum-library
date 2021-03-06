package org.daum.library.android.sitac.view.map;

import java.util.ArrayList;

import android.graphics.Point;
import android.os.Vibrator;
import org.daum.library.android.sitac.listener.OnOverlayEventListener;
import org.daum.library.android.sitac.view.entity.AbstractShapedEntity;
import org.daum.library.android.sitac.view.entity.ArrowEntity;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.entity.ZoneEntity;
import org.kevoree.android.framework.helper.UIServiceHandler;
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
	private  Vibrator move =   null;

	public MapOverlay(Context context) {
		super(context);
		this.paint = new Paint();
		this.paint.setColor(Color.CYAN);
		this.paint.setAntiAlias(true);
		this.paint.setStrokeWidth(7);
        move = (Vibrator) UIServiceHandler.getUIService().getRootActivity().getSystemService(Context.VIBRATOR_SERVICE);
		this.entities = new ArrayList<IEntity>();
	}

    @Override
	public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
		if (listener != null) return listener.onSingleTapConfirmed(e, mapView);
		return super.onSingleTapConfirmed(e, mapView);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView) {
		if (listener != null) {
          boolean  found =false;
            int eventaction = e.getAction();

            switch (eventaction )
            {
                case MotionEvent.ACTION_DOWN:

                    IGeoPoint geoP = mapView.getProjection().fromPixels(e.getX(), e.getY());
                    IEntity ent;
                    for (int i = entities.size()-1; i>= 0; i--) {
                        ent = entities.get(i);
                        if (ent.containsPoint(geoP, mapView)) {
                            ent.setSelected(true);
                            move.vibrate(20);
                            listener.onEntitySelected(ent);
                            found = true;
                        }  else
                        {
                            ent.setSelected(false);
                        }
                    }
                    if(found){
                        return true;
                    }

                    break;
            }


		}
		return super.onTouchEvent(e, mapView);
	}

	@Override
	protected void draw(Canvas canvas, MapView mapV, boolean shadow) {
		if (!shadow) {
			for (IEntity ent : entities)
            {
				if (ent.isDrawable()){
                    ent.draw(canvas, mapV);
                    if(ent.isSelected())
                    {
                        if(!(ent instanceof ArrowEntity) && !(ent instanceof ZoneEntity)){
                            ent.drawCircle(canvas,mapV,true);
                        }
                    }
                }
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

    public void deleteAllEntities() {
        entities.clear();
    }
}
