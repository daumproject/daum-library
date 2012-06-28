package org.daum.library.android.sitac.view.entity;

import android.graphics.drawable.Drawable;
import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.visitor.IVisitor;

public class DemandEntity extends AbstractEntity {
	
	public DemandEntity(Drawable icon, String type) {
		super(icon, type);
	}

	public DemandEntity(Drawable icon, String type, String message) {
		super(icon, type, message);
	}

    @Override
    public void accept(IVisitor visitor, IModel m) {
        visitor.visit(this, m);
    }
}
