package org.daum.javase.webportal.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class AdministrationForm extends VLayout{

    private GridPompierTest gridPompier;
    private GridIntervention gridIntervention;
    private AgentForm formulaireAgent;
    private IButton boutonAdministration,boutonLoggout;
    private final WebServiceAsync loginService = GWT.create(WebService.class);


	public AdministrationForm(){

        boutonAdministration = new IButton("Intervention");
        boutonAdministration.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                RootPanel.get().clear();
                RootPanel.get().add(new MainGUI());
            }
        });

        boutonLoggout = new IButton("logout");
        boutonLoggout.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                loginService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        RootPanel.get().clear();
                        RootPanel.get().add(new AuthentiForm());
                    }
                });
            }
        });
        
        TabSet theTabs = new TabSet();  
        theTabs.setWidth100();
        theTabs.setHeight100();
       /*
        Tab materielTab = new Tab();  
        materielTab.setTitle("Gestion du materiel");   
        materielTab.setPane(new MaterielForm());  */
  
        final Tab agentTab = new Tab();
        agentTab.setTitle("Liste des Agents");
        gridPompier = new GridPompierTest();
        agentTab.setPane(gridPompier);
        agentTab.addTabSelectedHandler(new TabSelectedHandler() {
            @Override
            public void onTabSelected(TabSelectedEvent tabSelectedEvent) {
                gridPompier.refreshGrille();
            }
        });
        
        Tab ajoutAgentTab = new Tab();
        ajoutAgentTab.setTitle("Ajout d'un agent");
        formulaireAgent = new AgentForm();
        ajoutAgentTab.setPane(formulaireAgent);
        ajoutAgentTab.addTabSelectedHandler(new TabSelectedHandler() {
            @Override
            public void onTabSelected(TabSelectedEvent tabSelectedEvent) {
                formulaireAgent.reset();
            }
        });
        
        Tab interventionTab = new Tab();
        interventionTab.setTitle("Liste des Interventions");
        gridIntervention = new GridIntervention();
        interventionTab.setPane(gridIntervention);
        interventionTab.addTabSelectedHandler(new TabSelectedHandler() {
            @Override
            public void onTabSelected(TabSelectedEvent tabSelectedEvent) {
                gridIntervention.refreshGrille();
            }
        });
          
       // theTabs.setTabs(materielTab, agentTab, ajoutAgentTab,interventionTab);

        theTabs.setTabs(agentTab, ajoutAgentTab,interventionTab);

        this.setWidth100();
        this.setHeight100();
        this.setMembersMargin(10);
        this.addMember(boutonAdministration);
        this.addMember(boutonLoggout);
        this.addMember(theTabs);
        this.draw();  
	}
}
