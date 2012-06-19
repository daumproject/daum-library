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
	
	public ArrowEntity(Drawable icon, String type, int arrowColor) {
		this(icon, type, "", arrowColor);
	}

	public ArrowEntity(Drawable icon, String type, String message, int arrowColor) {
		super(icon, type, message);
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
	
	@Override
	public boolean containsPoint(IGeoPoint geoP, MapView mapView) {
		Projection prj = mapView.getProjection();
		for (int i=0; i<points.size()-1; i++) {
			Point a = prj.toMapPixels(points.get(i), null);
			Point b = prj.toMapPixels(points.get(i+1), null);
			Point pt = prj.toMapPixels(geoP, null);
			if (isPointOnLine(a, b, pt)) return true;
		}
		return false;
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
	
	public boolean isPointOnLine(Point a, Point b, Point pt) {
		float A = -(b.y-a.y) / (b.x-a.x);
		float B = a.y - (A * a.x); // y = Ax + B
		int pt_y = Math.abs((int) (pt.x*A + B));
		pt.y = Math.abs(pt.y);
		
		return Math.abs(pt_y - pt.y) < 10;
	}

	public ArrayList<IGeoPoint> getPoints() {
		return points;
	}
}
