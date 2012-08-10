package org.daum.javase.webportal.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.layout.HLayout;

public class MainGUI extends HLayout{

    private final WebServiceAsync loginService = GWT.create(WebService.class);

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
