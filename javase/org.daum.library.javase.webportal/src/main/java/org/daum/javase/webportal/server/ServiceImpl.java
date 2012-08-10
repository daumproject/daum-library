package org.daum.javase.webportal.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.daum.javase.webportal.adapter.*;
import org.daum.javase.webportal.client.WebService;
import org.daum.javase.webportal.shared.*;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/13/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */

public class ServiceImpl extends RemoteServiceServlet implements WebService {
    private PersistenceSessionFactoryImpl factory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public ServiceImpl(PersistenceSessionFactoryImpl factory){
        this.factory = factory;
    }

    @Override
    public AgentDTO createAgent(String nom, String prenom, String matricule, String password) {
        AgentDTO agent = new AgentDTO(nom,prenom,matricule,password);
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        agent = adapteurAgent.saveAgent(agent);
        return agent;
    }

    @Override
    public boolean authenticateAgent(String matricule, String password)
    {
        List<AgentDTO> listeAgents = getAllAgent();
        for (AgentDTO a : listeAgents) {
            if (a.getMatricule().equals(matricule) && a.getPassword().equals(password)) {
                /*Agent agent = new Agent();
                agent.setMatricule(matricule);
                agent.setLogged(true);
                /* if (agent.isLogged()) {
                    storeAgentInSession(agent);
                }*/
                return true;
            }
        }
        return false;
    }

    private AgentDTO getAgentAlreadyFromSession() {
        AgentDTO agent = null;
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession();
        Object userObj = session.getAttribute("agent");
        if (userObj != null && userObj instanceof AgentDTO) {
            agent = (AgentDTO) userObj;
        }
        return agent;
    }


    private void storeAgentInSession(AgentDTO agent) {
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("agent", agent);
    }

    @Override
    public void logout() {
        //deleteAgentFromSession();
    }

    private void deleteAgentFromSession() {
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession();
        session.removeAttribute("agent");
    }

    @Override
    public AgentDTO loginFromSessionServer() {
        AgentDTO agent = getAgentAlreadyFromSession();
        return agent;
    }

    @Override
    public List<AgentDTO> getAllAgent() {
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        return adapteurAgent.getAllAgentFromSitac();
    }

    @Override
    public AgentDTO editAgent(AgentDTO agent) {
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        agent = adapteurAgent.editAgent(agent);
        return agent;
    }

    @Override
    public AgentDTO getAgent(String id) {
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        AgentDTO agent =  adapteurAgent.getAgentSharedFromId(id);
        return agent;
    }

    @Override
    public void deleteAgent(String id) {
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        adapteurAgent.deleteAgent(id);
    }

    @Override
    public InterventionDTO createIntervention(String description, PersonneDTO requerant, String codePostal, String adresse, List<String> listeIdAgent) {

        AdapteurPersonne adapteurPersonne = new AdapteurPersonne(factory);
        adapteurPersonne.savePersonne(requerant);

        AdapteurMoyens adapteurMoyens = new AdapteurMoyens(factory);
        MoyensDTO moyens = new MoyensDTO();
        moyens.setListeIdAgent(listeIdAgent);
        adapteurMoyens.saveMoyens(moyens);

        AffectationDTO affectation = new AffectationDTO();
        affectation.setIdMoyen(moyens.getId());
        AdapteurAffectation adapteurAffectation = new AdapteurAffectation(factory);
        adapteurAffectation.saveAffectation(affectation);

        DetachementDTO detachement = new DetachementDTO();
        detachement.addIdAffectation(affectation.getId());

        //TODO
        detachement.setIdChef(listeIdAgent.get(0));
        AdapteurDetachement adapteurDetachement = new AdapteurDetachement(factory);
        adapteurDetachement.saveDetachement(detachement);

        InterventionDTO intervention = new InterventionDTO();
        intervention.setDescription(description);
        intervention.setIdDetachement(detachement.getId());

        intervention.setIdRequerant(requerant.getId());
        AdapteurIntervention adapteurIntervention = new AdapteurIntervention(factory);


        return adapteurIntervention.saveIntervention(intervention);
    }

    @Override
    public List<InterventionDTO> getAllIntervention() {
        AdapteurIntervention adapteurIntervention = new AdapteurIntervention(factory);
        return adapteurIntervention.getAllIntervention();
    }


}
