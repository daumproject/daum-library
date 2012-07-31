package org.daum.library.android.sitac.listener;

import org.daum.common.genmodel.*;
import java.util.ArrayList;

public interface OnEngineStateChangeListener {

    /**
     * Called when a new model object is added to the replica
     *
     * @param m the new model added
     */
    void onAdd(IModel m);

    /**
     * Called when a new model object is updated in the replica
     *
     * @param m the updated model
     */
    void onUpdate(IModel m);

    /**
     * Called when a model object has been deleted from the replica
     *
     * @param id id of the deleted model
     */
    void onDelete(String id);

    /**
     * Called when the engine is completly synced with his replica
     * @param data the full IModel list from the replica
     */
    void onReplicaSynced(ArrayList<IModel> data);

    /**
     * Called when the engine has a new listener set
     *
     * @param data the full IModel list from the replica
     */
    void onUpdateAll(ArrayList<IModel> data);
}
