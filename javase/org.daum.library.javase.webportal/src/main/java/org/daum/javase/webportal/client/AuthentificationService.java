package org.daum.javase.webportal.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.daum.javase.webportal.shared.Agent;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/13/12
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath(value = "authentifService")
public interface AuthentificationService extends RemoteService {

    public Agent createAgent(String nom, String prenom, String matricule, String password);

    public boolean authenticateAgent(String matricule, String password) throws IllegalArgumentException;

    public void logout();

    public Agent loginFromSessionServer();

    public List<Agent> getAllAgent();

    public Agent editAgent(Agent agent);

    public Agent getAgent(String id);

    public void delete(String id);
}
