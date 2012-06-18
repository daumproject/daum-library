package org.daum.library.android.sitac.model;

import java.util.ArrayList;

import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.IModel;
import org.daum.common.model.api.Target;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;

/**
 * Here you should save/update/delete data from the Replica
 */
public class SITACEngine {

	private ArrayList<Demand> demands;
	private ArrayList<Danger> dangers;
	private ArrayList<Target> targets;
	private OnEngineStateChangeListener listener;
	
	public SITACEngine(OnEngineStateChangeListener engineHandler) {
		this.demands = new ArrayList<Demand>();
		this.dangers = new ArrayList<Danger>();
		this.targets = new ArrayList<Target>();
		listener = engineHandler;
	}
	
	public void add(IModel m) {
		if (m instanceof Demand) addDemand((Demand) m);
		else if (m instanceof Danger) addDanger((Danger) m);
		else if (m instanceof Target) addTarget((Target) m);
	}
	
	public void update(IModel m) {
		if (m instanceof Demand) updateDemand((Demand) m);
		else if (m instanceof Danger) updateDanger((Danger) m);
		else if (m instanceof Target) updateTarget((Target) m);
	}

	private void addDemand(Demand d) {
		demands.add(d);
		if (listener != null) listener.onAdd(d);
	}

	private void addDanger(Danger d) {
		dangers.add(d);
		if (listener != null) listener.onAdd(d);
	}

	private void addTarget(Target t) {
		targets.add(t);
		if (listener != null) listener.onAdd(t);
	}

	private void updateDemand(Demand d) {
    	// TODO
		if (listener != null) listener.onUpdate(d);
	}

	private void updateDanger(Danger d) {
        // TODO
        if (listener != null) listener.onUpdate(d);
    }

	private void updateTarget(Target t) {
        // TODO
        if (listener != null) listener.onUpdate(t);
    }
}
