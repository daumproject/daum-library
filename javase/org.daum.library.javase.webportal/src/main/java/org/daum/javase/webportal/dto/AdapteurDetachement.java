package org.daum.javase.webportal.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.daum.common.genmodel.Affectation;
import org.daum.common.genmodel.Agent;
import org.daum.common.genmodel.SitacFactory;
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

    private org.daum.common.genmodel.Detachement sharedDetachementToSitac(Detachement detachement) {
        PersistenceSession session = null;
        org.daum.common.genmodel.Detachement detachementSitac = null;
        detachementSitac = SitacFactory.createDetachement();
        detachementSitac.setChef(new Some(detachement.getChef()));
        detachementSitac.setAffectation((List<Affectation>) detachement.getAffectation());
        return detachementSitac;
    }
}
