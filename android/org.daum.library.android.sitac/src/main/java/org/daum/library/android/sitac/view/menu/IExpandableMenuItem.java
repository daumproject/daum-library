package org.daum.library.android.sitac.view.menu;

import android.graphics.drawable.Drawable;

public interface IExpandableMenuItem {

	Drawable getIcon();
	
	String getText();

    void setText(String text);

    void setIcon(Drawable icon);
}
