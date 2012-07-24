package org.daum.javase.webportal.client;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class AdministrationForm extends VLayout{

	public AdministrationForm(){  
        
        TabSet theTabs = new TabSet();  
        theTabs.setWidth("50%");  
        theTabs.setHeight("250px");  
          
        Tab materielTab = new Tab();  
        materielTab.setTitle("Gestion du materiel");   
        materielTab.setPane(new MaterielForm());  
  
        Tab agentTab = new Tab();  
        agentTab.setTitle("Gestion des Agents");  
        agentTab.setPane(new AgentForm());
        
        Tab exempleTab1 = new Tab();  
        exempleTab1.setTitle("Onglet d'exemple 1");  
        
        Tab exempleTab2 = new Tab();  
        exempleTab2.setTitle("Onglet d'exemple 2");         
          
        theTabs.setTabs(materielTab, agentTab, exempleTab1, exempleTab2);  
          
        //IButton submit = new IButton("Submit");
        
        this.setWidth100();
        this.setHeight100();
        this.setMembersMargin(10);  
        this.addMember(theTabs);
        //this.addMember(submit);
 
        this.draw();  
	}
}
