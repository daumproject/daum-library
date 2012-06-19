package org.daum.library.android.sitac.view.entity;

import android.graphics.drawable.Drawable;

public class DangerEntity extends Entity {
	
	public DangerEntity(Drawable icon, String type) {
		this(icon, type, "");
	}

	public DangerEntity(Drawable icon, String type, String message) {
		super(icon, type, message);
		setTagTextEnabled(false);
	}
}
