package org.daum.javase.webportal.dto;

import org.daum.common.genmodel.SitacFactory;
import org.daum.common.genmodel.impl.AgentImpl;
import org.daum.javase.webportal.shared.Agent;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 24/07/12
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
public class AdapteurAgent {

    private PersistenceSessionFactoryImpl factory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public AdapteurAgent(PersistenceSessionFactoryImpl factory){
        this.factory = factory;
    }


    public void sitacFactoryAgentToWebportalAgent(Agent agent){

    }

    public org.daum.common.genmodel.Agent webPortalAgentToSitacAgent(Agent agentShared){
        org.daum.common.genmodel.Agent agentSitac = null;
        agentSitac = SitacFactory.createAgent();
        agentSitac.setNom(agentShared.getNom());
        agentSitac.setPrenom(agentShared.getPrenom());
        agentSitac.setMatricule(agentShared.getMatricule());
        agentSitac.setPassword(agentShared.getPassword());
        return agentSitac;
    }

    public Agent saveAgent(Agent agentShared){
        PersistenceSession session = null;
        org.daum.common.genmodel.Agent agentSitac = webPortalAgentToSitacAgent(agentShared);
        try {
            session = factory.getSession();
            session.save(agentSitac);
            agentShared.setId(agentSitac.getId());
            //TODO session.lock();
            //TODO session.unlock();
        } catch (PersistenceException e) {
            logger.debug("Problem encountered while saving object "+ agentSitac.getNom());
        } finally {
            if(session != null)
                session.close();
        }
        return agentShared;
    }

    public Agent getAgentSharedFromSitacAgent(org.daum.common.genmodel.Agent agentSitac){
        Agent agentShared = null;
        agentShared.setNom(agentSitac.getNom());
        agentShared.setPrenom(agentSitac.getPrenom());
        agentShared.setMatricule(agentSitac.getMatricule());
        agentShared.setPassword(agentSitac.getPassword());
        return agentShared;
    }

    public Agent getAgentSharedFromId(String idSitacAgent){
        PersistenceSession session = null;
        org.daum.common.genmodel.Agent agentSitac = null;
        Agent agentShared = null;
        try {
            session = factory.getSession();
            agentSitac = session.get(AgentImpl.class , idSitacAgent);
        } catch (PersistenceException e) {
            logger.debug("Problem encountered while saving object "+ agentSitac.getNom());
        } finally {
           if(session != null)
               session.close();
        }
        agentShared.setId(agentSitac.getId());
        agentShared.setNom(agentSitac.getNom());
        agentShared.setPrenom(agentSitac.getPrenom());
        agentShared.setMatricule(agentSitac.getMatricule());
        agentShared.setPassword(agentSitac.getPassword());
        return agentShared;
    }

    public List<Agent> getAllAgentFromSitac(){
        PersistenceSession session = null;
        List<Agent> listeAgents = new ArrayList<Agent>();
        try {
            session = factory.getSession();
            Map<String,AgentImpl> hashmapAgent = session.getAll(AgentImpl.class);
            for(Map.Entry<String, AgentImpl> entry : hashmapAgent.entrySet()) {
                AgentImpl agentSitac = entry.getValue();
                Agent agentTemp = new Agent();
                agentTemp.setId(agentSitac.getId());
                agentTemp.setNom(agentSitac.getNom());
                agentTemp.setPrenom(agentSitac.getPrenom());
                agentTemp.setMatricule(agentSitac.getMatricule());
                agentTemp.setPassword(agentSitac.getPassword());
                listeAgents.add(agentTemp);
            }
        } catch (PersistenceException e) {
            logger.debug("Problem encountered while getting Agent List");
        } finally {
            if(session != null)
                session.close();
        }
        return listeAgents;
    }




}
