package org.daum.library.android.sitac.visitor;

import android.util.Log;
import org.daum.common.gps.api.IGpsPoint;
import org.daum.common.model.api.IModel;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.VehicleType;
import org.daum.library.android.sitac.view.entity.DemandEntity;
import org.daum.library.android.sitac.view.entity.EntityFactory;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

/**
 * EntityUpdateVisitor handles entities updates.
 * The purpose of this class is to gather every entity update work
 * in one and only location to make life easier for developers.
 *
 * By adding a visit method with a specific XXEntity related class you
 * will be able to make specific processes for that very entity update work.
 *
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 27/06/12
 * Time: 09:58
 *
 */
public class EntityUpdateVisitor implements IVisitor {

    private static final String TAG = "EntityUpdateVisitor";

    @Override
    public void visit(DemandEntity e, IModel m) {
        // update the location
        if (m.getLocation() != null) {
            IGpsPoint p = m.getLocation();
            IGeoPoint geoP = new GeoPoint(p.getLat(), p.getLong_());
            e.setGeoPoint(geoP);
        }

        // make some special process for DemandEntity
        Demand d = (Demand) m;
        if (d.getGh_engage() == null) {
            // we should use dotted icons
            e.setIcon(EntityFactory.getIcon(VehicleType.getSector(d.getType()), true));

        } else {
            e.setIcon(EntityFactory.getIcon(VehicleType.getSector(d.getType()), false));
        }

        if (d.getGh_desengagement() != null) {
            // the vehicle left the intervention
            e.setGeoPoint(null);
            Log.d(TAG, "geoPoint in visitor set to null for entity, it should not be displayed after that");
        }
    }

    @Override
    public void visit(IEntity e, IModel m) {
        if (m.getLocation() != null) {
            IGpsPoint p = m.getLocation();
            IGeoPoint geoP = new GeoPoint(p.getLat(), p.getLong_());
            e.setGeoPoint(geoP);
        }
    }
}
