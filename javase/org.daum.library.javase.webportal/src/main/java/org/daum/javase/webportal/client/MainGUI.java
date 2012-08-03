package org.daum.javase.webportal.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class MainGUI extends HLayout{

    private final AuthentificationServiceAsync loginService = GWT.create(AuthentificationService.class);

	public MainGUI(){
		this.setWidth100();
		this.setHeight("95%");
		AgentDragnDrop agentWindow = new AgentDragnDrop();
		InterventionForm interventionWindow = new InterventionForm();
		MaterielDragnDrop materielWindow = new MaterielDragnDrop();
		agentWindow.setWidth("20%");
		interventionWindow.setWidth("60");
		materielWindow.setWidth("20%");
        interventionWindow.reset();
		this.addMember(agentWindow);
		this.addMember(interventionWindow);
		this.addMember(materielWindow);
	}
}
