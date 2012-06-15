package org.daum.library.ormHM.persistence;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.daum.library.ormHM.api.IPersistenceSession;
import org.daum.library.ormHM.api.PersistenceSessionStore;
import org.daum.library.ormHM.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;

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
                        Object id =  pp.getValue(bean);
                        if(id != null)
                        {
                            Orhm ormh = new Orhm(pp.getCacheName(),pp.getGenerationType(),pp.getName(),id);

                            if(ormh.getGenerationType() == GenerationType.UUID)
                            {
                                ormh.setId(UUID.randomUUID().toString());

                                Object valueConverted = ConvertUtils.convert(ormh.getId(), String.class);
                                try {
                                    PropertyUtils.setSimpleProperty(bean, ormh.getIdName(), valueConverted);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                }
                            }

                            store.save(ormh, bean);
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
                        Object id =  pp.getValue(bean);
                        if(id != null)
                        {
                            Orhm ormh = new Orhm(pp.getCacheName(),pp.getGenerationType(),pp.getName(),id);
                            store.delete(ormh);
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
        Orhm ormh=null;
        try
        {
            pc = factory.getPersistenceConfiguration().getPersistentClass(clazz);
            ormh = new Orhm(pc.getId().getCacheName(),null,null,_id);
            bean = store.get(ormh);
            return bean;
        } catch (Exception e)
        {
            logger.error("Persistence Session get "+ormh.getCacheName()+" ",e);
        }
        return  null;
    }



    public Map<Object,Object> getAll(Class clazz) throws PersistenceException
    {
        PersistentClass pc= null;
        Orhm id=null;
        try
        {
            pc = factory.getPersistenceConfiguration().getPersistentClass(clazz);
            //String cacheName,GenerationType generationType, Object id
            id = new Orhm(pc.getId().getCacheName(),null,null,null);
            return store.getAll(id);

        } catch (Exception e)
        {
            logger.error("Persistence Session getAll "+id.getCacheName(),e);
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
            Orhm  ormH = new Orhm(pc.getId().getCacheName(),null,null,_id);
            store.lock(ormH);
            bean = store.get(ormH);
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
                    Orhm  ormH = new Orhm(pc.getId().getCacheName(),null,null,idclass);
                    store.unlock(ormH);
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