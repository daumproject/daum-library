package org.daum.library.ormH.persistence;


import org.daum.library.ormH.HelperList;
import org.daum.library.ormH.api.IPersistenceSession;
import org.daum.library.ormH.api.PersistenceSessionStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.None;
import scala.Some;
import scala.collection.mutable.ListBuffer;

import java.util.ArrayList;
import java.util.List;
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

                if(pp != null)
                {
                    Object id =  pp.getValue(bean);
                    Orhm ormh = new Orhm(pp.getCacheName(),pp.getGeneratedType(),id);
                    //  System.out.println(ormh);

                    // Generate ID  and set
                    if(ormh.getGeneratedType() == GeneratedType.UUID)
                    {
                        if(ormh.getId().getClass() !=  String.class && ormh.getId() != null)
                        {
                            throw new PersistenceException("GeneratedType : "+ GeneratedType.UUID+" need to be  : String id = new String()");
                        }else
                        {
                            if(ormh.getId().toString().length() == 0)
                            {
                                ormh.setId(conf.getNodeID()+"+"+UUID.randomUUID().toString());
                                logger.debug("Creating id "+ormh.getId()+" "+pp.getCacheName());
                                pp.getField().set(bean, ormh.getId());
                            }
                        }
                    }

                    // Manage OneToMany and ManyToOne
                    for(PersistentProperty p : pc.getPersistentProperties())
                    {
                        if(p.getF_ManyToOne() != null)
                        {
                            Object _bean =  p.getF_ManyToOne().get(bean);
                            // save it
                            if(_bean instanceof  scala.Some)
                            {
                                if(((Some)_bean).get() != null)
                                {
                                    save(((Some)_bean).get());
                                }

                            }else
                            {
                                if(_bean != null)
                                {
                                    save(_bean);
                                }
                            }
                        }

                        if(p.getF_OneToMany() != null)
                        {
                            Object _bean =  p.getF_OneToMany().get(bean);
                            if(_bean instanceof ArrayList)
                            {
                                for(Object o: ((ArrayList)_bean))
                                {
                                    if(o instanceof  scala.Some)
                                    {
                                        if(((Some)o).get() != null)
                                        {
                                            save(((Some)o).get());
                                        }

                                    }else {
                                        if(_bean != null)
                                        {
                                            save(o);
                                        }
                                    }
                                }
                            }   else if(_bean instanceof  scala.collection.mutable.ListBuffer)
                            {
                                HelperList t = new HelperList();

                                for(Object o: t.convert((scala.collection.mutable.ListBuffer)_bean))
                                {
                                    if(o instanceof  scala.Some){
                                        if(((Some)o).get() != null)
                                        {
                                            save(((Some)o).get());
                                        }

                                    }else {
                                        if(_bean != null)
                                        {
                                            save(o);
                                        }
                                    }

                                }
                            }
                        }
                    }


                    store.save(ormh, bean);


                }
                else
                {
                    logger.error("the id is null the bean cannot be saved");
                    throw new PersistenceException("the id is null the bean cannot be saved");
                }



            }   catch (Exception e)
            {
                logger.error("The persistence class "+bean.getClass()+" is not added in addPersistentClass ",e);
                System.out.println("The persistence class "+bean.getClass()+" is not added in addPersistentClass "+e);
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

                    // Manage OneToMany and ManyToOne
                    for(PersistentProperty p : pc.getPersistentProperties())
                    {
                        if(p.getF_ManyToOne() != null)
                        {
                            Object _bean =  p.getF_ManyToOne().get(bean);
                            // save it
                            if(_bean instanceof  scala.Some)
                            {
                                if(((Some)_bean).get() != null)
                                {
                                    update(((Some)_bean).get());
                                }
                            }else
                            {
                                if(_bean != null){
                                    update(_bean);
                                }
                                update(_bean);
                            }
                        }

                        if(p.getF_OneToMany() != null)
                        {
                            Object _bean =  p.getF_OneToMany().get(bean);
                            if(_bean instanceof ArrayList)
                            {
                                for(Object o: ((ArrayList)_bean))
                                {                                       if(o != null)
                                {
                                    if(o instanceof  scala.Some)
                                    {
                                        if(((Some)o).get() != null)
                                        {
                                            update(((Some)o).get());
                                        }

                                    }else {
                                        if(o != null)
                                        {
                                            update(o);
                                        }
                                    }
                                }
                                }
                            }   else if(_bean instanceof  scala.collection.mutable.ListBuffer)
                            {
                                HelperList t = new HelperList();

                                for(Object o: t.convert((scala.collection.mutable.ListBuffer)_bean))
                                {
                                    if(o != null)
                                    {
                                        if(o instanceof  scala.Some)
                                        {
                                            if(((Some)o).get() != null)
                                            {
                                                update(((Some)o).get());
                                            }

                                        }else
                                        {
                                            if(o != null)
                                            {
                                                update(o);
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }


                    Orhm ormh = new Orhm(pp.getCacheName(),pp.getGeneratedType(),id);
                    store.update(ormh, bean);
                }
                else
                {
                    logger.error("the id is null the bean cannot be saved");
                    throw new PersistenceException("the annotation id is not define");
                }


            }   catch (Exception e){
                logger.error("The persistence class "+bean.getClass()+" is not added in addPersistentClass ",e);

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
                logger.error("The persistence class "+bean.getClass()+" is not added in addPersistentClass ",e);           }
        }else {
            logger.error(" Fail to delete bean is null");
        }
    }

    public <T> T get(Class<T> clazz,Object _id) throws PersistenceException
    {
        Object bean = null;
        PersistentClass pc= null;
        Orhm ormh=null;
        try
        {
            pc = factory.getPersistenceConfiguration().getPersistentClass(clazz);
            ormh = new Orhm(pc.getPersistentPropertyID().getCacheName(),GeneratedType.NONE,_id);
            bean = store.get(ormh);
            return (T) bean;
        } catch (Exception e)
        {
            logger.error("Persistence Session get "+ormh.getCacheName()+" ",e);
        }
        return  null;
    }



    public <T> Map<Object, T> getAll(Class<T> clazz) throws PersistenceException
    {
        PersistentClass pc= null;
        Orhm id=null;
        try
        {
            pc = factory.getPersistenceConfiguration().getPersistentClass(clazz);
            //String cacheName,GeneratedType generationType, Object id
            id = new Orhm(pc.getPersistentPropertyID().getCacheName(),GeneratedType.NONE,null);
            return (Map<Object, T>) store.getAll(id);

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