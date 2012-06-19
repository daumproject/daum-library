package org.daum.library.ormHM.persistence;


import org.daum.library.ormHM.api.IPersistenceSession;
import org.daum.library.ormHM.api.PersistenceSessionStore;
import org.daum.library.ormHM.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                PersistentProperty pp = pc.getPersistentPropertyID();
                Object id =  pp.getValue(bean);
                if(id != null)
                {
                    Orhm ormh = new Orhm(pp.getCacheName(),pp.getGeneratedType(),id);

                    if(ormh.getGeneratedType() == GeneratedType.UUID)
                    {
                        if(ormh.getId().getClass() !=  String.class && ormh.getId() != null)
                        {
                            throw new PersistenceException("GeneratedType : "+ GeneratedType.UUID+" need to be  : String id = new String()");
                        }else
                        {
                            ormh.setId(conf.getNodeID()+"+"+UUID.randomUUID().toString());

                           pp.getFieldID().set(bean, ormh.getId());
                           // Object valueConverted = ConvertUtils.convert(ormh.getId(), ormh.getId().getClass());
                            // update id in bean
                            //PropertyUtils.setProperty(bean, ormh.getIdName(), valueConverted);
                        }
                    }
                    store.save(ormh, bean);
                }
                else
                {
                    logger.error("the id is null the bean cannot be saved");
                    throw new PersistenceException("the annotation id is not define");
                }



            }   catch (Exception e){
                logger.error("The persistence class is not added in addPersistentClass");

            }
        }else {
            logger.error(" Fail to save bean is null");
        }
    }

    @Override
    public void update(Object bean) throws PersistenceException {
        if(bean != null)
        {
            try
            {
                PersistentClass pc = null;
                pc=factory.getPersistenceConfiguration().getPersistentClass(bean);

                PersistentProperty pp =  pc.getPersistentPropertyID();

                Object id =  pp.getValue(bean);
                if(id != null)
                {
                    Orhm ormh = new Orhm(pp.getCacheName(),pp.getGeneratedType(),id);
                    store.update(ormh, bean);
                }
                else
                {
                    logger.error("the id is null the bean cannot be saved");
                    throw new PersistenceException("the annotation id is not define");
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

                PersistentProperty pp =   pc.getPersistentPropertyID();
                Object id =  pc.getPersistentPropertyID().getValue(bean);
                if(id != null)
                {
                    Orhm ormh = new Orhm(pp.getCacheName(),pp.getGeneratedType(),id);
                    store.delete(ormh);
                }
                else
                {
                    logger.error("the id is null");
                    throw new PersistenceException("the annotation id is not define");
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
            ormh = new Orhm(pc.getPersistentPropertyID().getCacheName(),GeneratedType.NONE,_id);
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
            //String cacheName,GeneratedType generationType, Object id
            id = new Orhm(pc.getPersistentPropertyID().getCacheName(),GeneratedType.NONE,null);
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
            Orhm  ormH = new Orhm(pc.getPersistentPropertyID().getCacheName(),GeneratedType.NONE,_id);
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

        Object idclass =  pc.getPersistentPropertyID().getValue(bean);
        if (idclass != null)
        {
            Orhm  ormH = new Orhm(pc.getPersistentPropertyID().getCacheName(),GeneratedType.NONE,idclass);
            store.unlock(ormH);
        }
        else
        {
            logger.warn("the id is null");
        }
    }


    public void close()
    {
        factory.close(this);
    }
}