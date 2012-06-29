package org.daum.library.android.sitac.view.entity;

import java.util.ArrayList;
import java.util.Observer;

import org.osmdroid.api.IGeoPoint;

import android.graphics.drawable.Drawable;

public abstract class AbstractShapedEntity extends AbstractEntity implements IShapedEntity {
    
	protected ArrayList<IGeoPoint> points;
	
	public AbstractShapedEntity(Drawable icon, String type, int color) {
		this(icon, type, "", color);
	}

	public AbstractShapedEntity(Drawable icon, String type, String message, int color) {
		super(icon, type, message);
		points = new ArrayList<IGeoPoint>();
		paint.setColor(color);
	}
	
	
	public void addPoint(IGeoPoint geoP) {
		points.add(geoP);
		notifyObservers();
	}
	
	public void removePoint(IGeoPoint geoP) {
		if (points.remove(geoP)) notifyObservers();
	}

	public ArrayList<IGeoPoint> getPoints() {
		return points;
	}

    @Override
    public void setGeoPoint(IGeoPoint geoPoint) {
        // we do not want to save the location until
        // the user confirm that his entity is ready
        // to be saved
        if (getState() == State.SAVED) {
            super.setGeoPoint(geoPoint);
        }
    }

    @Override
    public boolean isDrawable() {
        return points.size() > 0;
    }
}
