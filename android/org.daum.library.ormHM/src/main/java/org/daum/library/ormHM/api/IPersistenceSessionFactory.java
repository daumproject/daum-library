package org.daum.library.ormHM.api;

import org.daum.library.ormHM.persistence.PersistenceConfiguration;
import org.daum.library.ormHM.persistence.PersistenceSession;
import org.daum.library.ormHM.utils.PersistenceException;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 22/05/12
 * Time: 14:01
 */
public interface IPersistenceSessionFactory {
    public PersistenceSession openSession() throws PersistenceException;
    public PersistenceSession getSession() throws PersistenceException;
    public void close(PersistenceSession session);
    public PersistenceConfiguration getPersistenceConfiguration();
}
