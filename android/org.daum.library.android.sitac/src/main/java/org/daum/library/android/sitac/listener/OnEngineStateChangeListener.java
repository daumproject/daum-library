package org.daum.library.android.sitac.listener;

import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.Target;

public interface OnEngineStateChangeListener {

	/**
	 * Called when a new Demand is added to the engine
	 * @param d the new demand
	 */
	void onDemandAdded(Demand d);
	
	/**
	 * Called when a new Danger is added to the engine
	 * @param d the new danger
	 */
	void onDangerAdded(Danger d);

	/**
	 * Called when a new Target is added to the engine
	 * @param t the new target
	 */
	void onTargetAdded(Target t);
	
	/**
	 * Called when a demand is updated in the engine
	 * @param d the updated demand
	 */
	void onDemandUpdated(Demand d);

    /**
     * Called when a target is updated in the engine
     * @param t the updated target
     */
    void onTargetUpdated(Target t);

    /**
     * Called when a danger is updated in the engine
     * @param d the updated danger
     */
    void onDangerUpdated(Danger d);
}
