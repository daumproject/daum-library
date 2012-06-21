package org.daum.library.android.sitac.memento;

import org.daum.library.android.sitac.view.entity.ShapedEntity;
import org.osmdroid.api.IGeoPoint;

public class PointMemento implements IMemento {

	public ShapedEntity entity;
	public IGeoPoint geoP;
	
	public PointMemento(ShapedEntity ent, IGeoPoint geoP) {
		this.entity = ent;
		this.geoP = geoP;
	}
	
	@Override
	public String toString() {
		return entity+" - "+geoP;
	}
}
