package org.daum.library.android.moyens.model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 */
public class ResourcesList extends ArrayList<IResource> {

    public ResourcesList getVehicles() {
        ResourcesList list = new ResourcesList();
        for (IResource res : this) {
            if (res instanceof Vehicle) list.add(res);
        }
        return list;
    }

    public ResourcesList getDemands() {
        ResourcesList list = new ResourcesList();
        for (IResource res : this) {
            if (res instanceof Demand) list.add(res);
        }
        return list;
    }
}
