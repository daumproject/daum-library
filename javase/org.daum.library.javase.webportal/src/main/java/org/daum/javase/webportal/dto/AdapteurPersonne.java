package org.daum.javase.webportal.dto;

import org.daum.common.genmodel.SitacFactory;
import org.daum.common.genmodel.impl.AgentImpl;
import org.daum.common.genmodel.impl.PersonneImpl;
import org.daum.javase.webportal.shared.Agent;
import org.daum.javase.webportal.shared.Personne;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/07/12
 * Time: 13:13
 * To change this template use File | Settings | File Templates.
 */
public class AdapteurPersonne {

    private PersistenceSessionFactoryImpl factory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public AdapteurPersonne(PersistenceSessionFactoryImpl factory){
        this.factory = factory;
    }

    public Personne savePersonne(Personne personneShared){
        PersistenceSession session = null;
        org.daum.common.genmodel.Personne personneSitac = personneSharedToPersonneSitac(personneShared);
        try {
            session = factory.getSession();
            session.save(personneSitac);
            personneShared.setId(personneSitac.getId());
            //TODO session.lock();
            //TODO session.unlock();
        } catch (PersistenceException e) {
            logger.debug("Problem encountered while saving object "+ personneSitac.getNom());
        } finally {
            if(session != null)
                session.close();
        }
        return personneShared;
    }

    private org.daum.common.genmodel.Personne personneSharedToPersonneSitac(Personne personneShared) {
        org.daum.common.genmodel.Personne personneSitac = null;
        personneSitac = SitacFactory.createPersonne();
        personneSitac.setNom(personneShared.getNom());
        personneSitac.setPrenom(personneShared.getPrenom());
        return personneSitac;

    }


    public org.daum.common.genmodel.Personne getPersonne(String idPersonne){
        PersistenceSession session = null;
        org.daum.common.genmodel.Personne personneSitac = null;
        try {
            session = factory.getSession();
            personneSitac = session.get(PersonneImpl.class, idPersonne);
        } catch (PersistenceException e) {
            logger.error("Problem encountered while getting Personne",e);
        } finally {
            if(session != null)
                session.close();
        }
        return personneSitac;
    }

}
