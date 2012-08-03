package org.daum.javase.webportal.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.daum.common.genmodel.Personne;
import org.daum.common.genmodel.PositionCivil;
import org.daum.common.genmodel.SitacFactory;
import org.daum.common.genmodel.impl.AgentImpl;
import org.daum.common.genmodel.impl.MoyensImpl;
import org.daum.javase.webportal.shared.Agent;
import org.daum.javase.webportal.shared.Intervention;
import org.daum.javase.webportal.shared.Moyens;
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
 * Time: 11:53
 * To change this template use File | Settings | File Templates.
 */
public class AdapteurMoyens {

    private PersistenceSessionFactoryImpl factory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public AdapteurMoyens(PersistenceSessionFactoryImpl factory){
        this.factory = factory;
    }



    public Moyens saveMoyens(Moyens moyens){
        PersistenceSession session = null;
        org.daum.common.genmodel.Moyens moyensSitac = sharedMoyensToSitac(moyens);
        try {
            session = factory.getSession();
            session.save(moyensSitac);
            moyens.setId(moyensSitac.getId());
            //TODO session.lock();
            //TODO session.unlock();
        } catch (PersistenceException e) {
            logger.debug("Problem encountered while saving moyens ");
        } finally {
            if(session != null)
                session.close();
        }
        return moyens;
    }

    private org.daum.common.genmodel.Moyens sharedMoyensToSitac(Moyens moyens) {
        PersistenceSession session = null;
        org.daum.common.genmodel.Moyens moyensSitac = null;
        moyensSitac = SitacFactory.createMoyens();
        for(String agentId : moyens.getListeIdAgent()){
            try {
                session = factory.getSession();
                moyensSitac.addAgent(session.get(AgentImpl.class, agentId));
            } catch (PersistenceException e) {
                logger.debug("Probleme while adding Agent to moyens", e);
            }
        }
        return moyensSitac;
    }

    public org.daum.common.genmodel.Moyens getMoyens(String idMoyens) {
        PersistenceSession session = null;
        org.daum.common.genmodel.Moyens moyensSitac = null;
        try {
            session = factory.getSession();
            moyensSitac = session.get(MoyensImpl.class, idMoyens);
        } catch (PersistenceException e) {
            logger.warn("Problem encountered while getting moyens ");
        } finally {
            if(session != null)
                session.close();
        }
        return moyensSitac;
    }
}
