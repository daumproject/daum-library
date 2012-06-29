package org.daum.library.android.sitac.view.entity;

import android.graphics.*;
import android.util.Log;
import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.visitor.IVisitor;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;

import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

public class ZoneEntity extends AbstractShapedEntity {

    private static final String TAG = "ZoneEntity";

	private Paint pointPaint;
	private pythagoras.d.Path zone;
    private Path path;
	
	public ZoneEntity(Drawable icon, String type, int color) {
		this(icon, type, "", color);
	}

	public ZoneEntity(Drawable icon, String type, String message, int color) {
		super(icon, type, message, color);
		paint.setColor(Color.argb(60, Color.red(color), Color.green(color), Color.blue(color)));
		paint.setStyle(Paint.Style.FILL);

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        zone = new pythagoras.d.Path(pythagoras.d.Path.WIND_EVEN_ODD);
		
		pointPaint = new Paint(paint);
		pointPaint.setStyle(Style.STROKE);
		pointPaint.setColor(Color.argb(255, Color.red(color), Color.green(color), Color.blue(color)));
	}

	@Override
	public void draw(Canvas canvas, MapView mapView) {
		if (points.size() > 0) {
			Point[] pts = new Point[points.size()];
			int i=0;
			Projection prj = mapView.getProjection();
			for (IGeoPoint geoP : points) pts[i++] = prj.toMapPixels(geoP, null);
			
			if (pts.length <= 2) {
				// just draw the first point
				pointPaint.setStrokeWidth(4);
				for (Point p : pts) canvas.drawPoint(p.x, p.y, pointPaint);

			} else {
                //if (geoPoint != null) {
                //    Log.d(TAG, "geoPoint is not null, setting offset to the ZoneEntity");
                //    offsetPath(pts, prj.toMapPixels(geoPoint, null));
                //}

				// draw the filled zone
                path.reset();
                zone.reset();
				path.moveTo(pts[0].x, pts[0].y);
                zone.moveTo(pts[0].x, pts[0].y);
				for (i=1; i<pts.length-1; i++) {
					path.lineTo(pts[i].x, pts[i].y);
                    zone.lineTo(pts[i].x, pts[i].y);
				}
				path.lineTo(pts[pts.length-1].x, pts[pts.length-1].y);
                zone.lineTo(pts[pts.length-1].x, pts[pts.length-1].y);
				path.close();
                zone.closePath();

                canvas.drawPath(path, paint);

                // draw the boundary lines
                pointPaint.setStrokeWidth(2);
                for (i=0; i<pts.length-1; i++) {
                    canvas.drawLine(pts[i].x, pts[i].y, pts[i+1].x, pts[i+1].y, pointPaint);
                }
                canvas.drawLine(pts[i].x, pts[i].y, pts[0].x, pts[0].y, pointPaint);
			}
		}
	}
	
	@Override
	public boolean isPersistable() {
		return (points.size() >= 3);
	}
	
	@Override
	public boolean containsPoint(IGeoPoint geoP, MapView mapView) {
		Point pt = mapView.getProjection().toMapPixels(geoP, null);
		return zone.contains(pt.x, pt.y);
	}

    @Override
    public void accept(IVisitor visitor, IModel m) {
        visitor.visit(this, m);
    }

    private void offsetPath(Point[] pts, Point p) {
        RectF bounds = new RectF();
        path.computeBounds(bounds, false);
        float dx = p.x - bounds.centerX();
        float dy = p.y - bounds.centerY();

        for (Point pt : pts) {
            pt.x = pt.x + (int) dx;
            pt.y = pt.y + (int) dy;
        }
    }
}
