package org.daum.javase.webportal.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.daum.javase.webportal.shared.AgentDTO;
import org.daum.javase.webportal.shared.InterventionDTO;
import org.daum.javase.webportal.shared.PersonneDTO;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/13/12
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath(value = "authentifService")
public interface WebService extends RemoteService {

    public AgentDTO createAgent(String nom, String prenom, String matricule, String password);

    public boolean authenticateAgent(String matricule, String password) throws IllegalArgumentException;

    public void logout();

    public AgentDTO loginFromSessionServer();

    public List<AgentDTO> getAllAgent();

    public AgentDTO editAgent(AgentDTO agent);

    public AgentDTO getAgent(String id);

    public void deleteAgent(String id);

    public InterventionDTO createIntervention(String description, PersonneDTO requerant, String codePostal, String adresse, List<String> listeIdAgent);

    public List<InterventionDTO> getAllIntervention();
}
