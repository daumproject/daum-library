package org.daum.common.model.api;

import org.daum.common.gps.api.IGpsPoint;

public class Danger implements IModel {

	private static final long serialVersionUID = -4836868718903570353L;

	public enum Type {
		WATER,
		FIRE,
		CHEM
	}
	
	private IGpsPoint location;
	private Danger.Type type;
	
	public Danger(Danger.Type type, IGpsPoint location) {
		this.type = type;
		this.location = location;
	}

	public IGpsPoint getLocation() {
		return location;
	}

	public void setLocation(IGpsPoint location) {
		this.location = location;
	}

	public Danger.Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return type+"_"+location;
	}
}
