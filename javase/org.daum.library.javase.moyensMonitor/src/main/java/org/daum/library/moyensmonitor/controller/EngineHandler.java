package org.daum.library.moyensmonitor.controller;

import java.util.ArrayList;
import java.util.Collection;

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
	public void doUpdate(Collection<Demand> demands) {
        newDemands.clear();
        answeredDemands.clear();

        for (Demand d : demands) {
            if (d.getGh_depart() == null) {
                newDemands.add(d);
            } else {
                answeredDemands.add(d);
            }
        }

        newDemandsModel.fireTableDataChanged();
        answeredDemandsModel.fireTableDataChanged();
	}

	@Override
	public void onDelete(Demand d) {
        System.out.println("onDelete("+d+")");
		// delete demand in UI
        newDemands.remove(d);
        newDemandsModel.fireTableDataChanged();
        answeredDemands.remove(d);
        answeredDemandsModel.fireTableDataChanged();
	}

}
