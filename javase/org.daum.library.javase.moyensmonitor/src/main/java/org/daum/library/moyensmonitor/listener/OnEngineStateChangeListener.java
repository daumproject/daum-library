package org.daum.library.moyensmonitor.listener;

import org.daum.common.model.api.Demand;

import java.util.Collection;

public interface OnEngineStateChangeListener {
	
	/**
	 * Called when the data has been changed
	 * 
	 * @param demands all the demands
	 */
	void doUpdate(Collection<Demand> demands);

	/**
	 * Called when a model object has been deleted from the engine
	 * 
	 * @param d the demand deleted
	 */
	void onDelete(Demand d);
}
