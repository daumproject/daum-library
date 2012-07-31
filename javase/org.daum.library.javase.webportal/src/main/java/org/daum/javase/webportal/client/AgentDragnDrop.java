package org.daum.javase.webportal.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import org.daum.javase.webportal.shared.Agent;

import java.util.List;

public class AgentDragnDrop extends VLayout{

    private final AuthentificationServiceAsync loginService = GWT
            .create(AuthentificationService.class);
    private VStack vStack;


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
                    Firefighter fireFighter = new Firefighter(agents.get(i).getMatricule());
                    fireFighter.setShowTitle(true);
                    vStack.addMember(fireFighter);
                }
            }
        });

        
        this.addMember(agentLabel);
        this.addMember(vStack);
        
        
	}

}
