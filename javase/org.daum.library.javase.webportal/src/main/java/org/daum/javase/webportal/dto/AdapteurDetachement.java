package org.daum.javase.webportal.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/07/12
 * Time: 11:52
 * To change this template use File | Settings | File Templates.
 */
public class AdapteurDetachement implements IsSerializable {

    private PersistenceSessionFactoryImpl factory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public AdapteurDetachement(PersistenceSessionFactoryImpl factory){
        this.factory = factory;
    }

}
