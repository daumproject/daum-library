package org.daum.library.android.sitac.view.entity;

import org.daum.common.gps.api.IGpsPoint;
import org.daum.common.model.api.ArrowAction;
import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.IModel;
import org.daum.common.model.api.Target;
import org.daum.common.model.api.VehicleSector;
import org.daum.common.model.api.VehicleType;
import org.daum.common.model.api.ZoneAction;
import org.daum.library.android.sitac.view.DrawableFactory;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

import android.content.Context;
import android.graphics.Color;
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
		else if (m instanceof ArrowAction) return build((ArrowAction) m);
		else if (m instanceof ZoneAction) return build((ZoneAction) m);
		else Log.w(TAG, "build("+m.getClass().getSimpleName()+") >> Don't know what to do with that dude, sorry");
		return null;
	}

	private DemandEntity build(Demand d) {
		VehicleType type = d.getType();
		VehicleSector sector = VehicleType.getSector(type);
		// if the gh_engage is not set, then it's a dotted picto
		boolean dotted = (d.getGh_engage() == null) ? true : false;
		Drawable icon = getIcon(sector, dotted);
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
		Drawable icon = getIcon(type);
		String name;
		switch (type) {
			case WATER:
				name = DangerEntity.WATER;
				break;
			case CHEM:
				name = DangerEntity.CHEM;
				break;
			case FIRE:
				name = DangerEntity.FIRE;
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
		Drawable icon = getIcon(type);
		String name;
		switch (type) {
			case WATER:
				name = TargetEntity.WATER;
				break;
			case CHEM:
				name = TargetEntity.CHEM;
				break;
			case FIRE:
				name = TargetEntity.FIRE;
				break;
			case VICTIM:
				name = TargetEntity.VICTIM;
				break;
			default:
				name = type.name();
		}
		
		TargetEntity e = new TargetEntity(icon, name);
		IGeoPoint geoP = new GeoPoint(t.getLocation().getLat(), t.getLocation().getLong_());
		e.setGeoPoint(geoP);
		return e;
	}
	
	private ArrowEntity build(ArrowAction a) {
		ArrowAction.Type type = a.getType();
		Drawable icon = getIcon(type);
		String name;
		int arrowColor;
		switch (type) {
			case CHEM:
				name = ArrowEntity.CHEM;
				arrowColor = Color.rgb(255, 156, 0); // orange
				break;
				
			case FIRE:
				name = ArrowEntity.FIRE;
				arrowColor = Color.RED;
				break;
				
			case SAP:
				name = ArrowEntity.SAP;
				arrowColor = Color.GREEN;
				break;
				
			case WATER:
				name = ArrowEntity.WATER;
				arrowColor = Color.BLUE;
				break;
				
			default:
				name = type.name();
				arrowColor = Color.BLUE;
		}
		ArrowEntity e = new ArrowEntity(icon, name, arrowColor);
		e.setGeoPoint(new GeoPoint(a.getLocation().getLat(), a.getLocation().getLong_()));
		for (IGpsPoint gpsPt : a.getPoints()) {
			e.addPoint(new GeoPoint(gpsPt.getLat(), gpsPt.getLong_()));
		}
		return e;
	}
	
	private ZoneEntity build(ZoneAction z) {
		Drawable icon = DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_ZONE);
		String name = "Zone";
		int color = Color.argb(60, 0, 0, 0); // black with a lot of transparence

		ZoneEntity e = new ZoneEntity(icon, name, color);
		e.setGeoPoint(new GeoPoint(z.getLocation().getLat(), z.getLocation().getLong_()));
		for (IGpsPoint gpsPt : z.getPoints()) {
			e.addPoint(new GeoPoint(gpsPt.getLat(), gpsPt.getLong_()));
		}
		return e;
	}
	
	private Drawable getIcon(ArrowAction.Type type) {
		switch (type) {
			case WATER:
				return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_LINE_BLUE);
				
			case FIRE:
				return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_LINE_RED);
				
			case CHEM:
				return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_LINE_ORANGE);
				
			case SAP:
				return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_LINE_GREEN);
				
			default:
				return null;
		}
	}

	private Drawable getIcon(VehicleSector sector, boolean isDotted) {
		switch (sector) {
			case SAP:
				if (isDotted) return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_GREEN_DOTTED_AGRES); 
				else return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_GREEN_AGRES);
				
			case ALIM:
				if (isDotted) return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_BLUE_DOTTED_AGRES); 
				else return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_BLUE_AGRES);
				
			case INC:
				if (isDotted) return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_RED_DOTTED_AGRES); 
				else return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_RED_AGRES);
				
			case CHEM:
				if (isDotted) return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_ORANGE_DOTTED_AGRES); 
				else return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_ORANGE_AGRES);
				
			case COM:
				if (isDotted) return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_VIOLET_DOTTED_AGRES); 
				else return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_VIOLET_AGRES);
				
			case RTN:
				if (isDotted) return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_BLACK_DOTTED_AGRES); 
				else return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_BLACK_AGRES);
				
			default:
				return null;
		}
	}
	
	private Drawable getIcon(Danger.Type dangerType) {
		switch (dangerType) {
			case WATER:
				return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_BLUE_UP);
				
			case FIRE:
				return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_RED_UP);
				
			case CHEM:
				return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_ORANGE_UP);
				
			default:
				return null;
		}
	}
	
	private Drawable getIcon(Target.Type targetType) {
		switch (targetType) {
			case WATER:
				return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_BLUE_DOWN);
				
			case FIRE:
				return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_RED_DOWN);
				
			case CHEM:
				return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_ORANGE_DOWN);
				
			case VICTIM:
				return DrawableFactory.buildDrawable(ctx, DrawableFactory.PICTO_GREEN_DOWN);
				
			default:
				return null;
		}
	}
}
