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

    public Agent editAgent(Agent agentShared){
        PersistenceSession session = null;
        org.daum.common.genmodel.Agent agentSitac = null;
        try {
            session = factory.getSession();
            agentSitac = session.get(AgentImpl.class, agentShared.getId());
            agentSitac.setNom(agentShared.getNom());
            agentSitac.setPrenom(agentShared.getPrenom());
            agentSitac.setMatricule(agentShared.getMatricule());
            agentSitac.setPassword(agentShared.getPassword());
            session.save(agentSitac);
            //TODO session.lock();
            //TODO session.unlock();
        } catch (PersistenceException e) {
            logger.debug("Problem encountered while saving object "+ agentSitac.getNom());
        } finally {
            if(session != null)
                session.close();
        }
        return getAgentSharedFromSitacAgent(agentSitac);
    }

    public Agent getAgentSharedFromSitacAgent(org.daum.common.genmodel.Agent agentSitac){
        Agent agentShared = new Agent();
        agentShared.setId(agentSitac.getId());
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
            agentSitac = session.get(AgentImpl.class, idSitacAgent);
            agentShared = new Agent();
            agentShared.setId(agentSitac.getId());
            agentShared.setNom(agentSitac.getNom());
            agentShared.setPrenom(agentSitac.getPrenom());
            agentShared.setMatricule(agentSitac.getMatricule());
            agentShared.setPassword(agentSitac.getPassword());
        } catch (PersistenceException e) {
            logger.warn("Problem encountered while getting object "+ agentSitac.getNom());
        } finally {
           if(session != null)
               session.close();
        }
        return agentShared;
    }

    public List<Agent> getAllAgentFromSitac(){
        PersistenceSession session = null;
        List<Agent> listeAgents = new ArrayList<Agent>();
        try {
            session = factory.getSession();
            Map<String, AgentImpl> agents = session.getAll(AgentImpl.class);
            for(Map.Entry<String, AgentImpl> entry : agents.entrySet()) {
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
            logger.error("Problem encountered while getting Agent List",e);
        } finally {
            if(session != null)
                session.close();
        }
        return listeAgents;
    }

    public void deleteAgent(String id){
        PersistenceSession session = null;
        org.daum.common.genmodel.Agent agentSitac = null;
        Agent agentShared = null;
        try {
            session = factory.getSession();
            agentSitac = session.get(AgentImpl.class, id);
            session.delete(agentSitac);
        } catch (PersistenceException e) {
            logger.error("Problem encountered while delete Agent",e);
        } finally {
            if(session != null)
                session.close();
        }
    }


}
