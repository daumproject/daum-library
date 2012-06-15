package org.daum.library.android.sitac.view.menu;

import java.io.InputStream;

import org.daum.library.android.sitac.view.DrawableFactory;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


public class ExpandableMenuItem implements IExpandableMenuItem {
	
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
		this.icon = DrawableFactory.buildDrawable(context, iconPath);
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
