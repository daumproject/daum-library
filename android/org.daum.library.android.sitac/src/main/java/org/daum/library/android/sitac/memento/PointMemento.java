package org.daum.library.android.sitac.memento;

import org.daum.library.android.sitac.view.entity.AbstractShapedEntity;
import org.osmdroid.api.IGeoPoint;

public class PointMemento implements IMemento {

	public AbstractShapedEntity entity;
	public IGeoPoint geoP;
	
	public PointMemento(AbstractShapedEntity ent, IGeoPoint geoP) {
		this.entity = ent;
		this.geoP = geoP;
	}
	
	@Override
	public String toString() {
		return entity+" - "+geoP;
	}
}
