package org.daum.javase.webportal.client;

/* 
 * Smart GWT (GWT for SmartClient) 
 * Copyright 2008 and beyond, Isomorphic Software, Inc. 
 * 
 * Smart GWT is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License version 3 
 * as published by the Free Software Foundation.  Smart GWT is also 
 * available under typical commercial license terms - see 
 * http://smartclient.com/license 
 * 
 * This software is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * Lesser General Public License for more details. 
 */  
  
/* 
 * Smart GWT (GWT for SmartClient) 
 * Copyright 2008 and beyond, Isomorphic Software, Inc. 
 * 
 * Smart GWT is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License version 3 
 * is published by the Free Software Foundation.  Smart GWT is also 
 * available under typical commercial license terms - see 
 * http://smartclient.com/license 
 * 
 * This software is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * Lesser General Public License for more details. 
 */  
  
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
  
public class GridPompierTest extends VLayout{  
  
    private HLayout rollOverCanvas;  
    private ListGridRecord rollOverRecord;  
  
    public GridPompierTest() {  
  
        final ListGrid countryGrid = new ListGrid() {  
            @Override  
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {  
                rollOverRecord = this.getRecord(rowNum);  
  
                if(rollOverCanvas == null) {  
                    rollOverCanvas = new HLayout(3);  
                    rollOverCanvas.setSnapTo("TR");  
                    rollOverCanvas.setWidth(50);  
                    rollOverCanvas.setHeight(22);  
  
                    ImgButton editImg = new ImgButton();  
                    editImg.setShowDown(false);  
                    editImg.setShowRollOver(false);  
                    editImg.setLayoutAlign(Alignment.CENTER);  
                    editImg.setSrc("comment_edit.png");  
                    editImg.setPrompt("Modifier");  
                    editImg.setHeight(16);  
                    editImg.setWidth(16);  
                    editImg.addClickHandler(new ClickHandler() {  
                        public void onClick(ClickEvent event) {  
                            SC.say("Edit Comment Icon Clicked for country : " + rollOverRecord.getAttribute("nom"));  
                        }  
                    });  

                    rollOverCanvas.addMember(editImg);  
                }  
                return rollOverCanvas;  
  
            }  
        };  
        countryGrid.setShowRollOverCanvas(true);  
  
  
        countryGrid.setWidth(500);  
        countryGrid.setHeight(224);  
        countryGrid.setShowAllRecords(true);  
  
        ListGridField nomField = new ListGridField("nom", "Nom");  
        ListGridField prenomField = new ListGridField("prenom", "Prenom");  
        countryGrid.setFields(nomField, prenomField);  
        countryGrid.setCanResizeFields(true);  
        countryGrid.setData(PompierData.getRecords());  
  
        countryGrid.draw();  
    }  
  
}  