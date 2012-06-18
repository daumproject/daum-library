package org.daum.library.android.sitac.view.entity;

import android.graphics.drawable.Drawable;

public class TargetEntity extends Entity {

	public TargetEntity(Drawable icon, String msg) {
		super(icon, msg);
		setTagTextEnabled(false);
	}

}
