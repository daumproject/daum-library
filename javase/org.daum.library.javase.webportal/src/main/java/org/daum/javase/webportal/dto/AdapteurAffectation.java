package org.daum.javase.webportal.dto;

import org.daum.common.genmodel.Moyens;
import org.daum.common.genmodel.SitacFactory;
import org.daum.javase.webportal.shared.Affectation;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Some;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/07/12
 * Time: 13:13
 * To change this template use File | Settings | File Templates.
 */
public class AdapteurAffectation {

    private PersistenceSessionFactoryImpl factory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public AdapteurAffectation(PersistenceSessionFactoryImpl factory){
        this.factory = factory;
    }

    public Affectation saveAffectation(Affectation affectation){
        PersistenceSession session = null;
        org.daum.common.genmodel.Affectation affectationSitac = sharedAffectationToSitac(affectation);
        try {
            session = factory.getSession();
            session.save(affectationSitac);
            //TODO session.lock();
            //TODO session.unlock();
        } catch (PersistenceException e) {
            logger.debug("Problem encountered while saving affectation ");
        } finally {
            if(session != null)
                session.close();
        }
        return affectation;
    }

    private org.daum.common.genmodel.Affectation sharedAffectationToSitac(Affectation affectation) {
        PersistenceSession session = null;
        org.daum.common.genmodel.Affectation affectationSitac = null;
        affectationSitac = SitacFactory.createAffectation();
        affectationSitac.setMoyen(new Some(affectation.getMoyens()));
        return affectationSitac;
    }


}
