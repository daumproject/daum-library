package org.daum.library.android.sitac.view.entity;

import android.graphics.drawable.Drawable;

public class DemandEntity extends Entity {
	
	public DemandEntity(Drawable icon, String type) {
		super(icon, type);
	}

	public DemandEntity(Drawable icon, String type, String message) {
		super(icon, type, message);
	}
}
