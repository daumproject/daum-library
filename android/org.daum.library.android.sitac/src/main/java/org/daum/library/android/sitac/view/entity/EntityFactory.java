package org.daum.library.android.sitac.view.entity;

import org.daum.common.gps.api.IGpsPoint;
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

	private DemandEntity build(Demand d) {
		VehicleType type = d.getType();
		VehicleSector sector = VehicleType.getSector(type);
		// if the gh_engage is not set, then it's a dotted picto
		boolean dotted = (d.getGh_engage() == null) ? true : false;
		String iconPath = getIconPath(sector, dotted);
		Drawable icon = DrawableFactory.buildDrawable(ctx, iconPath);
		String number = (d.getNumber() == null) ? "" : d.getNumber();
		DemandEntity e = new DemandEntity(icon, type.name(), number);
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
	
	private String getIconPath(VehicleSector sector, boolean isDotted) {
		String iconPath = "";
		String dotted = (isDotted) ? "dotted_" : "";
		
		switch (sector) {
			case SAP:
				iconPath = "/images/picto_green_"+dotted+"agres.png";
				break;
			case ALIM:
				iconPath = "/images/picto_blue_"+dotted+"agres.png";
				break;
			case INC:
				iconPath = "/images/picto_red_"+dotted+"agres.png";
				break;
			case CHEM:
				iconPath = "/images/picto_orange_"+dotted+"agres.png";
				break;
			case COM:
				iconPath = "/images/picto_violet_"+dotted+"agres.png";
				break;
			case RTN:
				iconPath = "/images/picto_black_"+dotted+"agres.png";
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
