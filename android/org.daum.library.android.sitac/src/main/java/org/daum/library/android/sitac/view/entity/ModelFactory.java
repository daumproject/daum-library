package org.daum.library.android.sitac.view.entity;

import java.util.ArrayList;
import java.util.Date;

import org.daum.common.gps.impl.GpsPoint;
import org.daum.common.model.api.ArrowAction;
import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.IModel;
import org.daum.common.model.api.Target;
import org.daum.common.model.api.VehicleType;
import org.osmdroid.api.IGeoPoint;

import android.util.Log;

public class ModelFactory implements IModelFactory {
	
	private static final String TAG = "ModelFactory";

	@Override
	public IModel build(IEntity e) {
		if (e instanceof DemandEntity) return build((DemandEntity) e);
		else if (e instanceof TargetEntity) return build((TargetEntity) e);
		else if (e instanceof DangerEntity) return build((DangerEntity) e);
		else if (e instanceof ArrowEntity) return build((ArrowEntity) e);
		else Log.w(TAG, "build("+e.getClass().getSimpleName()+") >> Don't know what to do with that dude, sorry");
		return null;
	}

	private Demand build(DemandEntity ent) {
		VehicleType type = VehicleType.valueOf(ent.getType());
		IGeoPoint geoP = ent.getGeoPoint();
		GpsPoint location = new GpsPoint(geoP.getLatitudeE6(), geoP.getLongitudeE6());
		Demand d = new Demand(type, ent.getMessage(), "", new Date(), null, null, null, null, location);
		return d;
	}
	
	private Danger build(DangerEntity ent) {
		IGeoPoint geoP = ent.getGeoPoint();
		GpsPoint location = new GpsPoint(geoP.getLatitudeE6(), geoP.getLongitudeE6());
		Danger.Type type = Danger.Type.WATER;
		if (ent.getType().equals("Incendie")) {
			type = Danger.Type.FIRE;
		} else if (ent.getType().equals("Risques particuliers")) {
			type = Danger.Type.CHEM;
		}
		Danger d = new Danger(type, location);
		return d;
	}
	
	private ArrowAction build(ArrowEntity ent) {
		IGeoPoint geoP = ent.getGeoPoint();
		GpsPoint location = new GpsPoint(geoP.getLatitudeE6(), geoP.getLongitudeE6());
		ArrayList<IGeoPoint> geoPts = ent.getPoints();
		ArrayList<GpsPoint> points = new ArrayList<GpsPoint>();
		for (IGeoPoint gp : geoPts) {
			points.add(new GpsPoint(gp.getLatitudeE6(), gp.getLongitudeE6()));
		}
		
		ArrowAction.Type type = ArrowAction.Type.WATER;
		if (ent.getType().equals("Extinction")) {
			type = ArrowAction.Type.FIRE;
		} else if (ent.getType().equals("Secours à personnes")) {
			type = ArrowAction.Type.SAP;
		} else if (ent.getType().equals("Risques particuliers")) {
			type = ArrowAction.Type.CHEM;
		}
		ArrowAction a = new ArrowAction(type, location, points);
		return a;
	}

	private Target build(TargetEntity ent) {
		IGeoPoint geoP = ent.getGeoPoint();
		GpsPoint location = new GpsPoint(geoP.getLatitudeE6(), geoP.getLongitudeE6());
		Target.Type type = Target.Type.WATER;
		if (ent.getType().equals("Incendie")) {
			type = Target.Type.FIRE;
		} else if (ent.getType().equals("Risque particulier")) {
			type = Target.Type.CHEM;
		} else if (ent.getType().equals("Personne")) {
			type = Target.Type.VICTIM;
		}
		Target t = new Target(type, location);
		return t;
	}
}
