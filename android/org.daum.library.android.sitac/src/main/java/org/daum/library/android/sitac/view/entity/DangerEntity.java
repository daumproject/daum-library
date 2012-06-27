package org.daum.library.android.sitac.view.entity;

import android.graphics.drawable.Drawable;
import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.visitor.IVIsitor;

public class DangerEntity extends AbstractEntity {

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

    @Override
    public void accept(IVIsitor visitor, IModel m) {
        visitor.visit(this, m);
    }
}
