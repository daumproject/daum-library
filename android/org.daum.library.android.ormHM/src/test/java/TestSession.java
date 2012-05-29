/*
import org.daum.library.ormHM.persistence.PersistenceConfiguration;
import org.daum.library.ormHM.persistence.PersistenceSession;
import org.daum.library.ormHM.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormHM.store.EhcacheStore;
import org.daum.library.ormHM.utils.PersistenceException;
import org.junit.Before;
import org.junit.Test;
*/

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 10:45
 */
public class TestSession {
   /*

    EhcacheTest ehcacheTeste=null;
    EhcacheTest ehcacheTeste2=null;



    Moyen m1 = new Moyen(new MoyenType(1), "FPT", 1);
    Moyen m2 = new Moyen(new MoyenType(2), "VSAV", 2);
    Moyen m3 = new Moyen(new MoyenType(1), "FPT", 3);
    Moyen m4 = new Moyen(new MoyenType(1), "FPT", 4);
    Moyen m5 = new Moyen(new MoyenType(2), "VSAV", 5);


    PersistenceConfiguration configuration=null;
    PersistenceSessionFactoryImpl factory=null;

    @Before
    public void init() throws PersistenceException {

        // running EhcacheServer
        ehcacheTeste = new EhcacheTest(4013);
        ehcacheTeste.start();
        ehcacheTeste2 = new EhcacheTest(4014);
        ehcacheTeste2.start();

        configuration = new PersistenceConfiguration();
        configuration.addPersistentClass(Moyen.class);
        configuration.addPersistentClass(TestModel.class);

    }



    @Test
    public void test_save() throws PersistenceException {

        EhcacheStore ehcacheHandler = new EhcacheStore(ehcacheTeste.getCacheManager());

        configuration.setConnectionConfiguration(ehcacheHandler);

        factory = configuration.getPersistenceSessionFactory();

        PersistenceSession s = factory.openSession();


        s.save(m1);
        s.save(m2);
        s.save(m3);
        s.save(m4);
        s.save(m5);


        Moyen _m1 = (Moyen)  s.get(Moyen.class, m1.getNumber());
        Moyen _m2 = (Moyen)  s.get(Moyen.class, m2.getNumber());

        assertEquals(m1.getNumber(), _m1.getNumber());
        assertEquals(m1.getType(),_m1.getType());
        assertEquals(m1.getName(),_m1.getName());



        assertEquals(m2.getNumber(),_m2.getNumber());
        assertEquals(m2.getType(),_m2.getType());
        assertEquals(m2.getName(),_m2.getName());


        s.close();



    }


    @Test
    public void test_getall() throws PersistenceException {
        EhcacheStore ehcacheHandler = new EhcacheStore(ehcacheTeste.getCacheManager());
        configuration.setConnectionConfiguration(ehcacheHandler);
        factory = configuration.getPersistenceSessionFactory();
        PersistenceSession s = factory.openSession();
        Map<Object,Object> result = s.getAll(Moyen.class);
        assertEquals(result.size(),5);
        s.close();
    }

    @Test
    public  void test_delete()throws PersistenceException {

        EhcacheStore ehcacheHandler = new EhcacheStore(ehcacheTeste.getCacheManager());
        configuration.setConnectionConfiguration(ehcacheHandler);
        factory = configuration.getPersistenceSessionFactory();
        PersistenceSession s = factory.openSession();
        s.delete(m1);
        Map<Object,Object> result = s.getAll(Moyen.class);
        assertEquals(result.size(),4);
        s.close();

    }

    @Test
    public  void test_replication() throws PersistenceException {

        EhcacheStore ehcacheHandler = new EhcacheStore(ehcacheTeste2.getCacheManager());
        configuration.setConnectionConfiguration(ehcacheHandler);
        factory = configuration.getPersistenceSessionFactory();
        PersistenceSession s = factory.openSession();
        s.delete(m1);
        Map<Object,Object> result = s.getAll(Moyen.class);
        assertEquals(result.size(),4);
        s.close();



    }



    @Test
    public void test_process() throws PersistenceException, InterruptedException {
       final int iteration = 1000;
        // INSTANCE 1
        Thread t1=  new Thread(new Runnable() {
            @Override
            public void run() {
                PersistenceSession s=null;
                EhcacheStore ehcacheHandler = new EhcacheStore(ehcacheTeste2.getCacheManager());
                configuration.setConnectionConfiguration(ehcacheHandler);
                factory = configuration.getPersistenceSessionFactory();
                try {
                    s = factory.openSession();


                    int i=0;

                    while (i < iteration){

                        TestModel t = new TestModel(i);
                        s.save(t);


                        i++;
                    }

                    
                    s.close();
                } catch (PersistenceException e) {
                    e.printStackTrace();
                }


            }
        });



        t1.start();

        t1.join();



        EhcacheStore ehcacheHandler = new EhcacheStore(ehcacheTeste2.getCacheManager());
        configuration.setConnectionConfiguration(ehcacheHandler);
        factory = configuration.getPersistenceSessionFactory();
        PersistenceSession s = factory.openSession();

        Map<Object,Object>  result=  s.getAll(TestModel.class);

        assertEquals(result.size(),iteration);
        s.close();




    }
      */


}
