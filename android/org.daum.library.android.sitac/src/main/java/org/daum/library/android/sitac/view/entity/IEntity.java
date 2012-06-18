package org.daum.library.android.sitac.view.entity;

import org.daum.library.android.sitac.observer.IObservable;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public interface IEntity extends IObservable {
	
	public static final int STATE_NEW		= 0;
	public static final int STATE_EDITED	= 1;
	
	public long getId();
	
	public void setId(long id);

	/**
	 * Draws the entity on the canvas
	 * @param canvas drawing surface
	 * @param mapView mapView to compute GeoPoint into Pixel Point, Projection, Boundaries...
	 */
	public void draw(Canvas canvas, MapView mapView);
	
	/**
	 * Return true when the GeoPoint geoP location is "on" the entity
	 * @param geoP some geoP location
	 * @param mapView the mapView that handles the touch event
	 * @return true if the geoP is a part of this entity boundaries
	 */
	public boolean containsPoint(IGeoPoint geoP, MapView mapView);
	
	public String getMessage();

	public void setMessage(String message);

	public Drawable getIcon();

	public void setIcon(Drawable icon);

	public IGeoPoint getGeoPoint();

	public void setGeoPoint(IGeoPoint geoPoint);
	
	public int getState();
	
	public void setState(int state);
	
	public void setTagTextEnabled(boolean enabled);
}
