package org.daum.library.ormHM.api;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 22/05/12
 * Time: 14:07
 */
public interface IPersistenceConfiguration {
    public PersistenceSessionStore getConnectionConfiguration();
    public IPersistenceSessionFactory getPersistenceSessionFactory();
    public void setConnectionConfiguration(PersistenceSessionStore connectionConfiguration);
}
