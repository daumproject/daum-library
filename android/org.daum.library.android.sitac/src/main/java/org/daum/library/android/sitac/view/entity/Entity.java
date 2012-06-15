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
	private String message;
	private Drawable icon;
	private IGeoPoint geoPoint;
	private int state;
	
	protected MyObservable observable;
	protected Rect bounds;
	protected Paint paint;
	
	public Entity(Drawable icon, String msg) {
		this.icon = icon;
		this.message = msg;
		this.paint = new Paint();
		this.paint.setAntiAlias(true);
		this.bounds = new Rect();
		this.state = STATE_NEW;
		
		this.observable = new MyObservable();
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView) {
		Point p = mapView.getProjection().toMapPixels(geoPoint, null);
		Bitmap bmp = null;
		if (icon != null) bmp = ((BitmapDrawable) icon).getBitmap();
		
		int bmpWidth = (bmp == null) ? 0 : bmp.getWidth(); 
		int bmpHeight = (bmp == null) ? 0 : bmp.getHeight();
		int bmp_x = p.x-(bmpWidth/2);
		int bmp_y = p.y-(bmpHeight/2);
		
		if (bmp != null) canvas.drawBitmap(bmp, bmp_x, bmp_y, null);
		
		paint.setTextSize(12);
		float txtWidth = paint.measureText(message);
		int txt_x	= p.x-((int)txtWidth/2);
		int txt_y	= p.y+(bmpHeight/2)+15;
		
		int txtBgLeft	= txt_x - 2;
		int txtBgTop	= txt_y - 12;
		int txtBgRight	= txt_x + (int) txtWidth + 2;
		int txtBgBottom	= txt_y + 3;
		Rect r = new Rect(txtBgLeft, txtBgTop, txtBgRight, txtBgBottom);
		paint.setColor(Color.argb(180, 0, 0, 0));
		paint.setStyle(Style.FILL);
		canvas.drawRect(r, paint);
		paint.setColor(Color.WHITE);
		canvas.drawText(message, txt_x, txt_y, paint);
		
		bounds.left		= (bmp_x >= txtBgLeft) ? bmp_x : txtBgLeft;
		bounds.top		= bmp_y;
		bounds.right	= (bmp_x+bmpWidth >= txtBgRight) ? bmp_x+bmpWidth : txtBgRight;
		bounds.bottom	= txtBgBottom;
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
	public void addObserver(Observer obs) {
		observable.addObserver(obs);
	}
	
	protected void notifyObservers() {
		observable.setChanged();
		observable.notifyObservers(this);
	}
}
