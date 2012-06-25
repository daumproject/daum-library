package org.daum.library.android.sitac.listener;

import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.view.entity.IEntity;

public interface OnEngineStateChangeListener {

	/**
	 * Called when a new model object is added to the engine
	 * 
	 * @param m the new model added
	 * @param e the entity to add
	 */
	void onAdd(IModel m, IEntity e);
	
	/**
	 * Called when a new model object is updated in the engine
	 * 
	 * @param m the updated model
	 * @param e the entity to update
	 */
	void onUpdate(IModel m, IEntity e);

	/**
	 * Called when a model object has been deleted from the engine
	 *
     * @param m the deleted model
	 * @param e the entity to delete
	 */
	void onDelete(IModel m, IEntity e);
}
