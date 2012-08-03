package org.daum.javase.webportal.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import org.daum.javase.webportal.shared.Agent;

import java.util.List;

public class AgentDragnDrop extends VLayout{

    private final AuthentificationServiceAsync loginService = GWT
            .create(AuthentificationService.class);

    private VStack vStack;
    private Firefighter fireFighter;
    //private HLayout layoutBouton;


	public AgentDragnDrop() {
		
		this.setShowEdges(true);  
		this.setWidth100();
		this.setHeight100();
		
		Label agentLabel = new Label("Agents");
		agentLabel.setWidth("100%");
		agentLabel.setHeight("10%");
		agentLabel.setAlign(Alignment.CENTER);  
        agentLabel.setBorder("1px solid #808080");        
		
        vStack = new VStack();
		vStack.setLayoutMargin(10);  
        vStack.setShowEdges(true);  
        vStack.setCanAcceptDrop(true);  
        vStack.setAnimateMembers(true);  
        vStack.setDropLineThickness(4);
        loginService.getAllAgent(new AsyncCallback<List<Agent>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(List<Agent> agents) {
                for(int i=0 ; i<agents.size(); i++){
                    fireFighter = new Firefighter(agents.get(i));
                    fireFighter.setID(agents.get(i).getId());
                    fireFighter.setShowTitle(true);
                    vStack.addMember(fireFighter);
                }
            }
        });

        IButton boutonAdministration = new IButton("Administration");
        boutonAdministration.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                RootPanel.get().clear();
                RootPanel.get().add(new AdministrationForm());
            }
        });

        IButton boutonLoggout = new IButton("logout");
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

        this.addMember(boutonAdministration);
        this.addMember(boutonLoggout);
        this.addMember(agentLabel);
        this.addMember(vStack);

	}

}
