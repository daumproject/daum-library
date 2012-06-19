package org.daum.library.ormHM.api;

import org.daum.library.ormHM.utils.PersistenceException;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 22/05/12
 * Time: 14:07
 */
public interface IPersistenceConfiguration {
    public PersistenceSessionStore getConnectionConfiguration();
    public IPersistenceSessionFactory getPersistenceSessionFactory();
    public void setStore(PersistenceSessionStore connectionConfiguration);
    public void addPersistentClass(Class pc) throws PersistenceException;
}
