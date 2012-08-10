package org.daum.javase.webportal.adapter;

import org.daum.common.genmodel.*;
import org.daum.common.genmodel.impl.InterventionImpl;
import org.daum.javase.webportal.shared.InterventionDTO;
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

    public InterventionDTO saveIntervention(InterventionDTO intervention){
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

    public org.daum.common.genmodel.Intervention sharedInterventionToSitac(InterventionDTO intervention)
    {
        System.out.println(intervention);
        org.daum.common.genmodel.Intervention interventionSitac = SitacFactory.createIntervention();

        AdapteurPersonne adapteurPersonne = new AdapteurPersonne(factory);
        AdapteurDetachement adapteurDetachement = new AdapteurDetachement(factory);

         interventionSitac.setRequerant(adapteurPersonne.getPersonne(intervention.getIdRequerant()));

        for(String idDetachement : intervention.getListeIdDetachement()){
            interventionSitac.addDetachements(adapteurDetachement.getDetachement(idDetachement));
        }

        PositionCivil positionCivil = SitacFactory.createPositionCivil();
        positionCivil.setCp(intervention.getCodePostal());
        positionCivil.setNomRue(intervention.getAdresse());


        interventionSitac.setPosition(positionCivil);
        for(String idVictime : intervention.getListeIdVictime()){
            interventionSitac.addVictimes(adapteurPersonne.getPersonne(idVictime));
        }
        interventionSitac.setDescription(intervention.getDescription());

        return interventionSitac;
    }

    public List<InterventionDTO> getAllIntervention(){

        PersistenceSession session = null;
        List<InterventionDTO> listeIntervention = new ArrayList<InterventionDTO>();
        try {
            session = factory.getSession();
            Map<String, InterventionImpl> interventions = session.getAll(InterventionImpl.class);
            for(Map.Entry<String, InterventionImpl> entry : interventions.entrySet()) {
                InterventionImpl interventionSitac = entry.getValue();
                InterventionDTO interventionTemp = new InterventionDTO();
                interventionTemp.setId(interventionSitac.getNumeroIntervention());
                interventionTemp.setDescription(interventionSitac.getDescription());
                interventionTemp.setIdRequerant(interventionSitac.getRequerant().getId());
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
