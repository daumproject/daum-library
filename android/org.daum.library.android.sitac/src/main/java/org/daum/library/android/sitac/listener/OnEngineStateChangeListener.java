package org.daum.library.android.sitac.listener;

import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.view.entity.IEntity;

import java.util.ArrayList;

public interface OnEngineStateChangeListener {

	/**
	 * Called when a new model object is added to the replica
     * by the local node
	 * 
	 * @param m the new model added
	 * @param e the entity to add
	 */
	void onLocalAdd(IModel m, IEntity e);
	
	/**
	 * Called when a new model object is updated in the replica
     * by the local node
	 * 
	 * @param m the updated model
	 * @param e the entity to update
	 */
	void onLocalUpdate(IModel m, IEntity e);

	/**
	 * Called when a model object has been deleted in the replica
     * by the local node
	 *
     * @param m the deleted model
	 * @param e the entity to delete
	 */
	void onLocalDelete(IModel m, IEntity e);

    /**
     * Called when a new model object is added to the replica
     * by another node
     *
     * @param m the new model added
     */
    void onRemoteAdd(IModel m);

    /**
     * Called when a new model object is updated in the replica
     * by another node
     *
     * @param m the updated model
     */
    void onRemoteUpdate(IModel m);

    /**
     * Called when a model object has been deleted from the replica
     * by another node
     *
     * @param m the deleted model
     */
    void onRemoteDelete(IModel m);

    /**
     * Called when the engine is completly synced with his replica
     * @param data the full IModel list from the replica
     */
    void onReplicaSynced(ArrayList<IModel> data);
}
