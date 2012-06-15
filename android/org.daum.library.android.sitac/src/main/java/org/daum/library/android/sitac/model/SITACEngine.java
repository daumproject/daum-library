package org.daum.library.android.sitac.model;

import java.util.ArrayList;

import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.Target;
import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;

public class SITACEngine {

	private ArrayList<Demand> demands;
	private ArrayList<Danger> dangers;
	private ArrayList<Target> targets;
	private OnEngineStateChangeListener listener;
	
	public SITACEngine() {
		this.demands = new ArrayList<Demand>();
		this.dangers = new ArrayList<Danger>();
		this.targets = new ArrayList<Target>();
	}
	
	public void addDemand(Demand d) {
		demands.add(d);
		if (listener != null) listener.onDemandAdded(d);
	}
	
	public void setOnEngineStateChangeListener(OnEngineStateChangeListener listener) {
		this.listener = listener;
	}

	public void addDanger(Danger d) {
		dangers.add(d);
		if (listener != null) listener.onDangerAdded(d);
	}

	public void addTarget(Target t) {
		targets.add(t);
		if (listener != null) listener.onTargetAdded(t);
	}

	public void updateDemand(Demand d) {
		if (d.getId() != -1) {
			for (Demand demand : demands) {
				if (demand.getId() == d.getId()) {
					demand = d;
					break;
				}
			}
		}
		if (listener != null) listener.onDemandUpdated(d);
	}
}
