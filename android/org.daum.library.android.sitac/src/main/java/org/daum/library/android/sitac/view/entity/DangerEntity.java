package org.daum.library.android.sitac.view.entity;

import android.graphics.drawable.Drawable;

public class DangerEntity extends Entity {

    public static final String WATER = "Eau";
    public static final String FIRE = "Incendie";
    public static final String CHEM = "Risques particuliers";
	
	public DangerEntity(Drawable icon, String type) {
		this(icon, type, "");
	}

	public DangerEntity(Drawable icon, String type, String message) {
		super(icon, type, message);
		setTagTextEnabled(false);
	}
}
