package org.daum.common.model.api;

import java.util.ArrayList;

import org.daum.common.gps.api.IGpsPoint;
import org.daum.library.ormH.annotations.Generated;
import org.daum.library.ormH.annotations.Id;
import org.daum.library.ormH.persistence.GeneratedType;

public class ZoneAction implements IModel {
	
	private static final long serialVersionUID = 8373352154580203071L;
	
	@Id
	@Generated(strategy = GeneratedType.UUID)
	private String id = "";
	private IGpsPoint location;
	private ArrayList<IGpsPoint> points;
	
	public ZoneAction(IGpsPoint location, ArrayList<IGpsPoint> points) {
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

	@Override
	public void setLocation(IGpsPoint gpsPoint) {
		this.location = gpsPoint;
	}
	
	public IGpsPoint getLocation() {
		return location;
	}

	public ArrayList<IGpsPoint> getPoints() {
		return points;
	}
	
	@Override
	public String toString() {
		String str = "";
		for (IGpsPoint p : points) {
			str += p.toString()+", ";
		}
		return str;
	}
}
