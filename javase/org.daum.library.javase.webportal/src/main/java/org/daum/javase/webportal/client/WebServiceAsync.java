package org.daum.javase.webportal.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.daum.javase.webportal.shared.AgentDTO;
import org.daum.javase.webportal.shared.InterventionDTO;
import org.daum.javase.webportal.shared.PersonneDTO;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/13/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WebServiceAsync {
    void authenticateAgent(String matricule, String password, AsyncCallback<Boolean> callback);
    void createAgent(String nom, String prenom, String matricule , String password,AsyncCallback<AgentDTO> callback);
    void logout(AsyncCallback<Void> callback);
    void loginFromSessionServer(AsyncCallback<AgentDTO> callback);
    void  getAllAgent(AsyncCallback<List<AgentDTO>> callback);
    void editAgent(AgentDTO agentEdited, AsyncCallback<AgentDTO> callback);
    void getAgent(String id, AsyncCallback<AgentDTO> callback);
    void deleteAgent(String id, AsyncCallback<Void> asyncCallback);
    void createIntervention(String description, PersonneDTO requerant, String codePostal, String adresse, List<String> listeIdAgent, AsyncCallback<InterventionDTO> asyncCallback);

    void getAllIntervention(AsyncCallback<List<InterventionDTO>> asyncCallback);
}
