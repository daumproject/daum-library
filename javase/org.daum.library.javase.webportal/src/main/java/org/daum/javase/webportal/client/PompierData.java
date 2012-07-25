package org.daum.javase.webportal.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.daum.javase.webportal.shared.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PompierData {

    private final AuthentificationServiceAsync loginService = GWT.create(AuthentificationService.class);
    private PompierRecord[] records;
    private PompierRecord[] pompiers = new PompierRecord[]{};

    public PompierData(){

    }

    public PompierRecord[] getNewPompiersRecord(){
        loginService.getAllAgent(new AsyncCallback<List<Agent>>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert("Problem during getAllAgent()");
            }

            @Override
            public void onSuccess(List<Agent> agents) {
                Window.alert("Taille liste : "+agents.size());
                for(int i = 0 ; i < agents.size(); i++){
                    Window.alert("Ajout de l'agent a la liste : "+agents.get(i));
                    pompiers[i] = new PompierRecord(agents.get(i));
                }
            }
        });
        return pompiers;

    }
}