package org.daum.javase.webportal.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.grid.ListGrid;
import org.daum.javase.webportal.shared.Agent;
import org.daum.javase.webportal.shared.Intervention;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 03/08/12
 * Time: 10:06
 * To change this template use File | Settings | File Templates.
 */
public class InterventionData {
    private final AuthentificationServiceAsync loginService = GWT.create(AuthentificationService.class);
    private InterventionRecord[] interventionRecord;
    private ListGrid grille;

    public InterventionData(){

    }

  /*  public PompierRecord[] getNewRecords() {
        pompierRecords = new PompierRecord[1];
        pompierRecords[0] = new PompierRecord(new Agent("test","test","test","test"));
        return pompierRecords;
    }  */


    public InterventionRecord[] getInterventionRecords(ListGrid grilleIntervention) {
        this.grille = grilleIntervention;
        loginService.getAllIntervention(new AsyncCallback<List<Intervention>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(List<Intervention> interventions) {
                interventionRecord = new InterventionRecord[interventions.size()];
                for (int i = 0; i < interventions.size(); i++) {
                    interventionRecord[i] = new InterventionRecord(interventions.get(i));
                }
                grille.setData(interventionRecord);
            }
        });
        return interventionRecord;

    }
}
