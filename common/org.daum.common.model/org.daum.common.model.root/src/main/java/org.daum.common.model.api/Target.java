package org.daum.common.model.api;

import org.daum.common.gps.api.IGpsPoint;

public class Target implements IModel {

	private static final long serialVersionUID = 2254129011593676899L;

	public enum Type {
		WATER,
		FIRE,
		VICTIM,
		CHEM
	}
	
	private IGpsPoint location;
	private Target.Type type;
	
	public Target(Target.Type type, IGpsPoint location) {
		this.type = type;
		this.location = location;
	}

	public IGpsPoint getLocation() {
		return location;
	}

	public void setLocation(IGpsPoint location) {
		this.location = location;
	}

	public Target.Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return type+"_"+location;
	}
}
