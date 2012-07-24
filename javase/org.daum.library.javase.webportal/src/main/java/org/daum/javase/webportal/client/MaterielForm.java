package org.daum.javase.webportal.client;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class MaterielForm extends VLayout {
	
	public MaterielForm(){
		
		final DynamicForm form = new DynamicForm();
		form.setWidth100();
		form.setHeight100();
		
		HeaderItem header = new HeaderItem();
		header.setDefaultValue("Ajout d'un materiel");

		TextItem nomItem = new TextItem();  
		nomItem.setTitle("Nom");  
		nomItem.setRequired(true);   

		TextItem codeItem = new TextItem();  
		codeItem.setTitle("Code");  
		codeItem.setRequired(true);
		
		TextItem matriculeItem = new TextItem();  
		matriculeItem.setTitle("Matricule");  
		matriculeItem.setRequired(true);
		
		PasswordItem passwordItem = new PasswordItem();  
		passwordItem.setTitle("Password");  
		passwordItem.setRequired(true);

		ButtonItem btnValider = new ButtonItem("Valider");
		btnValider.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//TODO ajouter agent
			} 
		});


		form.setFields(new FormItem[] {header, nomItem, codeItem, matriculeItem, passwordItem, btnValider});
		this.addChild(form);
		this.setWidth100();
		this.setHeight100();
		this.draw();		
	}
}
