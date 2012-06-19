package org.daum.library.android.sitac.listener;

import org.daum.common.model.api.IModel;

public interface OnEngineStateChangeListener {

	/**
	 * Called when a new model object is added to the engine
	 * 
	 * @param m the new model added
	 */
	void onAdd(IModel m);
	
	/**
	 * Called when a new model object is updated in the engine
	 * 
	 * @param m the updated model
	 */
	void onUpdate(IModel m);

	/**
	 * Called when a model object has been deleted from the engine
	 * 
	 * @param m the deleted model object
	 */
	void onDelete(IModel m);
}
