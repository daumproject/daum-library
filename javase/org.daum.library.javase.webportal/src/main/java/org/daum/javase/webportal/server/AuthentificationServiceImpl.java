package org.daum.javase.webportal.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.daum.javase.webportal.dto.AdapteurAgent;
import org.daum.javase.webportal.shared.Agent;
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
    public boolean authenticateAgent(String matricule, String password){

        //TODO get agent from ormH
        //Agent agent = new Agent();

        if(matricule.equals("admin") && password.equals("admin")) {
            return true;
            //get caracteristics of the agent
            /*agent.setLastname("Despagne");
               agent.setMatricule("pdespagn");
               agent.setFirstname("Pierre");
               agent.setLogged(true);*/
        }
        return false;
        /*if (agent.isLogged()) {
              storeAgentInSession(agent);
          }
          return agent;*/
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
        deleteAgentFromSession();
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


}
