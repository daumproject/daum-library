package org.daum.library.android.sitac.model;

import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.Target;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 15/06/12
 * Time: 14:09
 * To change this template use File | Settings | File Templates.
 */
public interface ISITACEngine {

    public void addDemand(Demand d);

    public void addDanger(Danger d);

    public void addTarget(Target d);

    public void updateDemand(Demand d);
}
