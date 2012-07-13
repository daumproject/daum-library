package org.daum.library.android.sitac.view.entity;

import java.util.ArrayList;
import java.util.Date;

import org.osmdroid.api.IGeoPoint;

import android.util.Log;
import org.sitac.*;

public class ModelFactory implements IModelFactory {
	
	private static final String TAG = "ModelFactory";

	@Override
	public IModel build(IEntity e) {
		if (e instanceof DemandEntity) return build((DemandEntity) e);
		else if (e instanceof TargetEntity) return build((TargetEntity) e);
		else if (e instanceof DangerEntity) return build((DangerEntity) e);
		else if (e instanceof ZoneEntity) return build((ZoneEntity) e);
		else if (e instanceof ArrowEntity) return build((ArrowEntity) e);
		else Log.w(TAG, "build("+e.getClass().getSimpleName()+") >> Don't know what to do with that dude, sorry");
		return null;
	}

	private Vehicule build(DemandEntity ent) {
		VehiculeType type = VehiculeType.valueOf(ent.getType());
		IGeoPoint geoP = ent.getGeoPoint();
		GpsPoint location = SitacFactory.createGpsPoint();
        location.setLat(geoP.getLatitudeE6());
        location.setLong(geoP.getLongitudeE6());
        Vehicule demand = SitacFactory.createVehicule();
        demand.setVehiculeType(type);
        demand.setNumber(ent.getMessage());
        demand.setCis("");
        demand.setGh_demande(new Date());
        demand.setLocation(location);
		return demand;
	}
	
	private SourceDanger build(DangerEntity ent) {
		IGeoPoint geoP = ent.getGeoPoint();
        GpsPoint location = SitacFactory.createGpsPoint();
        location.setLat(geoP.getLatitudeE6());
        location.setLong(geoP.getLongitudeE6());
		DangerType type = DangerType.WATER;
		if (ent.getType().equals(DangerEntity.FIRE)) {
			type = DangerType.FIRE;
		} else if (ent.getType().equals(DangerEntity.CHEM)) {
			type = DangerType.CHEM;
		}
        SourceDanger d = SitacFactory.createSourceDanger();
        d.setType(type);
        d.setLocation(location);
		return d;
	}
	
	private ArrowAction build(ArrowEntity ent) {
		IGeoPoint geoP = ent.getGeoPoint();
        GpsPoint location = null;
        if (geoP != null) {
            location = SitacFactory.createGpsPoint();
            location.setLat(geoP.getLatitudeE6());
            location.setLong(geoP.getLongitudeE6());
        }
		ArrayList<IGeoPoint> geoPts = ent.getPoints();
		ArrayList<GpsPoint> points = new ArrayList<GpsPoint>();
		for (IGeoPoint gp : geoPts) {
            GpsPoint gpsPt = SitacFactory.createGpsPoint();
            gpsPt.setLat(gp.getLatitudeE6());
            gpsPt.setLong(gp.getLongitudeE6());
			points.add(gpsPt);
		}
		
		ArrowActionType type = ArrowActionType.WATER;
		if (ent.getType().equals("Extinction")) {
			type = ArrowActionType.FIRE;
		} else if (ent.getType().equals(ArrowEntity.SAP)) {
			type = ArrowActionType.SAP;
		} else if (ent.getType().equals(ArrowEntity.CHEM)) {
			type = ArrowActionType.CHEM;
		}
		ArrowAction a = SitacFactory.createArrowAction();
        a.setActionType(type);
        a.setLocation(location);
        a.setPoints(points);
		return a;
	}
	
	private ZoneAction build(ZoneEntity ent) {
		ArrayList<IGeoPoint> geoPts = ent.getPoints();
        ArrayList<GpsPoint> points = new ArrayList<GpsPoint>();
        for (IGeoPoint gp : geoPts) {
            GpsPoint gpsPt = SitacFactory.createGpsPoint();
            gpsPt.setLat(gp.getLatitudeE6());
            gpsPt.setLong(gp.getLongitudeE6());
            points.add(gpsPt);
        }
		IGeoPoint geoP = ent.getGeoPoint();
        GpsPoint location = null;
        if (geoP != null) {
            location = SitacFactory.createGpsPoint();
            location.setLat(geoP.getLatitudeE6());
            location.setLong(geoP.getLongitudeE6());
        }
        ZoneAction z = SitacFactory.createZoneAction();
        z.setLocation(location);
        z.setPoints(points);
		return z;
	}

	private Cible build(TargetEntity ent) {
		IGeoPoint geoP = ent.getGeoPoint();
		GpsPoint location = SitacFactory.createGpsPoint();
        location.setLat(geoP.getLatitudeE6());
        location.setLong(geoP.getLongitudeE6());
        CibleType type = CibleType.WATER;
		if (ent.getType().equals(TargetEntity.FIRE)) {
			type = CibleType.FIRE;
		} else if (ent.getType().equals(TargetEntity.CHEM)) {
			type = CibleType.CHEM;
		} else if (ent.getType().equals(TargetEntity.VICTIM)) {
			type = CibleType.VICTIM;
		}
        Cible c = SitacFactory.createCible();
        c.setType(type);
        c.setLocation(location);
		return c;
	}
}
