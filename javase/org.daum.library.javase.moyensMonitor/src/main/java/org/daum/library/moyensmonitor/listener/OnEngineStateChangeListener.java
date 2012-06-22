package org.daum.library.moyensmonitor.listener;

import org.daum.common.model.api.Demand;

public interface OnEngineStateChangeListener {

	/**
	 * Called when a new model object is added to the engine
	 * 
	 * @param d the new demand added
	 */
	void onAdd(Demand d);
	
	/**
	 * Called when a new model object is updated in the engine
	 * 
	 * @param d the updated demand
	 */
	void onUpdate(Demand d);

	/**
	 * Called when a model object has been deleted from the engine
	 * 
	 * @param d the demand deleted
	 */
	void onDelete(Demand d);
}
