package org.daum.javase.webportal.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.daum.javase.webportal.shared.Agent;

import java.util.List;

public class PompierData {

    private static final AuthentificationServiceAsync loginService = GWT.create(AuthentificationService.class);
	private static PompierRecord[] records;  

	public static PompierRecord[] getRecords() {  
		if (records == null) {  
			records = getNewRecords();
		}  
		return records;  
	}  

	public static PompierRecord[] getNewRecords() {
        PompierRecord[] pompiers = new PompierRecord[]{};
		return new PompierRecord[]{  
				//new PompierRecord("Despagne", "Pierre"),
				//new PompierRecord("Boschat", "Thierry"),
		};  
	}

    public static List<Agent> getAllAgents(){
        final List<Agent> listeAgents = null;
        loginService.getAllAgent(new AsyncCallback<List<Agent>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(List<Agent> agents) {
                listeAgents.addAll(agents);
            }
        });
        return listeAgents;
    }

    public static PompierRecord[] getNewPompiersRecord() {
       List<Agent> listeAgent = getAllAgents();
       return (PompierRecord[]) listeAgent.toArray();
    }
}