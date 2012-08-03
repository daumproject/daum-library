package org.daum.javase.webportal.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.daum.javase.webportal.shared.Agent;
import org.daum.javase.webportal.shared.Intervention;
import org.daum.javase.webportal.shared.Personne;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/13/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AuthentificationServiceAsync {
    void authenticateAgent(String matricule, String password, AsyncCallback<Boolean> callback);
    void createAgent(String nom, String prenom, String matricule , String password,AsyncCallback<Agent> callback);
    void logout(AsyncCallback<Void> callback);
    void loginFromSessionServer(AsyncCallback<Agent> callback);
    void  getAllAgent(AsyncCallback<List<Agent>> callback);
    void editAgent(Agent agentEdited, AsyncCallback<Agent> callback);
    void getAgent(String id, AsyncCallback<Agent> callback);
    void deleteAgent(String id, AsyncCallback<Void> asyncCallback);
    void createIntervention(String description, Personne requerant, String codePostal, String adresse, List<String> listeIdAgent, AsyncCallback<Intervention> asyncCallback);

    void getAllIntervention(AsyncCallback<List<Intervention>> asyncCallback);
}
