package org.daum.library.ormHM.persistence;

import org.daum.library.ormHM.api.IPersistenceSession;
import org.daum.library.ormHM.api.PersistenceSessionStore;
import org.daum.library.ormHM.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 11:33
 */

public class PersistenceSession implements IPersistenceSession {

    private static Logger logger =  LoggerFactory.getLogger(PersistenceSession.class);
    private PersistenceSessionFactoryImpl factory;
    private PersistenceConfiguration conf;
    private PersistenceSessionStore store =null;

    public PersistenceSession(PersistenceSessionFactoryImpl persistenceSessionFactory) throws PersistenceException
    {
        this.factory = persistenceSessionFactory;
        this.conf = persistenceSessionFactory.getPersistenceConfiguration();
        store = conf.getConnectionConfiguration();
        if(store == null)
        {
            throw new PersistenceException("The standard Handler is not define");
        }
    }

    public void save(Object bean) throws PersistenceException
    {
        if(bean != null)
        {
            try
            {
                PersistentClass pc = null;
                pc=factory.getPersistenceConfiguration().getPersistentClass(bean);

                for (PersistentProperty pp : pc.getPersistentProperties())
                {
                    if(pp.isId())
                    {
                        Object idclass =  pp.getValue(bean);
                        if(idclass != null)
                        {
                            OrhmID id = new OrhmID(pp.getAttachTO(),idclass,pp.getMaxEntriesLocalHeap());
                            store.save(id, bean);
                        }
                        else
                        {
                            logger.error("the id is null the bean cannot be saved");
                            throw new PersistenceException("the annotation id is not define");
                        }
                        break;
                    }
                }
            }   catch (Exception e){
                logger.error("The persistence class is not added in addPersistentClass");

            }
        }else {
            logger.error(" Fail to save bean is null");
        }
    }

    @Override
    public void delete(Object bean) throws PersistenceException {
        if(bean != null)
        {
            try{
                PersistentClass pc = null;
                pc=factory.getPersistenceConfiguration().getPersistentClass(bean);

                for (PersistentProperty pp : pc.getPersistentProperties())
                {
                    if(pp.isId())
                    {
                        Object idclass =  pp.getValue(bean);
                        if(idclass != null)
                        {
                            OrhmID id = new OrhmID(pp.getAttachTO(),idclass,pp.getMaxEntriesLocalHeap());
                            store.delete(id);
                        }
                        else
                        {
                            logger.error("the id is null");
                            throw new PersistenceException("the annotation id is not define");
                        }
                        break;
                    }
                }
            }catch (Exception e){
                logger.error("The format of the bean is not correct or The persistence class is not added in addPersistentClassThe persistence class is not added in addPersistentClass");
            }
        }else {
            logger.error(" Fail to delete bean is null");
        }
    }

    public Object get(Class clazz,Object _id) throws PersistenceException
    {
        Object bean = null;
        PersistentClass pc= null;
        OrhmID id=null;
        try
        {
            pc = factory.getPersistenceConfiguration().getPersistentClass(clazz);
            id = new OrhmID(pc.getId().getAttachTO(),_id,pc.getId().getMaxEntriesLocalHeap());
            bean = store.get(id);
            return bean;
        } catch (Exception e)
        {
            logger.error("Persistence Session get "+id+" "+id.getAttachToCache(),e);
        }
        return  null;
    }



    public Map<Object,Object> getAll(Class clazz) throws PersistenceException
    {
        PersistentClass pc= null;
        OrhmID id=null;
        try
        {
            pc = factory.getPersistenceConfiguration().getPersistentClass(clazz);
            id = new OrhmID(pc.getId().getAttachTO(),null,pc.getId().getMaxEntriesLocalHeap());
            return store.getAll(id);

        } catch (Exception e)
        {
            logger.error("Persistence Session getAll "+id.getAttachToCache(),e);
        }
        return  null;
    }



    public Object lock(Class clazz,Object _id) throws PersistenceException
    {
        Object bean = null;
        PersistentClass pc= null;
        try
        {
            pc = factory.getPersistenceConfiguration().getPersistentClass(clazz);
            OrhmID id = new OrhmID(pc.getId().getAttachTO(),_id,pc.getId().getMaxEntriesLocalHeap());
            store.lock(id);
            bean = store.get(id);
            return bean;

        } catch (Exception e)
        {
            logger.error("",e);
        }
        return  null;
    }

    public void unlock(Object bean) throws PersistenceException
    {
        PersistentClass pc = null;
        pc=factory.getPersistenceConfiguration().getPersistentClass(bean);

        for (PersistentProperty pp : pc.getPersistentProperties())
        {
            if(pp.isId())
            {
                Object idclass =  pp.getValue(bean);
                if (idclass != null)
                {
                    OrhmID id = new OrhmID(pp.getAttachTO(),idclass,pp.getMaxEntriesLocalHeap());
                    store.unlock(id);
                }
                else
                {
                    logger.warn("the id is null");
                }

                break;
            }
        }
    }


    public void close()
    {
        factory.close(this);
    }
}