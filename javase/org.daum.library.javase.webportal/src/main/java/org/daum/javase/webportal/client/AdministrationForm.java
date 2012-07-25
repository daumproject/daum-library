package org.daum.javase.webportal.client;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class AdministrationForm extends VLayout{

	public AdministrationForm(){  
        
        TabSet theTabs = new TabSet();  
        theTabs.setWidth("50%");  
        theTabs.setHeight("250px");  
          
        Tab materielTab = new Tab();  
        materielTab.setTitle("Gestion du materiel");   
        materielTab.setPane(new MaterielForm());  
  
        final Tab agentTab = new Tab();
        agentTab.setTitle("Liste des Agents");
        agentTab.setPane(new GridPompierTest());
        
        Tab ajoutAgentTab = new Tab();
        ajoutAgentTab.setTitle("Ajout d'un agent");
        ajoutAgentTab.setPane(new AgentForm());
        
        Tab exempleTab2 = new Tab();  
        exempleTab2.setTitle("Onglet d'exemple 2");         
          
        theTabs.setTabs(materielTab, agentTab, ajoutAgentTab, exempleTab2);
          
        //IButton submit = new IButton("Submit");
        
        this.setWidth100();
        this.setHeight100();
        this.setMembersMargin(10);  
        this.addMember(theTabs);
        this.draw();  
	}
}
