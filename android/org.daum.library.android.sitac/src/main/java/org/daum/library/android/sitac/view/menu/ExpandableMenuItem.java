package org.daum.library.android.sitac.view.menu;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;


public class ExpandableMenuItem implements IExpandableMenuItem {

    private static final String TAG = "ExpandableMenuItem";
	
	private Drawable icon;
	private String text;
	
	public ExpandableMenuItem(String text) {
		this(null, null, text);
	}
	
	public ExpandableMenuItem(Drawable icon, String text) {
		this.icon = icon;
		this.text = text;
	}
	
	public ExpandableMenuItem(Context context, String iconPath, String text) {
		try {
			//URL url = getClass().getResource(iconPath).openStream();
            InputStream is = getClass().getResourceAsStream("/images/picto.png");
            Log.i(TAG, "input stream = "+is);
            Bitmap bmp = BitmapFactory.decodeStream(is);
			this.icon = new BitmapDrawable(bmp);
		} catch (Exception e) {
			// don't care just display text if null
		}
		this.text = text;
	}

	@Override
	public Drawable getIcon() {
		return icon;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return text;
	}
}
