import model.sitac.Moyen;
import model.sitac.MoyenType;
import org.junit.Test;
import org.kevoree.library.ormHM.persistence.PersistenceConfiguration;
import org.kevoree.library.ormHM.persistence.PersistenceSession;
import org.kevoree.library.ormHM.persistence.PersistenceSessionFactory;
import org.kevoree.library.ormHM.persistence.connection.EhcacheHandler;

import static junit.framework.Assert.assertEquals;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 10:45
 */
public class TestSession {


    /*

    test save and replication on two Ehcache
     */

 @Test
 public void testSave(){

     // running EhcacheServer
     EhcacheTest ehcacheTeste = new EhcacheTest(4013);
     ehcacheTeste.start();
     EhcacheTest ehcacheTeste2 = new EhcacheTest(4014);
     ehcacheTeste2.start();


     PersistenceConfiguration configuration1=null;
     PersistenceSessionFactory factory1=null;


     PersistenceConfiguration configuration2=null;
     PersistenceSessionFactory factory2=null;

     try
     {
         configuration1 = new PersistenceConfiguration();
         configuration1.addPersistentObject(Moyen.class);





         EhcacheHandler ehcacheHandler = new EhcacheHandler(ehcacheTeste.getCacheManager());


         configuration1.setConnectionConfiguration(ehcacheHandler);


         factory1 = configuration1.getPersistenceSessionFactory();
         PersistenceSession s = factory1.openSession();

         Moyen m1 = new Moyen(new MoyenType(1), "FPT", 1);
         Moyen m2 = new Moyen(new MoyenType(2), "VSAV", 2);
         Moyen m3 = new Moyen(new MoyenType(1), "FPT", 3);
         Moyen m4 = new Moyen(new MoyenType(1), "FPT", 4);
         Moyen m5 = new Moyen(new MoyenType(2), "VSAV", 5);

         s.save(m1);
         s.save(m2);
         s.save(m3);
         s.save(m4);
         s.save(m5);

         s.close();



         configuration2 = new PersistenceConfiguration();
         configuration2.addPersistentObject(Moyen.class);




         EhcacheHandler ehcacheHandler2 = new EhcacheHandler(ehcacheTeste2.getCacheManager());


         configuration2.setConnectionConfiguration(ehcacheHandler2);


         factory2 = configuration1.getPersistenceSessionFactory();

         PersistenceSession s2 = factory2.openSession();

         Moyen _m1 = (Moyen)  s2.get(Moyen.class, m1.getNumber());

         assertEquals(m1.getNumber(), _m1.getNumber());
         assertEquals(m1.getType(),_m1.getType());
         assertEquals(m1.getName(),_m1.getName());

         Moyen _m2 = (Moyen)  s2.get(Moyen.class, m2.getNumber());

         assertEquals(m2.getNumber(),_m2.getNumber());
         assertEquals(m2.getType(),_m2.getType());
         assertEquals(m2.getName(),_m2.getName());


         s.close();

         ehcacheTeste.shudown();
         ehcacheTeste2.shudown();

     } catch (Exception e) {
         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
     }
 }


