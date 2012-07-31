package org.daum.library.android.sitac.view.entity;

import java.util.ArrayList;

import org.daum.library.android.sitac.visitor.IVisitor;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;

import org.daum.common.genmodel.*;
import pythagoras.f.IRectangle;
import pythagoras.f.Line;
import pythagoras.f.Rectangle;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class ArrowEntity extends AbstractShapedEntity {

    private static final String TAG = "ArrowEntity";

    public static final String SAP = "Secours à personnes";
    public static final String CHEM = "Risques particuliers";
    public static final String FIRE = "Extinction";
    public static final String WATER = "Eau";
    
	/**
	 * Width of the "virtual click" that is use
	 * to check if it intersects with this entity lines.
	 **/
	private static final int TOLERANCE_WIDTH = 15;
	/**
	 * Height of the "virtual click" that is use
	 * to check if it intersects with this entity lines.
	 **/
	private static final int TOLERANCE_HEIGHT = 15;
    
    private ArrayList<Line> lines;
    private float[] arrowHead = new float[8];
    private double xa, ya, xb, yb, xc, yc, adj, hyp, rotateHeadAngle;

	public ArrowEntity(Drawable icon, String type, int arrowColor) {
		this(icon, type, "", arrowColor);
	}

	public ArrowEntity(Drawable icon, String type, String message, int arrowColor) {
		super(icon, type, message, arrowColor);
		lines = new ArrayList<Line>();
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView) {
		if (points.size() > 0) {
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
				lines.clear();
				for (i=0; i<pts.length-1; i++) {
					canvas.drawLine(pts[i].x, pts[i].y, pts[i+1].x, pts[i+1].y, paint);
					lines.add(new Line(pts[i].x, pts[i].y, pts[i+1].x, pts[i+1].y));
				}
				
				// draw the head of the arrow
                arrowHead[0] = pts[pts.length-1].x-10;
                arrowHead[1] = pts[pts.length-1].y+20;

                arrowHead[2] = pts[pts.length-1].x;
                arrowHead[3] = pts[pts.length-1].y;

                arrowHead[4] = pts[pts.length-1].x;
                arrowHead[5] = pts[pts.length-1].y;

                arrowHead[6] = pts[pts.length-1].x+10;
                arrowHead[7] = pts[pts.length-1].y+20;

                xa = pts[pts.length-2].x;
                ya = pts[pts.length-2].y;
                xb = pts[pts.length-1].x;
                yb = pts[pts.length-1].y;
                xc = xa;
                yc = yb;

                adj= Math.sqrt(Math.pow(xc - xa, 2) + Math.pow(yc - ya, 2));
                hyp = Math.sqrt(Math.pow(xb - xa, 2) + Math.pow(yb - ya, 2));

                if (xb >= xa && yb <= ya) {
                    // top right direction
                    rotateHeadAngle = (Math.acos(adj/hyp)*(180/Math.PI));

                } else if (xb >= xa && yb >= ya) {
                    // bottom right direction
                    arrowHead[1] = pts[pts.length-1].y-20;
                    arrowHead[7] = pts[pts.length-1].y-20;
                    rotateHeadAngle = -(Math.acos(adj/hyp)*(180/Math.PI));

                } else if (xb <= xa && yb >= ya) {
                    // bottom left direction
                    rotateHeadAngle = (Math.acos(adj/hyp)*(180/Math.PI)+180);

                } else if (xb <= xa && yb <= ya) {
                    // top left direction
                    rotateHeadAngle = -(Math.acos(adj/hyp)*(180/Math.PI));
                }

                canvas.save();
                canvas.rotate((float) rotateHeadAngle, pts[pts.length-1].x, pts[pts.length-1].y);
                canvas.drawLines(arrowHead, paint);
                canvas.restore();
			}
		}
	}
	
	@Override
	public boolean containsPoint(IGeoPoint geoP, MapView mapView) {
		Point pt = mapView.getProjection().toMapPixels(geoP, null);
		IRectangle r = new Rectangle(pt.x - (TOLERANCE_WIDTH / 2), pt.y
				- (TOLERANCE_HEIGHT / 2), TOLERANCE_WIDTH, TOLERANCE_HEIGHT);
		for (Line line : lines) {
			// test if the "virtual click" rectangle intersects one of the lines
			if (line.intersects(r)) return true;
		}
		return false;
	}
	
	@Override
	public boolean isPersistable() {
		return (points.size() > 1);
	}

    @Override
    public void accept(IVisitor visitor, IModel m) {
        visitor.visit(this, m);
    }
}
