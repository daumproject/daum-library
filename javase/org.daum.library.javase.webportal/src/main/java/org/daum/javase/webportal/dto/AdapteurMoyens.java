package org.daum.javase.webportal.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.daum.common.genmodel.Personne;
import org.daum.common.genmodel.PositionCivil;
import org.daum.common.genmodel.SitacFactory;
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



    public Intervention saveMoyens(Moyens moyens){
        PersistenceSession session = null;
        org.daum.common.genmodel.Moyens moyensSitac = sharedMoyensToSitac(moyens);
        try {
            session = factory.getSession();
            session.save(moyens);
            //TODO session.lock();
            //TODO session.unlock();
        } catch (PersistenceException e) {
            logger.debug("Problem encountered while saving affectation ");
        } finally {
            if(session != null)
                session.close();
        }
        return intervention;
    }

    private org.daum.common.genmodel.Moyens sharedMoyensToSitac(Moyens moyens) {
        PersistenceSession session = null;
        org.daum.common.genmodel.Moyens moyensSitac = null;
        moyensSitac = SitacFactory.createMoyens();
        AdapteurAgent adapteurAgent = new AdapteurAgent(factory);
        for(Agent agent : moyens.getListeAgent()){
            agent.g
        }
        adapteurAgent.saveAgent()


        //moyensSitac.addAllAgents(moyens.getListeAgent());

    }

    private org.daum.common.genmodel.Intervention sharedInterventionToSitac(Intervention intervention) {
        PersistenceSession session = null;
        org.daum.common.genmodel.Intervention interventionSitac = null;
        interventionSitac = SitacFactory.createIntervention();
        interventionSitac.setDetachements((scala.collection.immutable.List<org.daum.common.genmodel.Detachement>) intervention.getDetachement());
        interventionSitac.setRequerant(new Some(intervention.getRequerant()));
        PositionCivil positionCivil = new PositionCivil();
        positionCivil.setCp(intervention.getCodePostal());
        positionCivil.setNomRue(intervention.getAdresse());
        interventionSitac.setPosition(new Some(positionCivil));
        interventionSitac.setRequerant(new Some(intervention.getRequerant()));
        interventionSitac.setVictimes((scala.collection.immutable.List<Personne>) intervention.getListeVictime());
        return interventionSitac;
    }

}
