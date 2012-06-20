package org.daum.library.android.sitac.view.entity;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class ZoneEntity extends ArrowEntity {
	
	private Paint pointPaint;
	
	public ZoneEntity(Drawable icon, String type, int color) {
		this(icon, type, "", color);
	}

	public ZoneEntity(Drawable icon, String type, String message, int color) {
		super(icon, type, message, color);
		paint.setColor(Color.argb(60, Color.red(color), Color.green(color), Color.blue(color)));
		paint.setStyle(Paint.Style.FILL);
		
		pointPaint = new Paint(paint);
		pointPaint.setStyle(Style.STROKE);
		pointPaint.setColor(Color.argb(255, Color.red(color), Color.green(color), Color.blue(color)));
		pointPaint.setStrokeWidth(4);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView) {
		Point[] pts = new Point[points.size()];
		int i=0;
		Projection prj = mapView.getProjection();
		for (IGeoPoint geoP : points) pts[i++] = prj.toMapPixels(geoP, null);
		
		if (pts.length <= 2) {
			// just draw the first point
			for (Point p : pts) {
				canvas.drawPoint(p.x, p.y, pointPaint);
			}
		} else {
			// draw the filled zone
			Path path = new Path();
			path.setFillType(Path.FillType.EVEN_ODD);
			path.moveTo(pts[0].x, pts[0].y);
			for (i=1; i<pts.length-1; i++) {
				path.lineTo(pts[i].x, pts[i].y);
			}
			path.lineTo(pts[pts.length-1].x, pts[pts.length-1].y);
			path.close();
			
			canvas.drawPath(path, paint);
			
			// draw the boundary lines
			// draw the lines
			for (i=0; i<pts.length-1; i++) {
				canvas.drawLine(pts[i].x, pts[i].y, pts[i+1].x, pts[i+1].y, pointPaint);
			}
			canvas.drawLine(pts[i].x, pts[i].y, pts[0].x, pts[0].y, pointPaint);
		}
	}
	
	@Override
	public boolean isDrawable() {
		return (points.size() >= 3);
	}
}
