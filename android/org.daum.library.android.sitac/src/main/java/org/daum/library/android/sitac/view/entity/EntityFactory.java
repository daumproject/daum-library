package org.daum.library.android.sitac.view.entity;

import org.daum.library.android.sitac.view.DrawableFactory;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import org.sitac.*;


public class EntityFactory implements IEntityFactory {
	
	private static final String TAG = "EntityFactory";
	
	private static Context ctx;
	
	public EntityFactory(Context ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public IEntity build(IModel m) {
		if (m instanceof Vehicule) return build((Vehicule) m);
		else if (m instanceof Cible) return build((Cible) m);
		else if (m instanceof SourceDanger) return build((SourceDanger) m);
		else if (m instanceof ArrowAction) return build((ArrowAction) m);
		else if (m instanceof ZoneAction) return build((ZoneAction) m);
		else Log.e(TAG, "build("+m.getClass().getSimpleName()+") >> Don't know what to do with that dude, sorry");
		return null;
	}

	private DemandEntity build(Vehicule d) {
		VehiculeType type = d.getVehiculeType();
		VehiculeSector sector = VehiculeType.getSector(type);
		// if the gh_engage is not set, then it's a dotted picto
		boolean dotted = (d.getGh_engage() == null) ? true : false;
		Drawable icon = getIcon(sector, dotted);
		String number = (d.getNumber() == null) ? "" : d.getNumber();
		DemandEntity e = new DemandEntity(icon, type.name(), number);
		GpsPoint location = d.getLocation();
		if (location != null) {
			IGeoPoint geoP = new GeoPoint(location.getLat(), location.getLong());
			e.setGeoPoint(geoP);
		}
		return e;
	}
	
	private DangerEntity build(SourceDanger d) {
		DangerType type = d.getType();
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
		IGeoPoint geoP = new GeoPoint(d.getLocation().getLat(), d.getLocation().getLong());
		e.setGeoPoint(geoP);
		return e;
	}
	
	private TargetEntity build(Cible t) {
        CibleType type = t.getType();
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
		IGeoPoint geoP = new GeoPoint(t.getLocation().getLat(), t.getLocation().getLong());
		e.setGeoPoint(geoP);
		return e;
	}
	
	private ArrowEntity build(ArrowAction a) {
		ArrowActionType type = a.getActionType();
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
		if (a.getLocation() != null) {
            e.setGeoPoint(new GeoPoint(a.getLocation().getLat(), a.getLocation().getLong()));
        }
		for (GpsPoint gpsPt : a.getPoints()) {
			e.addPoint(new GeoPoint(gpsPt.getLat(), gpsPt.getLong()));
		}
		return e;
	}
	
	private ZoneEntity build(ZoneAction z) {
		Drawable icon = DrawableFactory.build(ctx, DrawableFactory.PICTO_ZONE);
		String name = "Zone";
		int color = Color.argb(60, 0, 0, 0); // black with a lot of transparence

		ZoneEntity e = new ZoneEntity(icon, name, color);
		if (z.getLocation() != null) {
            e.setGeoPoint(new GeoPoint(z.getLocation().getLat(), z.getLocation().getLong()));
        }
		for (GpsPoint gpsPt : z.getPoints()) {
			e.addPoint(new GeoPoint(gpsPt.getLat(), gpsPt.getLong()));
		}
		return e;
	}
	
	public static Drawable getIcon(ArrowActionType type) {
		switch (type) {
			case WATER:
				return DrawableFactory.build(ctx, DrawableFactory.PICTO_LINE_BLUE);
				
			case FIRE:
				return DrawableFactory.build(ctx, DrawableFactory.PICTO_LINE_RED);
				
			case CHEM:
				return DrawableFactory.build(ctx, DrawableFactory.PICTO_LINE_ORANGE);
				
			case SAP:
				return DrawableFactory.build(ctx, DrawableFactory.PICTO_LINE_GREEN);
				
			default:
				return null;
		}
	}

    public static Drawable getIcon(VehiculeSector sector, boolean isDotted) {
		switch (sector) {
			case SAP:
				if (isDotted) return DrawableFactory.build(ctx, DrawableFactory.PICTO_GREEN_DOTTED_AGRES); 
				else return DrawableFactory.build(ctx, DrawableFactory.PICTO_GREEN_AGRES);
				
			case ALIM:
				if (isDotted) return DrawableFactory.build(ctx, DrawableFactory.PICTO_BLUE_DOTTED_AGRES); 
				else return DrawableFactory.build(ctx, DrawableFactory.PICTO_BLUE_AGRES);
				
			case INC:
				if (isDotted) return DrawableFactory.build(ctx, DrawableFactory.PICTO_RED_DOTTED_AGRES); 
				else return DrawableFactory.build(ctx, DrawableFactory.PICTO_RED_AGRES);
				
			case CHEM:
				if (isDotted) return DrawableFactory.build(ctx, DrawableFactory.PICTO_ORANGE_DOTTED_AGRES); 
				else return DrawableFactory.build(ctx, DrawableFactory.PICTO_ORANGE_AGRES);
				
			case COM:
				if (isDotted) return DrawableFactory.build(ctx, DrawableFactory.PICTO_VIOLET_DOTTED_AGRES); 
				else return DrawableFactory.build(ctx, DrawableFactory.PICTO_VIOLET_AGRES);
				
			case RTN:
				if (isDotted) return DrawableFactory.build(ctx, DrawableFactory.PICTO_BLACK_DOTTED_AGRES); 
				else return DrawableFactory.build(ctx, DrawableFactory.PICTO_BLACK_AGRES);
				
			default:
				return null;
		}
	}

    public static Drawable getIcon(DangerType type) {
		switch (type) {
			case WATER:
				return DrawableFactory.build(ctx, DrawableFactory.PICTO_BLUE_UP);
				
			case FIRE:
				return DrawableFactory.build(ctx, DrawableFactory.PICTO_RED_UP);
				
			case CHEM:
				return DrawableFactory.build(ctx, DrawableFactory.PICTO_ORANGE_UP);
				
			default:
				return null;
		}
	}

    public static Drawable getIcon(CibleType type) {
		switch (type) {
			case WATER:
				return DrawableFactory.build(ctx, DrawableFactory.PICTO_BLUE_DOWN);
				
			case FIRE:
				return DrawableFactory.build(ctx, DrawableFactory.PICTO_RED_DOWN);
				
			case CHEM:
				return DrawableFactory.build(ctx, DrawableFactory.PICTO_ORANGE_DOWN);
				
			case VICTIM:
				return DrawableFactory.build(ctx, DrawableFactory.PICTO_GREEN_DOWN);
				
			default:
				return null;
		}
	}
}
