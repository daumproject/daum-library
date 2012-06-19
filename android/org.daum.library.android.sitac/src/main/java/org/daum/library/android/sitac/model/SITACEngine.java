package org.daum.library.android.sitac.model;

import java.util.ArrayList;

import org.daum.common.model.api.ArrowAction;
import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.IModel;
import org.daum.common.model.api.Target;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;
import org.daum.library.android.sitac.view.entity.IEntity;

import android.util.Log;

/**
 * Here you should save/update/delete data from the Replica
 */
public class SITACEngine {
	
	private static final String TAG = "SITACEngine";

	private ArrayList<Demand> demands;
	private ArrayList<Danger> dangers;
	private ArrayList<Target> targets;
	private ArrayList<ArrowAction> arrowActions;
	private OnEngineStateChangeListener listener;
	
	public SITACEngine(OnEngineStateChangeListener engineHandler) {
		this.demands = new ArrayList<Demand>();
		this.dangers = new ArrayList<Danger>();
		this.targets = new ArrayList<Target>();
		this.arrowActions = new ArrayList<ArrowAction>();
		listener = engineHandler;
	}
	
	public void add(IModel m, IEntity e) {
		if (m instanceof Demand) addDemand((Demand) m);
		else if (m instanceof Danger) addDanger((Danger) m);
		else if (m instanceof Target) addTarget((Target) m);
		else if (m instanceof ArrowAction) addArrowAction((ArrowAction) m);
		else if (m == null) {
			Log.w(TAG, "add(null) >> I'm sorry but I cannot add null thing :/");
			return;
		} else {
			Log.w(TAG, "add("+m.getClass().getSimpleName()+") >> Don't know what to do with that dude, sorry");
			return;
		}
		if (listener != null) listener.onAdd(m, e);
	}

	public void update(IModel m, IEntity e) {
		if (m instanceof Demand) updateDemand((Demand) m);
		else if (m instanceof Danger) updateDanger((Danger) m);
		else if (m instanceof Target) updateTarget((Target) m);
		else if (m instanceof ArrowAction) updateArrowAction((ArrowAction) m);
		else if (m == null) {
			Log.w(TAG, "update(null) >> I'm sorry but I cannot update null thing :/");
			return;
		} else {
			Log.w(TAG, "update("+m.getClass().getSimpleName()+") >> Don't know what to do with that dude, sorry");
			return;
		}
		if (listener != null) listener.onUpdate(m, e);
	}
	
	public void delete(IModel m, IEntity e) {
		if (m instanceof Demand) deleteDemand((Demand) m);
		else if (m instanceof Danger) deleteDanger((Danger) m);
		else if (m instanceof Target) deleteTarget((Target) m);
		else if (m instanceof ArrowAction) deleteArrowAction((ArrowAction) m);
		else if (m == null) {
			Log.w(TAG, "delete(null) >> I'm sorry but I cannot delete null thing :/");
			return;
		} else {
			Log.w(TAG, "delete("+m.getClass().getSimpleName()+") >> Don't know what to do with that dude, sorry");
			return;
		}
		if (listener != null) listener.onDelete(e);
	}
	
	private void addDemand(Demand d) {
		demands.add(d);
	}

	private void addDanger(Danger d) {
		dangers.add(d);
	}

	private void addTarget(Target t) {
		targets.add(t);
	}

	private void updateDemand(Demand d) {
    	// TODO
	}

	private void updateDanger(Danger d) {
        // TODO
    }

	private void updateTarget(Target t) {
        // TODO
    }
	
	private void deleteTarget(Target t) {
		targets.remove(t);
	}

	private void deleteDanger(Danger d) {
		dangers.remove(d);
	}

	private void deleteDemand(Demand d) {
		demands.remove(d);
	}
	
	private void addArrowAction(ArrowAction m) {
		arrowActions.add(m);
	}
	
	private void updateArrowAction(ArrowAction m) {
		// TODO
	}

	private void deleteArrowAction(ArrowAction m) {
		arrowActions.remove(m);
	}
}
