package org.daum.library.android.sitac.view.entity;

import android.graphics.drawable.Drawable;

public class TargetEntity extends Entity {
	
	public TargetEntity(Drawable icon, String type) {
		super(icon, type);
	}

	public TargetEntity(Drawable icon, String type, String message) {
		super(icon, type, message);
		setTagTextEnabled(false);
	}

}
