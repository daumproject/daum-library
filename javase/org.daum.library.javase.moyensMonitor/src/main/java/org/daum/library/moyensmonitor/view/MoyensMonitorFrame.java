package org.daum.library.moyensmonitor.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.daum.common.model.api.Demand;
import org.daum.common.model.api.VehicleType;
import org.daum.library.moyensmonitor.controller.MoyensMonitorController;
import org.daum.library.moyensmonitor.listener.OnMoyensMonitorEventListener;
import org.daum.library.moyensmonitor.model.MoyensTableModel;
import org.daum.library.ormH.store.ReplicaStore;

public class MoyensMonitorFrame extends JFrame {

	private static final long serialVersionUID = 8044205527746379985L;
	private static final String FRAME_TITLE = "Moyens Monitor";
	private static final String NEW_DEMAND_TAB = "Nouvelles demandes";
	private static final String ANSWERED_DEMAND_TAB = "Demandes traitées";
	private static final String VALIDATE_BTN = "Valider la demande";
    private static final String ADD_DEMAND_BTN = "Ajouter une fakeDemand";
	
	private MoyensMonitorController controller;
	private MoyensTableModel newDemandsModel, answeredDemandsModel;
	private MoyensTableView newDemandsView, answeredDemandsView;
	private JButton validateBtn, addDemandBtn;
	private JTextField	tf_agres,
						tf_cis,
						tf_demande,
						tf_depart,
						tf_crm,
						tf_engage,
						tf_deseng;
	private ArrayList<Demand> newDemands, answeredDemands;
	private Demand selectedDemand;
	private OnMoyensMonitorEventListener listener;
	
	public MoyensMonitorFrame(String nodeName, ReplicaStore storeImpl) {
		super(FRAME_TITLE);

		setSize(800, 600);
		setLocationRelativeTo(null); // center frame

        initUI();
		configUI();
		defineCallbacks();
		
		this.controller = new MoyensMonitorController(this, nodeName, storeImpl);
	}

    private void initUI() {
        // data
        newDemands = new ArrayList<Demand>();
        answeredDemands = new ArrayList<Demand>();

        // tableView adapters
        newDemandsModel = new MoyensTableModel(newDemands);
        answeredDemandsModel = new MoyensTableModel(answeredDemands);

        // tableViews
        newDemandsView = new MoyensTableView(newDemandsModel);
        answeredDemandsView = new MoyensTableView(answeredDemandsModel);

        // text fields
        tf_agres = new JTextField();
        tf_cis = new JTextField();
        tf_demande = new JTextField();
        tf_depart = new JTextField();
        tf_crm = new JTextField();
        tf_engage = new JTextField();
        tf_deseng = new JTextField();

        // button
        validateBtn = new JButton(VALIDATE_BTN);
        addDemandBtn = new JButton(ADD_DEMAND_BTN);
    }
	
	private void configUI() {
		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);

		// new demands panel
		JPanel newDemandPanel = new JPanel(new BorderLayout());
		newDemandPanel.add(newDemandsView, BorderLayout.CENTER);
		
		// demand in JTextField panel
		JPanel txtFieldPanel = new JPanel(new GridLayout(1, 7));
		
		tf_agres.setEditable(false);
		tf_cis.setEditable(false);
		tf_demande.setEditable(false);
		tf_depart.setEditable(false);
		tf_crm.setEditable(false);
		tf_engage.setEditable(false);
		tf_deseng.setEditable(false);
		
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
		
		txtFieldPanel.add(tf_agres);
		txtFieldPanel.add(tf_cis);
		txtFieldPanel.add(tf_demande);
		txtFieldPanel.add(tf_depart);
		txtFieldPanel.add(tf_crm);
		txtFieldPanel.add(tf_engage);
		txtFieldPanel.add(tf_deseng);
		
		// btn panel
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnPanel.add(validateBtn);
        btnPanel.add(addDemandBtn);

		
		JPanel southPanel = new JPanel(new GridLayout(2, 1));
		southPanel.add(txtFieldPanel);
		southPanel.add(btnPanel);
		newDemandPanel.add(southPanel, BorderLayout.SOUTH);
		
		// answered demands panel
		JPanel answeredDemandPanel = new JPanel(new BorderLayout());
		answeredDemandPanel.add(answeredDemandsView, BorderLayout.CENTER);
		
		tabPane.addTab(NEW_DEMAND_TAB, newDemandPanel);
		tabPane.addTab(ANSWERED_DEMAND_TAB, answeredDemandPanel);
		add(tabPane);
	}
	
	private void defineCallbacks() {
		newDemandsView.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
						int rowId = newDemandsView.getSelectedRow();
						if (rowId != -1) {
							selectedDemand = newDemands.get(rowId);
							String[] values = new String[8];
							for (int i=0; i<8; i++) {
								values[i] = newDemandsModel.getValueAt(rowId, i);
							}
							tf_agres.setText(values[0]);
							tf_cis.setText(values[1]);
							tf_demande.setText(values[2]);
							tf_depart.setText(values[3]);
							tf_crm.setText(values[4]);
							tf_engage.setText(values[5]);
							tf_deseng.setText(values[6]);
							
							tf_cis.setEditable(true);
						}
				}
			}
		});
		
		validateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listener != null && selectedDemand != null) {
					listener.onValidateButtonClicked(selectedDemand, tf_cis.getText().trim());
					newDemandsModel.fireTableDataChanged();
					tf_agres.setText("");
					tf_cis.setText("");
					tf_demande.setText("");
					tf_depart.setText("");
					tf_crm.setText("");
					tf_engage.setText("");
					tf_deseng.setText("");
					
					tf_cis.setEditable(false);
					selectedDemand = null;
				}
			}
		});

        addDemandBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onAddFakeDemand();
                }
            }
        });
	}
	
	public ArrayList<Demand> getNewDemandList() {
		return newDemands;
	}
	
	public ArrayList<Demand> getAnsweredDemandList() {
		return answeredDemands;
	}

    public MoyensTableModel getNewDemandsModel() {
        return newDemandsModel;
    }

    public MoyensTableModel getAnsweredDemandsModel() {
        return answeredDemandsModel;
    }

    public void setOnMoyensMonitorEventListener(OnMoyensMonitorEventListener listener) {
		this.listener = listener;
	}
}
