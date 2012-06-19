package org.daum.common.model.api;

import java.util.ArrayList;

import org.daum.common.gps.impl.GpsPoint;
import org.daum.common.model.api.ArrowAction.Type;

public class ArrowAction implements IModel {
	
	private static final long serialVersionUID = 7387507491131834415L;

	public enum Type {
		WATER, FIRE, SAP, CHEM
	}
	
	private long id;
	private ArrowAction.Type type;
	private GpsPoint location;
	private ArrayList<GpsPoint> points;
	
	public ArrowAction(Type type, GpsPoint location, ArrayList<GpsPoint> points) {
		this.type = type;
		this.location = location;
		this.points = points;
	}

	public Type getType() {
		return type;
	}
	
	public GpsPoint getLocation() {
		return location;
	}
	
	public ArrayList<GpsPoint> getPoints() {
		return points;
	}
}
