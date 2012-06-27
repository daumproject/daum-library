package org.daum.library.moyensmonitor.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.daum.common.model.api.Demand;
import org.daum.library.moyensmonitor.controller.MoyensMonitorController;
import org.daum.library.moyensmonitor.listener.OnMoyensMonitorEventListener;
import org.daum.library.moyensmonitor.model.MoyensTableModel;
import org.daum.library.ormH.store.ReplicaStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoyensMonitorFrame extends JFrame {

	private static final long serialVersionUID = 8044205527746379985L;
	private static final String FRAME_TITLE = "Moyens Monitor";
	private static final String NEW_DEMAND_TAB = "Nouvelles demandes";
	private static final String ANSWERED_DEMAND_TAB = "Demandes traitées";
	private static final String VALIDATE_BTN = "Valider la demande";
    private static final String ADD_DEMAND_BTN = "Ajouter une fakeDemand";
    private static final String EDIT_BTN = "Enregistrer modifications";
    private static final String DELETE_BTN = "Supprimer";
	
	private MoyensMonitorController controller;
	private MoyensTableModel newDemandsModel, answeredDemandsModel;
	private TabContentView newDemandsView, answeredDemandsView;
	private JButton validateBtn, addDemandBtn, editBtn, deleteBtn;
	private ArrayList<Demand> newDemands, answeredDemands;
	private OnMoyensMonitorEventListener listener;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
	
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

        // tab content
        newDemandsView = new TabContentView(newDemandsModel);
        answeredDemandsView = new TabContentView(answeredDemandsModel);

        // button
        validateBtn = new JButton(VALIDATE_BTN);
        addDemandBtn = new JButton(ADD_DEMAND_BTN);
        editBtn = new JButton(EDIT_BTN);
        deleteBtn = new JButton(DELETE_BTN);
    }
	
	private void configUI() {
		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);

		// new demands panel
		JPanel newDemandPanel = new JPanel(new BorderLayout());
		newDemandPanel.add(newDemandsView, BorderLayout.CENTER);

        newDemandsView.addButton(validateBtn);
        newDemandsView.addButton(addDemandBtn);
		
		// answered demands panel
		JPanel answeredDemandPanel = new JPanel(new BorderLayout());
		answeredDemandPanel.add(answeredDemandsView, BorderLayout.CENTER);

        answeredDemandsView.addButton(editBtn);
        answeredDemandsView.addButton(deleteBtn);
		
		tabPane.addTab(NEW_DEMAND_TAB, newDemandPanel);
		tabPane.addTab(ANSWERED_DEMAND_TAB, answeredDemandPanel);
		add(tabPane);
	}
	
	private void defineCallbacks() {
		validateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listener != null && newDemandsView.getSelectedDemand() != null) {
                    Demand selectedDemand = newDemandsView.getSelectedDemand();
					listener.onValidateButtonClicked(selectedDemand, newDemandsView.getFieldValues()[1]);
                    newDemandsView.resetSelection();
				}
			}
		});

        addDemandBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onAddFakeDemand();
                    newDemandsView.resetSelection();
                }
            }
        });

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null && answeredDemandsView.getSelectedDemand() != null) {
                    Demand selectedDemand = answeredDemandsView.getSelectedDemand();
                    listener.onEditDemand(selectedDemand, answeredDemandsView.getFieldValues());
                    answeredDemandsView.resetSelection();
                }
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null && answeredDemandsView.getSelectedDemand() != null) {
                    listener.onDeleteBtnClicked(answeredDemandsView.getSelectedDemand());
                    answeredDemandsView.resetSelection();
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
