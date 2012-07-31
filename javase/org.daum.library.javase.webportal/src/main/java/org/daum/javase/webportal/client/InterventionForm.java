package org.daum.javase.webportal.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.DragMoveEvent;
import com.smartgwt.client.widgets.events.DragMoveHandler;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;


public class InterventionForm extends VLayout{

	private final AuthentificationServiceAsync loginService = GWT
			.create(AuthentificationService.class);

	public InterventionForm(){

		DynamicForm formInterventionBasics = new DynamicForm();  
		formInterventionBasics.setID("basicsInterventionForm");
		
		TextItem itemAdresse = new TextItem();  
		itemAdresse.setName("adresse");  
		itemAdresse.setTitle("Adresse");
		
		TextItem itemCp = new TextItem();  
		itemCp.setName("cp");  
		itemCp.setTitle("CP");
		
		TextItem itemVille = new TextItem();  
		itemVille.setName("ville");  
		itemVille.setTitle("Ville");
			
		TextItem itemRequerant = new TextItem();  
		itemRequerant.setName("nomRequerant");  
		itemRequerant.setTitle("Requerant");
		
		TextItem itemCodeSinitre = new TextItem();  
		itemCodeSinitre.setName("codeSinistre");  
		itemCodeSinitre.setTitle("Code sinistre");  

		TextItem itemVictime = new TextItem();  
		itemVictime.setName("nomVictime");  
		itemVictime.setTitle("Victimes");  
		
		VStack agentVStack = new VStack();
		agentVStack.setLayoutMargin(10);  
        agentVStack.setShowEdges(true);  
        agentVStack.setCanAcceptDrop(true);  
        agentVStack.setAnimateMembers(true);  
        agentVStack.setDropLineThickness(4);
        agentVStack.addDropHandler(new DropHandler() {
        	
			@Override
			public void onDrop(DropEvent event) {

			}
        });
        
        
		
        formInterventionBasics.setFields(new FormItem[]{itemAdresse,itemCp, itemVille, itemRequerant, itemCodeSinitre, itemVictime});

		IButton submit = new IButton();  
		submit.setTitle("Submit");  
		submit.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				
			}
		});
		
		this.setWidth100();
		this.setHeight100();
		this.setMembersMargin(10);
		this.addMember(formInterventionBasics);
		this.addMember(agentVStack);
		this.addMember(submit);  
		this.draw();  
	}


}
