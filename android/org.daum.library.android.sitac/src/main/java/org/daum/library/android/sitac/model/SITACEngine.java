package org.daum.library.android.sitac.model;

import java.util.Hashtable;

import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.Target;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;

public class SITACEngine implements ISITACEngine {

	private Hashtable<Long, Demand> demands;
	private Hashtable<Long, Danger> dangers;
	private Hashtable<Long, Target> targets;
	private OnEngineStateChangeListener listener;
	
	public SITACEngine(OnEngineStateChangeListener engineHandler) {
		this.demands = new Hashtable<Long, Demand>();
		this.dangers = new Hashtable<Long, Danger>();
		this.targets = new Hashtable<Long, Target>();
		listener = engineHandler;
	}

    @Override
	public void addDemand(Demand d) {
		demands.put(42L, d);
		if (listener != null) listener.onDemandAdded(d);
	}

    @Override
	public void addDanger(Danger d) {
		dangers.put(42L, d);
		if (listener != null) listener.onDangerAdded(d);
	}

    @Override
	public void addTarget(Target t) {
		targets.put(42L, t);
		if (listener != null) listener.onTargetAdded(t);
	}

    @Override
	public void updateDemand(Demand d) {
    	demands.put(42L, d);
    	System.out.println("WESH HEU BATARD");
		if (listener != null) listener.onDemandUpdated(d);
	}
}
