package org.daum.library.android.sitac.view.entity;

import java.util.Observer;

import org.daum.library.android.sitac.observer.MyObservable;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public abstract class Entity implements IEntity {
	
	private long id = -1;
	private String type;
	private String message;
	private Drawable icon;
	private Bitmap bmp;
	private IGeoPoint geoPoint;
	private int state;
	private boolean tagTextEnabled = true;
	
	private int bmpWidth	= 0;
	private int bmpHeight	= 0;
	private int bmp_x		= 0;
	private int bmp_y		= 0;
	private int txt_x 		= 0;
	private int txt_y 		= 0;
	private int txtBgLeft	= 0;
	private int txtBgTop	= 0;
	private int txtBgRight	= 0;
	private int txtBgBottom	= 0;
	
	protected MyObservable observable;
	protected Rect bounds;
	protected Paint paint;
	
	public Entity(Drawable icon, String type) {
		this(icon, type, "");
	}
	
	public Entity(Drawable icon, String type, String message) {
		this.icon = icon;
		this.type = type;
		this.message = message;
		this.paint = new Paint();
		this.paint.setAntiAlias(true);
		this.bounds = new Rect();
		this.state = STATE_NEW;
		
		this.observable = new MyObservable();
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView) {
		Point p = mapView.getProjection().toMapPixels(geoPoint, null);
		
		if (icon != null) {
			bmp = ((BitmapDrawable) icon).getBitmap();
			bmpWidth = bmp.getWidth(); 
			bmpHeight = bmp.getHeight();
			bmp_x = p.x-(bmpWidth/2);
			bmp_y = p.y-(bmpHeight/2);
			canvas.drawBitmap(bmp, bmp_x, bmp_y, null);
		}
		
		if (tagTextEnabled) {
			paint.setTextSize(13);
			float txtWidth = paint.measureText(type+message);
			txt_x	= p.x-((int)txtWidth/2);
			txt_y	= p.y+(bmpHeight/2)+ (int)(-paint.getFontMetrics().top)+3;
			
			txtBgLeft	= txt_x - 2;
			txtBgTop	= txt_y + (int) paint.getFontMetrics().top;
			txtBgRight	= txt_x + (int) txtWidth + 2;
			txtBgBottom	= txt_y + (int) paint.getFontMetrics().bottom;
			Rect r = new Rect(txtBgLeft, txtBgTop, txtBgRight, txtBgBottom);
			paint.setColor(Color.argb(180, 0, 0, 0));
			paint.setStyle(Style.FILL);
			canvas.drawRect(r, paint);
			paint.setColor(Color.WHITE);
			canvas.drawText(type+message, txt_x, txt_y, paint);
		} else {
			txtBgRight = bmp_x+bmp.getWidth();
			txtBgBottom = bmp_y+bmpHeight;
		}
		
		bounds.left		= (bmp_x <= txtBgLeft) ? bmp_x : txtBgLeft;
		bounds.top		= bmp_y;
		bounds.right	= (bmp_x+bmpWidth >= txtBgRight) ? bmp_x+bmpWidth : txtBgRight;
		bounds.bottom	= txtBgBottom;
		
		// DEBUG purposes: see the entity boundaries
//		Paint pa = new Paint();
//		pa.setColor(Color.MAGENTA);
//		pa.setStyle(Paint.Style.STROKE);
//		pa.setStrokeWidth(1);
//		canvas.drawRect(bounds, pa);
	}

	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public boolean containsPoint(IGeoPoint geoP, MapView mapView) {
		Point p = mapView.getProjection().toMapPixels(geoP, null);
		return bounds.contains(p.x, p.y);
	}
	
	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
		notifyObservers();
	}

	@Override
	public Drawable getIcon() {
		return icon;
	}

	@Override
	public void setIcon(Drawable icon) {
		this.icon = icon;
		notifyObservers();
	}

	@Override
	public IGeoPoint getGeoPoint() {
		return geoPoint;
	}

	@Override
	public void setGeoPoint(IGeoPoint geoPoint) {
		this.geoPoint = geoPoint;
		notifyObservers();
	}

	@Override
	public int getState() {
		return state;
	}

	@Override
	public void setState(int state) {
		this.state = state;
	}
	
	@Override
	public void setTagTextEnabled(boolean enabled) {
		this.tagTextEnabled = enabled;
		notifyObservers();
	}

	@Override
	public void addObserver(Observer obs) {
		observable.addObserver(obs);
	}
	
	protected void notifyObservers() {
		observable.setChanged();
		observable.notifyObservers(this);
	}
}
