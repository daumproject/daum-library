package org.daum.javase.webportal.client;

import com.smartgwt.client.widgets.layout.HLayout;

public class MainGUI extends HLayout{
	
	public MainGUI(){
		this.setWidth100();
		this.setHeight100();
		AgentDragnDrop agentWindow = new AgentDragnDrop();
		InterventionForm interventionWindow = new InterventionForm();
		MaterielDragnDrop materielWindow = new MaterielDragnDrop();
		agentWindow.setWidth("20%");
		interventionWindow.setWidth("60");
		materielWindow.setWidth("20%");		
		this.addMember(agentWindow);
		this.addMember(interventionWindow);
		this.addMember(materielWindow);
	}
}
