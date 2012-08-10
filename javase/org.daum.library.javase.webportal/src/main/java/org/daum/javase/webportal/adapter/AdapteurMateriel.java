package org.daum.javase.webportal.adapter;

import org.daum.javase.webportal.shared.MaterielDTO;
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

    public MaterielDTO saveMateriel(MaterielDTO materiel){
        //TODO
        return null;
    }


}
