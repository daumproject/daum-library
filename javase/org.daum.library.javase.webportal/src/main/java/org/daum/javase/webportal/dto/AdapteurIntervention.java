package org.daum.javase.webportal.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.daum.common.genmodel.*;
import org.daum.common.genmodel.impl.AgentImpl;
import org.daum.common.genmodel.impl.InterventionImpl;
import org.daum.javase.webportal.shared.Agent;
import org.daum.javase.webportal.shared.Detachement;
import org.daum.javase.webportal.shared.Intervention;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Some;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/07/12
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
public class AdapteurIntervention {

    private PersistenceSessionFactoryImpl factory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public AdapteurIntervention(PersistenceSessionFactoryImpl factory){
        this.factory = factory;
    }

    public Intervention saveIntervention(Intervention intervention){
        PersistenceSession session = null;
        org.daum.common.genmodel.Intervention interventionSitac = sharedInterventionToSitac(intervention);
        try {
            session = factory.getSession();
            session.save(interventionSitac);
            intervention.setId(interventionSitac.getNumeroIntervention());
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

    public org.daum.common.genmodel.Intervention sharedInterventionToSitac(Intervention intervention) {
        org.daum.common.genmodel.Intervention interventionSitac = null;
        interventionSitac = SitacFactory.createIntervention();
        AdapteurPersonne adapteurPersonne = new AdapteurPersonne(factory);
        AdapteurDetachement adapteurDetachement = new AdapteurDetachement(factory);

         interventionSitac.setRequerant(new Some(adapteurPersonne.getPersonne(intervention.getIdRequerant())));

        for(String idDetachement : intervention.getListeIdDetachement()){
            interventionSitac.addDetachements(adapteurDetachement.getDetachement(idDetachement));
        }

        PositionCivil positionCivil = SitacFactory.createPositionCivil();
        positionCivil.setCp(intervention.getCodePostal());
        positionCivil.setNomRue(intervention.getAdresse());


        interventionSitac.setPosition(new Some(positionCivil));
        for(String idVictime : intervention.getListeIdVictime()){
            interventionSitac.addVictimes(adapteurPersonne.getPersonne(idVictime));
        }
        interventionSitac.setDescription(intervention.getDescription());

        return interventionSitac;
    }

    public List<Intervention> getAllIntervention(){

        PersistenceSession session = null;
        List<Intervention> listeIntervention = new ArrayList<Intervention>();
        try {
            session = factory.getSession();
            Map<String, InterventionImpl> interventions = session.getAll(InterventionImpl.class);
            for(Map.Entry<String, InterventionImpl> entry : interventions.entrySet()) {
                InterventionImpl interventionSitac = entry.getValue();
                Intervention interventionTemp = new Intervention();
                interventionTemp.setId(interventionSitac.getNumeroIntervention());
                interventionTemp.setDescription(interventionSitac.getDescription());
                interventionTemp.setIdRequerant(interventionSitac.getRequerant().get().getId());
                listeIntervention.add(interventionTemp);
            }
        } catch (PersistenceException e) {
            logger.debug("Problem encountered while getting Intervention List",e);
        } finally {
            if(session != null)
                session.close();
        }
        return listeIntervention;
    }



}
