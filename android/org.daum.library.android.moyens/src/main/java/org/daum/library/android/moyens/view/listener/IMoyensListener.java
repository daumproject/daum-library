package org.daum.library.android.moyens.view.listener;

import org.daum.common.model.api.Demand;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/06/12
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */
public interface IMoyensListener {

    /**
     * Called when a new demand is asked
     * @param newDemand the new demand
     */
    void onDemandAsked(Demand newDemand);

    /**
     * Called when a demand in the list has been updated
     * @param demand the updated demand
     */
    void onDemandUpdated(Demand demand);
}
