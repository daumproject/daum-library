package org.daum.javase.webportal.server;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.daum.javase.webportal.dto.*;
import org.daum.javase.webportal.shared.*;
import org.daum.javase.webportal.client.AuthentificationService;
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

public class AuthentificationServiceImpl extends RemoteServiceServlet implements AuthentificationService {
    private PersistenceSessionFactoryImpl factory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public AuthentificationServiceImpl(PersistenceSessionFactoryImpl factory){
        this.factory = factory;
    }

    @Override
    public Agent createAgent(String nom, String prenom, String matricule, String password) {
        Agent agent = new Agent(nom,prenom,matricule,password);
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        agent = adapteurAgent.saveAgent(agent);
        return agent;
    }

    @Override
    public boolean authenticateAgent(String matricule, String password)
    {
        List<Agent> listeAgents = getAllAgent();
        for (Agent a : listeAgents) {
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

    private Agent getAgentAlreadyFromSession() {
        Agent agent = null;
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession();
        Object userObj = session.getAttribute("agent");
        if (userObj != null && userObj instanceof Agent) {
            agent = (Agent) userObj;
        }
        return agent;
    }


    private void storeAgentInSession(Agent agent) {
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
    public Agent loginFromSessionServer() {
        Agent agent = getAgentAlreadyFromSession();
        return agent;
    }

    @Override
    public List<Agent> getAllAgent() {
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        return adapteurAgent.getAllAgentFromSitac();
    }

    @Override
    public Agent editAgent(Agent agent) {
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        agent = adapteurAgent.editAgent(agent);
        return agent;
    }

    @Override
    public Agent getAgent(String id) {
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        Agent agent =  adapteurAgent.getAgentSharedFromId(id);
        return agent;
    }

    @Override
    public void deleteAgent(String id) {
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        adapteurAgent.deleteAgent(id);
    }

    @Override
    public Intervention createIntervention(String description, Personne requerant, String codePostal, String adresse, List<String> listeIdAgent) {
        AdapteurPersonne adapteurPersonne = new AdapteurPersonne(factory);
        adapteurPersonne.savePersonne(requerant);

        AdapteurMoyens adapteurMoyens = new AdapteurMoyens(factory);
        Moyens moyens = new Moyens();
        moyens.setListeIdAgent(listeIdAgent);
        adapteurMoyens.saveMoyens(moyens);

        Affectation affectation = new Affectation();
        affectation.setIdMoyen(moyens.getId());
        AdapteurAffectation adapteurAffectation = new AdapteurAffectation(factory);
        adapteurAffectation.saveAffectation(affectation);

        Detachement detachement = new Detachement();
        detachement.addIdAffectation(affectation.getId());

        //TODO
        detachement.setIdChef(listeIdAgent.get(0));
        AdapteurDetachement adapteurDetachement = new AdapteurDetachement(factory);
        adapteurDetachement.saveDetachement(detachement);

        Intervention intervention = new Intervention();
        intervention.setDescription(description);
        intervention.setIdDetachement(detachement.getId());
        AdapteurIntervention adapteurIntervention = new AdapteurIntervention(factory);
        intervention.setIdRequerant(requerant.getId());

        return adapteurIntervention.saveIntervention(intervention);
    }

    @Override
    public List<Intervention> getAllIntervention() {
        AdapteurIntervention adapteurIntervention = new AdapteurIntervention(factory);
        return adapteurIntervention.getAllIntervention();
    }


}
