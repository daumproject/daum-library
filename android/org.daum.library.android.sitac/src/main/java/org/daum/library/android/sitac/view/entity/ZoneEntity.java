package org.daum.library.android.sitac.view.entity;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class ZoneEntity extends ArrowEntity {
	
	public ZoneEntity(Drawable icon, String type, int color) {
		this(icon, type, "", color);
	}

	public ZoneEntity(Drawable icon, String type, String message, int color) {
		super(icon, type, message, color);
		paint.setColor(Color.argb(220, Color.red(color), Color.green(color), Color.blue(color)));
		paint.setStyle(Paint.Style.FILL);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView) {
		Point[] pts = new Point[points.size()];
		int i=0;
		Projection prj = mapView.getProjection();
		for (IGeoPoint geoP : points) pts[i++] = prj.toMapPixels(geoP, null);
		
		if (pts.length <= 1) {
			// just draw the first point
			canvas.drawPoint(pts[0].x, pts[0].y, paint);
		} else {
			// draw the lines
			Path path = new Path();
			path.moveTo(pts[0].x, pts[0].y);
			for (i=1; i<pts.length-2; i++) {
				path.lineTo(pts[i].x, pts[i].y);
			}
			path.lineTo(pts[pts.length-1].x, pts[pts.length-1].x);
			path.close();
			
			canvas.drawPath(path, paint);
		}
	}
}
