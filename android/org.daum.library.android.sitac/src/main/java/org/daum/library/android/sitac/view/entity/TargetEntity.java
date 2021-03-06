package org.daum.library.android.sitac.view.entity;

import android.graphics.drawable.Drawable;
import org.daum.library.android.sitac.visitor.IVisitor;
import org.daum.common.genmodel.*;

public class TargetEntity extends AbstractEntity {

    public static final String WATER = "Eau";
    public static final String FIRE = "Incendie";
    public static final String VICTIM = "Personne";
    public static final String CHEM = "Risque particulier";
	
	public TargetEntity(Drawable icon, String type) {
		this(icon, type, "");
	}

	public TargetEntity(Drawable icon, String type, String message) {
		super(icon, type, message);
		setTagTextEnabled(false);
	}

    @Override
    public void accept(IVisitor visitor, IModel m) {
        visitor.visit(this, m);
    }

}
