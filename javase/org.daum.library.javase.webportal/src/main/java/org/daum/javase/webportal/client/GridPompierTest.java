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
  
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.daum.javase.webportal.shared.Agent;

public class GridPompierTest extends VLayout{

    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;
    private ListGrid pompierGrid;
    private PompierData pompierData= new PompierData();
    private final AuthentificationServiceAsync loginService = GWT
            .create(AuthentificationService.class);
    private Window windowEdit;


    public GridPompierTest() {
        pompierGrid = new ListGrid() {
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
                    editImg.setPrompt("Modify");
                    editImg.setHeight(16);
                    editImg.setWidth(16);
                    editImg.addClickHandler(new ClickHandler() {
                        public void onClick(ClickEvent event) {
                            loginService.getAgent(rollOverRecord.getAttribute("id"), new AsyncCallback<Agent>() {

                                @Override
                                public void onFailure(Throwable throwable) {
                                    throwable.printStackTrace();
                                }

                                @Override
                                public void onSuccess(Agent agent) {
                                    AgentForm agentForm = new AgentForm(agent);
                                    windowEdit = new Window();
                                    windowEdit.setWidth("30%");
                                    windowEdit.setHeight("45%");
                                    windowEdit.setIsModal(true);
                                    windowEdit.setShowModalMask(true);
                                    windowEdit.centerInPage();
                                    windowEdit.addItem(agentForm);




                                    windowEdit.addCloseClickHandler(new com.smartgwt.client.widgets.events.CloseClickHandler() {
                                        @Override
                                        public void onCloseClick(CloseClientEvent closeClientEvent) {
                                            refreshGrille();
                                            windowEdit.destroy();
                                        }
                                    });
                                    windowEdit.show();
                                }
                            });
                        }
                    });

                    ImgButton deleteImg = new ImgButton();
                    deleteImg.setShowDown(false);
                    deleteImg.setShowRollOver(false);
                    deleteImg.setLayoutAlign(Alignment.CENTER);
                    deleteImg.setSrc("delete.png");
                    deleteImg.setPrompt("Delete");
                    deleteImg.setHeight(16);
                    deleteImg.setWidth(16);
                    deleteImg.addClickHandler(new ClickHandler() {
                        public void onClick(ClickEvent event) {
                            SC.ask("Confirm deleteing "+rollOverRecord.getAttribute("nom") + " ?", new BooleanCallback() {

                                @Override
                                public void execute(Boolean aBoolean) {
                                    if(aBoolean){
                                         loginService.deleteAgent(rollOverRecord.getAttribute("id"), new AsyncCallback<Void>(){

                                             @Override
                                             public void onFailure(Throwable throwable) {
                                                 throwable.printStackTrace();
                                             }

                                             @Override
                                             public void onSuccess(Void aVoid) {
                                                 refreshGrille();
                                             }
                                         });
                                    }
                                }
                            });
                        }
                    });
                    rollOverCanvas.addMember(editImg);
                    rollOverCanvas.addMember(deleteImg);
                }
                return rollOverCanvas;

            }
        };


        pompierGrid.setShowRollOverCanvas(true);
        pompierGrid.setWidth100();
        pompierGrid.setHeight100();
        pompierGrid.setShowAllRecords(true);

        ListGridField nomField = new ListGridField("nom", "Nom");
        ListGridField prenomField = new ListGridField("prenom", "Prenom");
        ListGridField matriculeField = new ListGridField("matricule", "Matricule");
        pompierGrid.setFields(nomField, prenomField, matriculeField);
        pompierGrid.setCanResizeFields(true);
        refreshGrille();
        this.addChild(pompierGrid);
        this.setWidth100();
        this.setHeight100();
        this.draw();
    }

    public void refreshGrille(){
        pompierData.getPompierRecords(pompierGrid);
    }



}  