        /*
    @Test
    public void testConcurrentSave(){

        // running EhcacheServer
        final EhcacheTest ehcacheTeste1 = new EhcacheTest(4013);
        ehcacheTeste1.start();
        final EhcacheTest ehcacheTeste2 = new EhcacheTest(4014);
        ehcacheTeste2.start();

        final   EhcacheTest ehcacheTeste3 = new EhcacheTest(4015);
        ehcacheTeste3.start();

        final   EhcacheTest ehcacheTeste4 = new EhcacheTest(4016);
        ehcacheTeste4.start();


        final  int nb=2500;
        final TestModel test = new TestModel(151);

        try
        {


            // INSTANCE 1
            Thread t1=  new Thread(new Runnable() {
                @Override
                public void run() {

                    PersistenceConfiguration configuration1=null;
                    PersistenceSessionFactory factory1=null;

                    try
                    {
                        configuration1 = new PersistenceConfiguration();
                        configuration1.addPersistentObject(TestModel.class);



                        EhcacheHandler ehcacheHandler1 = new EhcacheHandler(ehcacheTeste1.getCacheManager());
                        configuration1.setConnectionConfiguration(ehcacheHandler1);

                        factory1 = configuration1.getPersistenceSessionFactory();
                        PersistenceSession s = factory1.openSession();

                        s.save(test);

                        s.close();
                        int i=0;

                        while (i < nb){

                            s = factory1.openSession();
                            TestModel locked =null;
                            try {


                                locked =  (TestModel) s.lock(TestModel.class, test.getId());
                                locked.setSum(locked.getSum()+1);
                                s.save(locked);
                            } finally {
                                s.unlock(locked);
                            }



                            s.close();

                            i++;
                        }

                    } catch (PersistenceException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }
            });


            // INSTANCE 2
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    PersistenceConfiguration configuration1=null;
                    PersistenceSessionFactory factory1=null;

                    try
                    {
                        configuration1 = new PersistenceConfiguration();
                        configuration1.addPersistentObject(TestModel.class);

                        EhcacheHandler ehcacheHandler1 = new EhcacheHandler(ehcacheTeste2.getCacheManager());
                        configuration1.setConnectionConfiguration(ehcacheHandler1);
                        factory1 = configuration1.getPersistenceSessionFactory();
                        PersistenceSession s =null;


                        int i=0;

                        while (i < nb){

                            s = factory1.openSession();

                            TestModel _m1 = (TestModel)  s.get(TestModel.class, test.getId());


                            if(_m1 != null){

                                TestModel locked =  (TestModel) s.lock(TestModel.class, test.getId());
                                locked.setSum(locked.getSum() + 1);
                                s.save(locked);
                                s.unlock(locked);


                                i++;
                            }
                            s.close();
                        }

                    } catch (PersistenceException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }
            });



            // INSTANCE 3
            Thread t3=    new Thread(new Runnable() {
                @Override
                public void run() {

                    PersistenceConfiguration configuration1=null;
                    PersistenceSessionFactory factory1=null;

                    try
                    {
                        configuration1 = new PersistenceConfiguration();
                        configuration1.addPersistentObject(TestModel.class);

                        EhcacheHandler ehcacheHandler1 = new EhcacheHandler(ehcacheTeste3.getCacheManager());
                        configuration1.setConnectionConfiguration(ehcacheHandler1);

                        factory1 = configuration1.getPersistenceSessionFactory();
                        PersistenceSession s =null;


                        int i=0;

                        while (i < nb){

                            s = factory1.openSession();

                            TestModel _m1 = (TestModel)  s.get(TestModel.class, test.getId());


                            if(_m1 != null){

                                TestModel locked =  (TestModel) s.lock(TestModel.class, test.getId());
                                locked.setSum(locked.getSum()+1);
                                s.save(locked);
                                s.unlock(locked);


                                i++;
                            }
                            s.close();
                        }

                    } catch (PersistenceException e) {
                        e.printStackTrace();
                    }
                }
            });


            // INSTANCE 4
            Thread t4=    new Thread(new Runnable() {
                @Override
                public void run() {

                    PersistenceConfiguration configuration1=null;
                    PersistenceSessionFactory factory1=null;
                    Moyen m1 = new Moyen(new MoyenType(1), "FPT", 1);
                    try
                    {
                        configuration1 = new PersistenceConfiguration();
                        configuration1.addPersistentObject(TestModel.class);

                        EhcacheHandler ehcacheHandler1 = new EhcacheHandler(ehcacheTeste4.getCacheManager());
                        configuration1.setConnectionConfiguration(ehcacheHandler1);

                        factory1 = configuration1.getPersistenceSessionFactory();
                        PersistenceSession s =null;


                        int i=0;

                        while (i < nb){

                            s = factory1.openSession();

                            TestModel _m1 = (TestModel)  s.get(TestModel.class, test.getId());


                            if(_m1 != null){


                                TestModel locked =  (TestModel) s.lock(TestModel.class, test.getId());
                                locked.setSum(locked.getSum()+1);
                                s.save(locked);
                                s.unlock(locked);


                               // TestModel model =  (TestModel)  s.get(TestModel.class, test.getId()) ;
                                
                               // model.setSum(model.getSum()+1);
                                //s.save(model);

                                i++;
                            }
                            s.close();
                        }

                    } catch (PersistenceException e) {
                        e.printStackTrace();
                    }
                }
            });

            t1.start();
            t2.start();


            t3.start();
            t4.start();


            t1.join();


            t3.join();
            t2.join();
            PersistenceConfiguration configuration1=null;
            PersistenceSessionFactory factory1=null;

            try
            {
                configuration1 = new PersistenceConfiguration();
                configuration1.addPersistentObject(TestModel.class);



                EhcacheHandler ehcacheHandler1 = new EhcacheHandler(ehcacheTeste4.getCacheManager());
                configuration1.setConnectionConfiguration(ehcacheHandler1);

                factory1 = configuration1.getPersistenceSessionFactory();
                PersistenceSession s = factory1.openSession();

                TestModel _m1 = (TestModel)  s.get(TestModel.class, test.getId());

                System.out.println(_m1.getSum());

                assertEquals(_m1.getSum(), nb * 4);

            } catch (PersistenceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


            ehcacheTeste1.shudown();
            ehcacheTeste2.shudown();
            ehcacheTeste3.shudown();
            ehcacheTeste4.shudown();

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    } */
}
