package org.daum.library.android.sitac.view.entity;

import java.util.ArrayList;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class ArrowEntity extends Entity {
	
	protected ArrayList<IGeoPoint> points;

	public ArrowEntity(Drawable icon, String msg, int arrowColor) {
		super(icon, msg);
		points = new ArrayList<IGeoPoint>();
		paint.setColor(arrowColor);
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView) {
		Point[] pts = new Point[points.size()];
		int i=0;
		Projection prj = mapView.getProjection();
		for (IGeoPoint geoP : points) pts[i++] = prj.toMapPixels(geoP, null);
		
		paint.setStrokeWidth(5);
		if (pts.length <= 1) {
			// just draw the first point
			canvas.drawPoint(pts[0].x, pts[0].y, paint);
		} else {
			// draw the lines
			for (i=0; i<pts.length-1; i++) {
				canvas.drawLine(pts[i].x, pts[i].y, pts[i+1].x, pts[i+1].y, paint);
			}
			
			// draw the head of the arrow
			// TODO
		}
	}
	
	public void addPoint(IGeoPoint geoP) {
		points.add(geoP);
		notifyObservers();
	}
	
	/**
	 * 
	 * @return true if there's at least one line to draw not just one and only
	 *         point
	 */
	public boolean hasOneLine() {
		return (points.size() > 1);
	}
}
