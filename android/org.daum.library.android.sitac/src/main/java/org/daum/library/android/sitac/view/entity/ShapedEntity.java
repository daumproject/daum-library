package org.daum.library.android.sitac.view.entity;

import java.util.ArrayList;

import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.visitor.IVisitor;
import org.osmdroid.api.IGeoPoint;

import android.graphics.drawable.Drawable;

public abstract class ShapedEntity extends AbstractEntity implements IShapedEntity {
    
	protected ArrayList<IGeoPoint> points;
	
	public ShapedEntity(Drawable icon, String type, int color) {
		this(icon, type, "", color);
	}

	public ShapedEntity(Drawable icon, String type, String message, int color) {
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
    public void accept(IVisitor visitor, IModel m) {
        visitor.visit(this, m);
    }
}
