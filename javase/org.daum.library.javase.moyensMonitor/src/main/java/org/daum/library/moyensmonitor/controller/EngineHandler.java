package org.daum.library.moyensmonitor.controller;

import java.util.ArrayList;

import org.daum.common.model.api.Demand;
import org.daum.library.moyensmonitor.listener.OnEngineStateChangeListener;
import org.daum.library.moyensmonitor.view.MoyensMonitorFrame;

public class EngineHandler implements OnEngineStateChangeListener {
	
	private ArrayList<Demand> newDemands;
	private ArrayList<Demand> answeredDemands;
	
	public EngineHandler(MoyensMonitorFrame frame) {
		this.newDemands = frame.getNewDemandList();
		this.answeredDemands = frame.getAnsweredDemandList();
	}

	@Override
	public void onAdd(Demand d) {
		newDemands.add(d);
	}

	@Override
	public void onUpdate(Demand d) {
		// update demand in UI
		if (newDemands.contains(d) && d.getGh_depart() != null) {
			newDemands.remove(d);
			answeredDemands.add(d);
		}
	}

	@Override
	public void onDelete(Demand d) {
		// delete demand in UI
	}

}
