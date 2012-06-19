package org.daum.library.demos;

import org.daum.library.ormH.store.LocalStore;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormH.utils.PersistenceException;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 18/06/12
 * Time: 10:48
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "Android")
@ComponentType
public class DemoLocalStore  extends AbstractComponentType implements  Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LocalStore localStore = new LocalStore();
    private  Thread t = new Thread(this);
    PersistenceConfiguration configuration=null;
    PersistenceSessionFactoryImpl factory=null;


    @Start
    public void start() {
        try
        {
            configuration = new PersistenceConfiguration(getNodeName());
            configuration.setStore(localStore);
            configuration.addPersistentClass(TestDemo.class);
            t.start();
        } catch (PersistenceException e)
        {
            e.printStackTrace();
        }

    }

    @Stop
    public void stop() {
        t.interrupt();
    }

    @Update
    public void update() {

    }

    @Override
    public void run() {
        PersistenceSession s=null;
        try
        {

            factory = configuration.getPersistenceSessionFactory();

            while(!Thread.currentThread().isInterrupted())
            {
                s = factory.openSession();
                if(s !=null)
                {
                    TestDemo test = new TestDemo();
                    s.save(test);


                    Map<Object,Object> result =  s.getAll(TestDemo.class);
                    int count =      result.size();

                    logger.debug("Number of Test Demo "+count);
                    if(count > 25 )
                    {
                        for(Object key : result.keySet()){

                            s.delete(result.get(key));
                        }
                    }

                }
                s.close();
                Thread.sleep(500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            if(s != null)
            {
                s.close();
            }
        }


    }
}
