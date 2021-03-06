package org.daum.javase.webportal.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 03/08/12
 * Time: 09:32
 * To change this template use File | Settings | File Templates.
 */
public class GridIntervention extends VLayout {

        private HLayout rollOverCanvas;
        private ListGrid grilleIntervention;
        private ListGridRecord rollOverRecord;
        private InterventionData interventionData = new InterventionData();
        private final WebServiceAsync loginService = GWT.create(WebService.class);

    public GridIntervention() {
        grilleIntervention = new ListGrid() {
            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if(rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);
                }
                return rollOverCanvas;
            }
        };

        grilleIntervention.setShowRollOverCanvas(true);
        grilleIntervention.setWidth100();
        grilleIntervention.setHeight100();
        grilleIntervention.setShowAllRecords(true);

        ListGridField idField = new ListGridField("code", "Code Sinistre");
        ListGridField codeSinistreField = new ListGridField("description", "Description");
        ListGridField adr = new ListGridField("adresse", "Adresse");

        grilleIntervention.setFields(idField,adr,codeSinistreField);
        grilleIntervention.setCanResizeFields(true);
        refreshGrille();
        this.addChild(grilleIntervention);
        this.setWidth100();
        this.setHeight100();
        this.draw();
    }


    public void refreshGrille(){
            interventionData.getInterventionRecords(grilleIntervention);
        }
}

