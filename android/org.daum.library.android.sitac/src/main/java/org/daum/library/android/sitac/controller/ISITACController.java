package org.daum.library.android.sitac.controller;

import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.Target;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 15/06/12
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public interface ISITACController {

    /**
     * The map will be centered automatically to the device current position
     *
     * @param minTime
     *            the minimum time interval for notifications, in milliseconds.
     *            This field is only used as a hint to conserve power, and
     *            actual time between location updates may be greater or lesser
     *            than this value.
     * @param minDistance
     *            the minimum distance interval for notifications, in meters
     */
    public void requestLocationUpdates(long minTime, long minDistance);

    /**
     * Removes the registration for location updates of the current SITACView.
     * Following this call, updates will no longer occur for the map.
     */
    public void removeLocationUpdates();

    /**
     * Add a new Demand to the engine associated with the controller
     * @param d
     */
    public void addDemand(Demand d);

    /**
     * * Add a new Danger to the engine associated with the controller
     * @param d
     */
    public void addDanger(Danger d);

    /**
     * Add a new Target to the engine associated with the controller
     * @param t
     */
    public void addTarget(Target t);
}