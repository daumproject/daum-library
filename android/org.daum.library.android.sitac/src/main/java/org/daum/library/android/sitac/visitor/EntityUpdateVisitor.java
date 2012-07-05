package org.daum.library.android.sitac.visitor;

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
 * By adding a visit method with a specific XXXEntity related class you
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
        Demand d = (Demand) m;

        // get rid of the entity if gh_desengagement is set
        if (d.getGh_desengagement() != null) {
            // the vehicle left the intervention
            e.destroy();
            return;
        }

        // update the location
        if (m.getLocation() != null) {
            IGpsPoint p = m.getLocation();
            IGeoPoint geoP = new GeoPoint(p.getLat(), p.getLong_());
            e.setGeoPoint(geoP);
        }

        // change the demand icon from dotted to normal if gh_Engage is set
        if (d.getGh_engage() == null) {
            // we should use dotted icons
            e.setIcon(EntityFactory.getIcon(VehicleType.getSector(d.getType()), true));

        } else {
            e.setIcon(EntityFactory.getIcon(VehicleType.getSector(d.getType()), false));
        }

        // add the number if any is set
        if (d.getNumber() != null) {
            e.setMessage(d.getNumber());
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
