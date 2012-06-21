package org.daum.library.android.sitac.view.entity;

import org.daum.library.android.sitac.observer.IObservable;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public interface IEntity extends IObservable {
	
	public enum State {
		/** default Entity State; mark the entity as "not saved into any engine" */
		NEW,
		/**  mark the entity as "saved into one engine" */
		SAVED,
		/** mark the entity as "displayed on map" but not saved into an engine */
		ON_MAP
	}

	public String getId();

	public void setId(String id);

	/**
	 * Draws the entity on the canvas
	 * 
	 * @param canvas
	 *            drawing surface
	 * @param mapView
	 *            mapView to compute GeoPoint into Pixel Point, Projection,
	 *            Boundaries...
	 */
	public void draw(Canvas canvas, MapView mapView);

	/**
	 * Return true when the GeoPoint geoP location is "on" the entity
	 * 
	 * @param geoP
	 *            some geoP location
	 * @param mapView
	 *            the mapView that handles the touch event
	 * @return true if the geoP is a part of this entity boundaries
	 */
	public boolean containsPoint(IGeoPoint geoP, MapView mapView);

	/**
	 * Must always be set, so this is not supposed to be null
	 * 
	 * @return a string representation of the type of the entity
	 */
	public String getType();

	/**
	 * Set the type of the entity. You should really be careful when using this
	 * method because it is used by the IModelFactory to create the model from
	 * the type
	 * 
	 * @param type
	 *            some model type
	 */
	public void setType(String type);

	/**
	 * A message use to add information for the user (like 01 in FPT01)
	 * 
	 * @return message
	 */
	public String getMessage();

	/**
	 * @param message
	 *            a string
	 */
	public void setMessage(String message);

	/**
	 * Gets the icon of the entity
	 * 
	 * @return icon a drawable
	 */
	public Drawable getIcon();

	/**
	 * Sets the icon of the entity
	 * 
	 * @param icon
	 *            some drawable
	 */
	public void setIcon(Drawable icon);

	/**
	 * The position of the entity on the map If this value is null, then the
	 * entity as never been positionned on the map
	 * 
	 * @return geoPoint gps location of the entity
	 */
	public IGeoPoint getGeoPoint();

	/**
	 * Change the location of the entity
	 * 
	 * @param geoPoint
	 *            new location
	 */
	public void setGeoPoint(IGeoPoint geoPoint);

	/**
	 * Get the state of the entity
	 * 
	 * @return State entity's state
	 */
	public State getState();

	/**
	 * Change the state of the entity
	 * 
	 * @param state
	 *            new state
	 */
	public void setState(State state);

	/**
	 * If set to true, an entity that does not override it's draw(Canvas,
	 * MapView) method from Entity will display a little tag with the entity
	 * type + message
	 * 
	 * @param enabled
	 *            boolean to active or not the tagText
	 */
	public void setTagTextEnabled(boolean enabled);
}
