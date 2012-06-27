package org.daum.library.moyensmonitor.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.daum.common.model.api.Demand;
import org.daum.library.moyensmonitor.model.MoyensTableModel;

import java.awt.*;
import java.util.ArrayList;

public class TabContentView extends JPanel {

	private static final long serialVersionUID = -342783817376586230L;

    private static final String TEXT_AGRES = "Agrès";
    private static final String TEXT_CIS = "CIS";
    private static final String TEXT_DEMAND = "Demande";
    private static final String TEXT_CRM = "Arrivé CRM";
    private static final String TEXT_DEPART = "Départ";
    private static final String TEXT_ENGAGE = "Engagé";
    private static final String TEXT_DESENG = "Désengagement";
	
	private final MoyensTableModel model;
    private JTable table;
    private JTextField	tf_agres,
            tf_cis,
            tf_demande,
            tf_depart,
            tf_crm,
            tf_engage,
            tf_deseng;
    private JPanel btnPanel;
    private Demand selectedDemand;
	
	public TabContentView(final MoyensTableModel model) {
        this.model = model;
        table = new JTable(model);
		// table sort
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		sorter.setSortable(2, false);
		sorter.setSortsOnUpdates(true);
		table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        // text fields
        tf_agres = new JTextField();
        tf_cis = new JTextField();
        tf_demande = new JTextField();
        tf_depart = new JTextField();
        tf_crm = new JTextField();
        tf_engage = new JTextField();
        tf_deseng = new JTextField();

        Border current = tf_agres.getBorder();
        Border empty = new EmptyBorder(5, 5, 5, 5);
        if (current == null) {
            tf_agres.setBorder(empty);
        } else {
            Border padding = new CompoundBorder(empty, current);
            tf_agres.setBorder(padding);
            tf_cis.setBorder(padding);
            tf_demande.setBorder(padding);
            tf_depart.setBorder(padding);
            tf_crm.setBorder(padding);
            tf_engage.setBorder(padding);
            tf_deseng.setBorder(padding);
        }

        // demand in JTextField panel
        JPanel txtFieldPanel = new JPanel(new GridLayout(1, 7));
        txtFieldPanel.add(tf_agres);
        txtFieldPanel.add(tf_cis);
        txtFieldPanel.add(tf_demande);
        txtFieldPanel.add(tf_depart);
        txtFieldPanel.add(tf_crm);
        txtFieldPanel.add(tf_engage);
        txtFieldPanel.add(tf_deseng);

        // btn panel
        btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel southPanel = new JPanel(new GridLayout(2, 1));
        southPanel.add(txtFieldPanel);
        southPanel.add(btnPanel);

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        final ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!selectionModel.isSelectionEmpty()) {
                    int rowId = selectionModel.getMinSelectionIndex();
                    selectedDemand = model.getData().get(rowId);
                    String[] values = new String[7];
                    for (int i = 0; i < 7; i++) {
                        values[i] = model.getValueAt(rowId, i);
                    }
                    tf_agres.setText(values[0]);
                    tf_cis.setText(values[1]);
                    tf_demande.setText(values[2]);
                    tf_depart.setText(values[3]);
                    tf_crm.setText(values[4]);
                    tf_engage.setText(values[5]);
                    tf_deseng.setText(values[6]);
                }
            }
        });


	}

    public void addButton(JButton btn) {
        btnPanel.add(btn);
    }

    public void resetSelection() {
        tf_agres.setText("");
        tf_cis.setText("");
        tf_demande.setText("");
        tf_depart.setText("");
        tf_crm.setText("");
        tf_engage.setText("");
        tf_deseng.setText("");

        selectedDemand = null;
    }

    public Demand getSelectedDemand() {
        return selectedDemand;
    }

    public String[] getFieldValues() {
        String[] ret = new String[7];
        if (table.getSelectedRow() == -1) return ret;
        ret[0] = tf_agres.getText().trim();
        ret[1] = tf_cis.getText().trim();
        ret[2] = tf_demande.getText().trim();
        ret[3] = tf_depart.getText().trim();
        ret[4] = tf_crm.getText().trim();
        ret[5] = tf_engage.getText().trim();
        ret[6] = tf_deseng.getText().trim();
        return ret;
    }
}
