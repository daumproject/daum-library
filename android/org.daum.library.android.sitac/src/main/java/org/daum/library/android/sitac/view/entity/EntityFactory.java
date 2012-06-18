package org.daum.library.android.sitac.view.entity;

import java.util.Date;

import org.daum.common.gps.api.IGpsPoint;
import org.daum.common.gps.impl.GpsPoint;
import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.IModel;
import org.daum.common.model.api.Target;
import org.daum.common.model.api.VehicleSector;
import org.daum.common.model.api.VehicleType;
import org.daum.library.android.sitac.view.DrawableFactory;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;


public class EntityFactory implements IEntityFactory {
	
	private static final String TAG = "EntityFactory";
	
	private Context ctx;
	
	public EntityFactory(Context ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public IEntity build(IModel m) {
		if (m instanceof Demand) return build((Demand) m);
		else if (m instanceof Target) return build((Target) m);
		else if (m instanceof Danger) return build((Danger) m);
		else Log.w(TAG, "build("+m.getClass().getSimpleName()+") >> Don't know what to do with that dude, sorry");
		return null;
	}
	
	@Override
	public IModel build(IEntity e) {
		if (e instanceof DemandEntity) return build((DemandEntity) e);
		else if (e instanceof TargetEntity) return build((TargetEntity) e);
		else if (e instanceof DangerEntity) return build((DangerEntity) e);
		else Log.w(TAG, "build("+e.getClass().getSimpleName()+") >> Don't know what to do with that dude, sorry");
		return null;
	}

	private DemandEntity build(Demand d) {
		VehicleType type = d.getType();
		VehicleSector sector = VehicleType.getSector(type);
		String iconPath = getIconPath(sector);
		Drawable icon = DrawableFactory.buildDrawable(ctx, iconPath);
		DemandEntity e = new DemandEntity(icon, type.name());
		IGpsPoint location = d.getLocation();
		if (location != null) {
			IGeoPoint geoP = new GeoPoint(location.getLat(), location.getLong_());
			e.setGeoPoint(geoP);
		}
		return e;
	}
	
	private DangerEntity build(Danger d) {
		Danger.Type type = d.getType();
		String iconPath = getIconPath(type);
		Drawable icon = DrawableFactory.buildDrawable(ctx, iconPath);
		String name;
		switch (type) {
			case WATER:
				name = "Eau";
				break;
			case CHEM:
				name = "Risques particuliers";
				break;
			case FIRE:
				name = "Incendie";
				break;
			default:
				name = type.name();
		}
		
		DangerEntity e = new DangerEntity(icon, name);
		IGeoPoint geoP = new GeoPoint(d.getLocation().getLat(), d.getLocation().getLong_());
		e.setGeoPoint(geoP);
		return e;
	}
	
	private TargetEntity build(Target t) {
		Target.Type type = t.getType();
		String iconPath = getIconPath(type);
		Drawable icon = DrawableFactory.buildDrawable(ctx, iconPath);
		String name;
		switch (type) {
			case WATER:
				name = "Eau";
				break;
			case CHEM:
				name = "Risque particulier";
				break;
			case FIRE:
				name = "Incendie";
				break;
			case VICTIM:
				name = "Personne";
				break;
			default:
				name = type.name();
		}
		
		TargetEntity e = new TargetEntity(icon, name);
		IGeoPoint geoP = new GeoPoint(t.getLocation().getLat(), t.getLocation().getLong_());
		e.setGeoPoint(geoP);
		return e;
	}
	
	private Demand build(DemandEntity ent) {
		VehicleType type = VehicleType.valueOf(ent.getMessage());
		IGeoPoint geoP = ent.getGeoPoint();
		GpsPoint location = new GpsPoint(geoP.getLatitudeE6(), geoP.getLongitudeE6());
		Demand d = new Demand(type, "", "", new Date(), null, null, null, null, location);
		return d;
	}
	
	private Danger build(DangerEntity ent) {
		IGeoPoint geoP = ent.getGeoPoint();
		GpsPoint location = new GpsPoint(geoP.getLatitudeE6(), geoP.getLongitudeE6());
		Danger.Type type = Danger.Type.WATER;
		if (ent.getMessage().equals("Incendie")) {
			type = Danger.Type.FIRE;
		} else if (ent.getMessage().equals("Risques particuliers")) {
			type = Danger.Type.CHEM;
		}
		Danger d = new Danger(type, location);
		return d;
	}

	private Target build(TargetEntity ent) {
		IGeoPoint geoP = ent.getGeoPoint();
		GpsPoint location = new GpsPoint(geoP.getLatitudeE6(), geoP.getLongitudeE6());
		Target.Type type = Target.Type.WATER;
		if (ent.getMessage().equals("Incendie")) {
			type = Target.Type.FIRE;
		} else if (ent.getMessage().equals("Risque particulier")) {
			type = Target.Type.CHEM;
		} else if (ent.getMessage().equals("Personne")) {
			type = Target.Type.VICTIM;
		}
		Target t = new Target(type, location);
		return t;
	}
	
	private String getIconPath(VehicleSector sector) {
		String iconPath = "";
		switch (sector) {
			case SAP:
				iconPath = "/images/picto_green_dotted_agres.png";
				break;
			case ALIM:
				iconPath = "/images/picto_blue_dotted_agres.png";
				break;
			case INC:
				iconPath = "/images/picto_red_dotted_agres.png";
				break;
			case CHEM:
				iconPath = "/images/picto_orange_dotted_agres.png";
				break;
			case COM:
				iconPath = "/images/picto_violet_dotted_agres.png";
				break;
			case RTN:
				iconPath = "/images/picto_black_dotted_agres.png";
				break;
		}
		return iconPath;
	}
	
	private String getIconPath(Danger.Type dangerType) {
		String iconPath = "";
		switch (dangerType) {
			case WATER:
				iconPath = "/images/picto_blue_up.png";
				break;
			case FIRE:
				iconPath = "/images/picto_red_up.png";
				break;
			case CHEM:
				iconPath = "/images/picto_orange_up.png";
				break;
		}
		return iconPath;
	}
	
	private String getIconPath(Target.Type targetType) {
		String iconPath = "";
		switch (targetType) {
			case WATER:
				iconPath = "/images/picto_blue_down.png";
				break;
			case FIRE:
				iconPath = "/images/picto_red_down.png";
				break;
			case CHEM:
				iconPath = "/images/picto_orange_down.png";
				break;
			case VICTIM:
				iconPath = "/images/picto_green_down.png";
				break;
		}
		return iconPath;
	}
}
