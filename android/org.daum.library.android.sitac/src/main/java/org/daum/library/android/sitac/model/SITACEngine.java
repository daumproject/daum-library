package org.daum.library.android.sitac.model;

import java.util.ArrayList;

import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.Target;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;

public class SITACEngine implements ISITACEngine {

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

    @Override
	public void addDemand(Demand d) {
		demands.add(d);
		if (listener != null) listener.onDemandAdded(d);
	}

    @Override
	public void addDanger(Danger d) {
		dangers.add(d);
		if (listener != null) listener.onDangerAdded(d);
	}

    @Override
	public void addTarget(Target t) {
		targets.add(t);
		if (listener != null) listener.onTargetAdded(t);
	}

    @Override
	public void updateDemand(Demand d) {
    	
		if (listener != null) listener.onDemandUpdated(d);
	}
}
