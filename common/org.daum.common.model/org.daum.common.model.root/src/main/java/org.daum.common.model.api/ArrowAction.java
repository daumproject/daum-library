package org.daum.common.model.api;

import java.util.ArrayList;

import org.daum.common.gps.api.IGpsPoint;
import org.daum.common.gps.impl.GpsPoint;
import org.daum.library.ormH.annotations.Generated;
import org.daum.library.ormH.annotations.Id;
import org.daum.library.ormH.persistence.GeneratedType;

public class ArrowAction implements IModel {
	
	private static final long serialVersionUID = 7387507491131834415L;

	public enum Type {
		WATER, FIRE, SAP, CHEM
	}

    @Id
    @Generated(strategy = GeneratedType.UUID)
	private String id = "";
	private ArrowAction.Type type;
	private IGpsPoint location;
	private ArrayList<IGpsPoint> points;
	
	public ArrowAction(Type type, GpsPoint location, ArrayList<IGpsPoint> points) {
		this.type = type;
		this.location = location;
		this.points = points;
	}

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

	public Type getType() {
		return type;
	}

    @Override
	public IGpsPoint getLocation() {
		return location;
	}
	
	public ArrayList<IGpsPoint> getPoints() {
		return points;
	}

	@Override
	public void setLocation(IGpsPoint gpsPoint) {
		this.location = gpsPoint;
	}
}
