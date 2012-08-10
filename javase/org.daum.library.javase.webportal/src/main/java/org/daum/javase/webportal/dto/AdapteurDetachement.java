package org.daum.javase.webportal.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.daum.common.genmodel.Affectation;
import org.daum.common.genmodel.Agent;
import org.daum.common.genmodel.SitacFactory;
import org.daum.common.genmodel.impl.AffectationImpl;
import org.daum.common.genmodel.impl.DetachementImpl;
import org.daum.javase.webportal.shared.Detachement;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Some;
import scala.collection.immutable.List;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/07/12
 * Time: 11:52
 * To change this template use File | Settings | File Templates.
 */
public class AdapteurDetachement {

    private PersistenceSessionFactoryImpl factory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public AdapteurDetachement(PersistenceSessionFactoryImpl factory){
        this.factory = factory;
    }

    public Detachement saveDetachement(Detachement detachement){
        PersistenceSession session = null;
        org.daum.common.genmodel.Detachement detachementSitac = sharedDetachementToSitac(detachement);
        try {
            session = factory.getSession();
            session.save(detachementSitac);
            detachement.setId(detachementSitac.getId());
            //TODO session.lock();
            //TODO session.unlock();
            detachement.setId(detachementSitac.getId());
        } catch (PersistenceException e) {
            logger.debug("Problem encountered while saving affectation ");
        } finally {
            if(session != null)
                session.close();
        }
        return detachement;
    }

    public org.daum.common.genmodel.Detachement sharedDetachementToSitac(Detachement detachement) {
        org.daum.common.genmodel.Detachement detachementSitac = null;
        detachementSitac = SitacFactory.createDetachement();
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        detachementSitac.setChef(adapteurAgent.getAgent(detachement.getIdChef()));
        AdapteurAffectation adapteurAffectation = new AdapteurAffectation(factory);
        for(String idAffecation : detachement.getListeIdAffectation()){
            detachementSitac.addAffectation(adapteurAffectation.getAffectation(idAffecation));
        }
        return detachementSitac;
    }

    public  org.daum.common.genmodel.Detachement getDetachement(String idDetachement) {
        PersistenceSession session = null;
        org.daum.common.genmodel.Detachement detachementSitac = null;
        try {
            session = factory.getSession();
            detachementSitac = session.get(DetachementImpl.class, idDetachement);
            //TODO session.lock();
            //TODO session.unlock();
        } catch (PersistenceException e) {
            logger.debug("Problem encountered while saving affectation ");
        } finally {
            if(session != null)
                session.close();
        }
        return detachementSitac;
    }
}
