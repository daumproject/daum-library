package org.daum.javase.webportal.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Img;
import org.daum.javase.webportal.shared.Agent;

public class Firefighter extends Img {

    private Agent agent;
	
	 public Firefighter() {  
         setWidth(48);  
         setHeight(48);  
         setLayoutAlign(Alignment.CENTER);  
         setCanDragReposition(true);  
         setCanDrop(true);  
         setDragAppearance(DragAppearance.TARGET);  
     }
	 
	 public Firefighter(Agent agent) {
         this();
         setAgent(agent);
         setSrc("bandeBlanche.jpg");
         setTitle(this.agent.getMatricule());
     }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
