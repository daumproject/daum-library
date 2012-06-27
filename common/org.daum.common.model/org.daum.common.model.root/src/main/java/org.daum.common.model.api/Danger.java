package org.daum.common.model.api;

import org.daum.common.gps.api.IGpsPoint;

import org.daum.library.ormH.annotations.Generated;
import org.daum.library.ormH.annotations.Id;
import org.daum.library.ormH.persistence.GeneratedType;

public class Danger implements IModel {

	private static final long serialVersionUID = -4836868718903570353L;

	public enum Type {
		WATER,
		FIRE,
		CHEM
	}

    @Id
    @Generated(strategy = GeneratedType.UUID)
    private String id = "";
	private IGpsPoint location;
	private Danger.Type type;
	
	public Danger(Danger.Type type, IGpsPoint location) {
		this.type = type;
		this.location = location;
	}

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
	public IGpsPoint getLocation() {
		return location;
	}

    @Override
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
