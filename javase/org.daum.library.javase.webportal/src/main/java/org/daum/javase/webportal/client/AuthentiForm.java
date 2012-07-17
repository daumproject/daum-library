package org.daum.javase.webportal.client;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class AuthentiForm extends HLayout{
	
	public AuthentiForm(){
		this.setMargin(20); 
	    
	    final DynamicForm form = new DynamicForm();  
	    form.setWidth(250);  
	      
	    TextItem matriculeItem = new TextItem();  
	    matriculeItem.setTitle("Matricule");  
	    matriculeItem.setRequired(true);   

	    PasswordItem passwordItem = new PasswordItem();  
	    passwordItem.setTitle("Password");  
	    passwordItem.setRequired(true);   

	    
	    form.setFields(new FormItem[] {matriculeItem, passwordItem});  
	    
	    Button valider = new Button("valider");
	    valider.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				Window.alert("Cay bon");
			}
		});
	      
	    this.addMember(form);  
	    this.addMember(valider);  

	}
	
	public void init(){
		this.draw();  
	}
	
}
