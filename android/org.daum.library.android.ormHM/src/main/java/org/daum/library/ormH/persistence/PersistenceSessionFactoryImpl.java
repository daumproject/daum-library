package org.daum.library.ormH.persistence;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 11:32
 */

import org.daum.library.ormH.api.IPersistenceSessionFactory;
import org.daum.library.ormH.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class PersistenceSessionFactoryImpl implements IPersistenceSessionFactory
{

  private static Logger log = LoggerFactory.getLogger(PersistenceSessionFactoryImpl.class);
  private PersistenceConfiguration persistenceConfiguration;
  protected static ThreadLocal<PersistenceSession> currentSession = new ThreadLocal<PersistenceSession>();

  public PersistenceSessionFactoryImpl(PersistenceConfiguration persistenceConfiguration)
  {
    this.persistenceConfiguration = persistenceConfiguration;
  }

  public PersistenceSession openSession() throws PersistenceException {
    return new PersistenceSession(this);
  }


  public PersistenceSession getSession() throws PersistenceException
  {
    log.debug("Getting current Persistence Session ");
    PersistenceSession session = currentSession.get();
    if (session == null)
    {
      session = openSession();
      currentSession.set(session);
    }
    return session;
  }

  public void close(PersistenceSession session)
  {
    PersistenceSession current = currentSession.get();
    if (current != null && current == session) {
      currentSession.set(null);
    }
    log.debug("Closing Persistence Session ");
  }

  public PersistenceConfiguration getPersistenceConfiguration() {
    return persistenceConfiguration;
  }

}
