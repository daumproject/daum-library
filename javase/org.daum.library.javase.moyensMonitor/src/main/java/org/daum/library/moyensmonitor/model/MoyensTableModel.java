package org.daum.library.moyensmonitor.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.daum.common.model.api.Demand;
import org.daum.common.util.api.TimeFormatter;

public class MoyensTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -1466727223025494133L;
	
	private ArrayList<Demand> data;
	private String[] header = {"Agrès", "CIS", "Demande", "Départ", "Arrivée CRM", "Engagé", "Désengagement"};
	
	public MoyensTableModel(ArrayList<Demand> data) {
		this.data =  data;
	}

	@Override
	public int getColumnCount() {
		return header.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public String getValueAt(int rowId, int colId) {
		Demand d = data.get(rowId);
		if (d == null) return "-no data-";
		switch (colId) {
			case 0:
				return d.getType()+((d.getNumber() == null) ? "": d.getNumber());
			case 1:
				return d.getCis();
			case 2:
				return TimeFormatter.getGroupeHoraire(d.getGh_demande());
			case 3:
				return TimeFormatter.getGroupeHoraire(d.getGh_depart());
			case 4:
				return TimeFormatter.getGroupeHoraire(d.getGh_crm());
			case 5:
				return TimeFormatter.getGroupeHoraire(d.getGh_engage());
			case 6:
				return TimeFormatter.getGroupeHoraire(d.getGh_desengagement());
			default:
				return "-no data-";
		}
	}
}
