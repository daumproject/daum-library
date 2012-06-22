package org.daum.library.moyensmonitor.view;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.daum.library.moyensmonitor.model.MoyensTableModel;

import java.awt.*;

public class MoyensTableView extends JPanel {

	private static final long serialVersionUID = -342783817376586230L;

    private static final String TEXT_AGRES = "Agrès";
    private static final String TEXT_CIS = "CIS";
    private static final String TEXT_DEMAND = "Demande";
    private static final String TEXT_CRM = "Arrivé CRM";
    private static final String TEXT_DEPART = "Départ";
    private static final String TEXT_ENGAGE = "Engagé";
    private static final String TEXT_DESENG = "Désengagement";
	
	private MoyensTableModel adapter;
    private JTable table;
	
	public MoyensTableView(MoyensTableModel adapter) {
        table = new JTable(adapter);
		// table sort
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(adapter);
		sorter.setSortable(2, false);
		sorter.setSortsOnUpdates(true);
		table.setRowSorter(sorter);
//		getRowSorter().toggleSortOrder(0);

        // making header
        JPanel header = new JPanel(new GridLayout(1, 7));
        header.add(new JLabel(TEXT_AGRES, JLabel.CENTER));
        header.add(new JLabel(TEXT_CIS, JLabel.CENTER));
        header.add(new JLabel(TEXT_DEMAND, JLabel.CENTER));
        header.add(new JLabel(TEXT_DEPART, JLabel.CENTER));
        header.add(new JLabel(TEXT_CRM, JLabel.CENTER));
        header.add(new JLabel(TEXT_ENGAGE, JLabel.CENTER));
        header.add(new JLabel(TEXT_DESENG, JLabel.CENTER));

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);
	}

    public int getSelectedRow() {
        return table.getSelectedRow();
    }
}
