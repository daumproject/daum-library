package org.daum.library.ormH.api;

import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.utils.PersistenceException;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 22/05/12
 * Time: 14:01
 */
public interface IPersistenceSessionFactory {
    public PersistenceSession getSession() throws PersistenceException;
    public void close(PersistenceSession session);
    public PersistenceConfiguration getPersistenceConfiguration();
}
