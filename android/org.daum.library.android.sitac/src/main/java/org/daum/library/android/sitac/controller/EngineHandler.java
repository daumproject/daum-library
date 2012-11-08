package org.daum.library.android.sitac.controller;

import java.util.*;

import org.daum.common.genmodel.impl.AgentImpl;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;
import org.daum.library.android.sitac.view.SITACMapView;
import org.daum.library.android.sitac.view.SITACMenuView;
import org.daum.library.android.sitac.view.entity.DemandEntity;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.entity.IEntityFactory;

import org.daum.library.android.sitac.visitor.EntityUpdateVisitor;
import org.daum.library.android.sitac.visitor.IVisitor;
import org.daum.common.genmodel.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Handle engine's events and updates views accordingly
 * This class should be used as a delegate of the SITACController
 * @author max
 *
 */
public class EngineHandler implements OnEngineStateChangeListener {
	
	private static final String TAG = EngineHandler.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(EngineHandler.class);

	private SITACMapView mapView;
	private SITACMenuView menuView;
	private IEntityFactory factory;
    private IVisitor visitor;
	private Map<IEntity, IModel> modelMap;
	
	public EngineHandler(IEntityFactory factory, Hashtable<IEntity, IModel> modelMap) {
		this.factory = factory;
		this.modelMap = Collections.synchronizedMap(modelMap);
        this.visitor = new EntityUpdateVisitor();
	}

    @Override
    public void onAdd(IModel m) {
        logger.debug(TAG+" onAdd(IModel) "+m);
        if(m instanceof AgentImpl){
            logger.debug(TAG+" onAdd(IModel) "+((AgentImpl) m).getMatricule()+" "+((AgentImpl) m).getPosRef());
        }
        processModel(m);
    }

    @Override
    public void onUpdate(IModel m) {
        logger.debug(TAG+ " onUpdate(IModel) "+m);
        processModel(m);
    }

    @Override
    public void onUpdateAll(ArrayList<IModel> data) {
        logger.debug(TAG+ " onUpdateAll(ArrayList<IModel>)");
        mapView.deleteAllEntities();
        menuView.deleteAllEntities();
        modelMap.clear();
        for (IModel m : data) {
            onAdd(m);
        }
    }

    private void processModel(IModel m) {
        synchronized (this) {
            for (IModel model : modelMap.values()) {
                if (model.getId().equals(m.getId()))  {
                    update(m);
                    return;
                }
            }
            add(m);
        }
    }

    @Override
    public void onDelete(String id) {
        logger.debug(TAG+" onDelete(IModel)");
        IEntity e = getEntityWithModelId(id);
        if (e != null) delete(e);
    }

    @Override
    public void onReplicaSynced(ArrayList<IModel> data) {
        logger.debug(TAG+ " onReplicaSynced(ArrayList<IModel>)");
        for (IModel m : data) onUpdate(m);
    }

    private void add(IModel m) {
        logger.debug(TAG+" add(IModel) with brand new entity");
        IEntity e = factory.build(m);
        modelMap.put(e, m);
        e.setState(IEntity.State.SAVED);

        logger.debug(TAG+" add entity to the mapView (and menuView if necessary)");
        if (e.getGeoPoint() == null && e instanceof DemandEntity) {
            // in case its a DemandEntity that has no location on map, add it to the menu
            menuView.addEntityWithNoLocation((DemandEntity) e);
        }
        mapView.addEntity(e);
    }

    private void update(IModel m) {
        logger.debug(TAG+ " update(IModel)");
        IEntity e = getEntityWithModel(m);
        modelMap.put(e, m); // update model in modelMap
        e.setState(IEntity.State.SAVED);

        // TODO problem if remote node0 deletes entity0 and this node1
        // moves entity0 from x,y to x1,y1
        // then the entity is destroyed on node0 but moved on node1
        if (!mapView.hasEntity(e)) mapView.addEntity(e);

        // update the entity according to the model given
        e.accept(visitor, m);
    }

    private void delete(IEntity e) {
        logger.debug(TAG+" delete(IEntity)");
        modelMap.remove(e);
        mapView.deleteEntity(e);
    }

    /**
     * Returns the IEntity associated with the IModel in modelMap and
     * replace the old IModel value with the one given in parameter.
     * To find the IModel object it will search based on IModel ids.
     *
     * @param m model you want to find the entity associated with
     * @return IEntity associated with the model
     */
    private IEntity getEntityWithModel(IModel m) {
        Set<Map.Entry<IEntity, IModel>> entries = modelMap.entrySet();
        synchronized (modelMap) {
            for (Map.Entry<IEntity, IModel> entry: entries) {
                if (entry.getValue().getId().equals(m.getId())) {
                    entry.setValue(m); // update model
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    private IEntity getEntityWithModelId(String id) {
        Set<Map.Entry<IEntity, IModel>> entries = modelMap.entrySet();
        synchronized (modelMap) {
            for (Map.Entry<IEntity, IModel> entry: entries) {
                if (entry.getValue().getId().equals(id)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public void registerMenuView(SITACMenuView menuView) {
		this.menuView = menuView;
	}

	public void registerMapView(SITACMapView mapView) {
		this.mapView = mapView;
	}
}
