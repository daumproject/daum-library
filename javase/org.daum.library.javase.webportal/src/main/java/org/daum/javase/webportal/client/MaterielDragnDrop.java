package org.daum.javase.webportal.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class MaterielDragnDrop extends VLayout{

	public MaterielDragnDrop() {
		
		this.setShowEdges(true);  
		this.setWidth100();
		this.setHeight100();
		
		Label MaterielLabel = new Label("Materiel");
		MaterielLabel.setWidth("100%");
		MaterielLabel.setHeight("10%");
		MaterielLabel.setAlign(Alignment.CENTER);  
        MaterielLabel.setBorder("1px solid #808080");  
		
        VStack vStack = new VStack();
		vStack.setLayoutMargin(10);  
        vStack.setShowEdges(true);  
        vStack.setCanAcceptDrop(true);  
        vStack.setAnimateMembers(true);  
        vStack.setDropLineThickness(4);  
        
        this.addMember(MaterielLabel);
        this.addMember(vStack);
		
	}

}
