package org.daum.library.moyensmonitor.controller;

import java.util.ArrayList;

import org.daum.common.model.api.Demand;
import org.daum.library.moyensmonitor.listener.OnEngineStateChangeListener;
import org.daum.library.moyensmonitor.model.MoyensTableModel;
import org.daum.library.moyensmonitor.view.MoyensMonitorFrame;

public class EngineHandler implements OnEngineStateChangeListener {
	
	private ArrayList<Demand> newDemands;
	private ArrayList<Demand> answeredDemands;
    private MoyensTableModel newDemandsModel;
    private MoyensTableModel answeredDemandsModel;
	
	public EngineHandler(MoyensMonitorFrame frame) {
		this.newDemands = frame.getNewDemandList();
        this.newDemandsModel = frame.getNewDemandsModel();

		this.answeredDemands = frame.getAnsweredDemandList();
        this.answeredDemandsModel = frame.getAnsweredDemandsModel();
	}

	@Override
	public void onAdd(Demand d) {
        if (!newDemands.contains(d) && !answeredDemands.contains(d) && d.getGh_depart() == null) {
            newDemands.add(d);
            newDemandsModel.fireTableDataChanged();
        } else {
            answeredDemands.add(d);
            answeredDemandsModel.fireTableDataChanged();
        }
	}

	@Override
	public void onUpdate(Demand d) {
		// update demand in UI
		if (newDemands.contains(d) && d.getGh_depart() != null) {
			newDemands.remove(d);
            newDemandsModel.fireTableDataChanged();
			answeredDemands.add(d);
            answeredDemandsModel.fireTableDataChanged();
		}
	}

	@Override
	public void onDelete(Demand d) {
		// delete demand in UI
        newDemands.remove(d);
        newDemandsModel.fireTableDataChanged();
        answeredDemands.remove(d);
        answeredDemandsModel.fireTableDataChanged();
	}

}
