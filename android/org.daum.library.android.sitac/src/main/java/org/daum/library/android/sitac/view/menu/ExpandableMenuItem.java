package org.daum.library.android.sitac.view.menu;

import org.daum.library.android.sitac.view.DrawableFactory;

import android.content.Context;
import android.graphics.drawable.Drawable;
import org.daum.library.android.sitac.view.entity.IEntity;


public class ExpandableMenuItem implements IExpandableMenuItem {
	
	private Drawable icon;
	private String text;
	private IEntity e;

	public ExpandableMenuItem(String text) {
		this(null, null, text);
	}
	
	public ExpandableMenuItem(Drawable icon, String text) {
		this.icon = icon;
		this.text = text;
	}

    public ExpandableMenuItem(Drawable icon, String text,IEntity e) {
        this.icon = icon;
        this.text = text;
        this.e = e;
    }
	
	public ExpandableMenuItem(Context context, String iconPath, String text) {
		if (iconPath != null) this.icon = DrawableFactory.build(context, iconPath);
		this.text = text;
	}

    @Override
    public IEntity getEntity() {
        return e;
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
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
	public String toString() {
		return text;
	}
}
