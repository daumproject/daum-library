package org.daum.javase.webportal.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.grid.ListGrid;
import org.daum.javase.webportal.shared.AgentDTO;

import java.util.List;


public class PompierData {

    private final WebServiceAsync loginService = GWT.create(WebService.class);
    private PompierRecord[] pompierRecords;
    private ListGrid grille;

    public PompierData(){

    }

    public PompierRecord[] getNewRecords() {
        pompierRecords = new PompierRecord[1];
        pompierRecords[0] = new PompierRecord(new AgentDTO("test","test","test","test"));
        return pompierRecords;
    }

    public PompierRecord[] getPompierRecords(ListGrid grillePompier){
        this.grille = grillePompier;
        loginService.getAllAgent(new AsyncCallback<List<AgentDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(List<AgentDTO> agents) {
                pompierRecords = new PompierRecord[agents.size()];
                for(int i = 0; i < agents.size(); i++){
                    pompierRecords[i] = new PompierRecord(agents.get(i));
                }
                grille.setData(pompierRecords);
            }
        });
        return pompierRecords;
    }
}