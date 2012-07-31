package org.daum.javase.webportal.dto;

import org.daum.common.genmodel.SitacFactory;
import org.daum.common.genmodel.Vehicule;
import org.daum.javase.webportal.shared.Materiel;
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
public class AdapteurMateriel {

    private PersistenceSessionFactoryImpl factory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public AdapteurMateriel(PersistenceSessionFactoryImpl factory){
        this.factory = factory;
    }

    public Materiel saveMateriel(Materiel materiel){
        //TODO
        return null;
    }


}
