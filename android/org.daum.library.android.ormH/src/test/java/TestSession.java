/*
import org.daum.library.ormHM.persistence.PersistenceConfiguration;
import org.daum.library.ormHM.persistence.PersistenceSession;
import org.daum.library.ormHM.persistence.PersistenceSessionFactoryImpl;
import org.daum.library.ormHM.store.EhcacheStore;
import org.daum.library.ormHM.utils.PersistenceException;
import org.junit.Before;
import org.junit.Test;
*/

import model.sitac.Moyen;
import model.sitac.MoyenType;
import model.sitac.TestModel;
import org.daum.library.ormH.api.IPersistenceConfiguration;
import org.daum.library.ormH.api.IPersistenceSessionFactory;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceSession;
import org.daum.library.ormH.store.LocalStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 10:45
 */
public class TestSession {

    private LocalStore localStore = new LocalStore();
    IPersistenceConfiguration configuration=null;


    @Before
    public void configuration() throws PersistenceException {

        configuration = new PersistenceConfiguration("node0");
        configuration.setStore(localStore);

        configuration.addPersistentClass(Moyen.class);
        configuration.addPersistentClass(TestModel.class);
    }



    @Test
    public void test_save() throws PersistenceException {

        IPersistenceSessionFactory factory=null;

        factory = configuration.getPersistenceSessionFactory();

        PersistenceSession s = factory.getSession();

        assertEquals(0,s.getAll(Moyen.class).size());

        Moyen m1 = new Moyen(new MoyenType(1), "FPT");
        Moyen m2 = new Moyen(new MoyenType(2), "VSAV");
        Moyen m3 = new Moyen(new MoyenType(1), "FPT");
        Moyen m4 = new Moyen(new MoyenType(1), "FPT");
        Moyen m5 = new Moyen(new MoyenType(2), "VSAV");


        s.save(m1);

        s.save(m2);
        s.save(m3);
        s.save(m4);
        s.save(m5);


        Moyen _m1 = (Moyen)  s.get(Moyen.class, m1.getId());
        Moyen _m2 = (Moyen)  s.get(Moyen.class, m2.getId());

        assertEquals(m1.getId(), _m1.getId());
        assertEquals(m1.getType(),_m1.getType());
        assertEquals(m1.getName(),_m1.getName());


        assertEquals(m2.getId(),_m2.getId());
        assertEquals(m2.getType(),_m2.getType());
        assertEquals(m2.getName(),_m2.getName());

        // test update

        m1.setName("FPT2");

        s.update(m1);

        Moyen _m3 = (Moyen)  s.get(Moyen.class, m1.getId());

        assertEquals(m1.getName(),_m3.getName());
        s.close();

    }



    @Test
    public void test_getall() throws PersistenceException {
        IPersistenceSessionFactory factory=null;
        factory = configuration.getPersistenceSessionFactory();
        PersistenceSession s = factory.getSession();
        Map<Object,Moyen> result = (Map<Object, Moyen>) s.getAll(Moyen.class);
        assertEquals(result.size(),5);
        s.close();
    }

    @Test
    public  void test_delete()throws PersistenceException {
        IPersistenceSessionFactory factory=null;
        factory = configuration.getPersistenceSessionFactory();
        PersistenceSession s = factory.getSession();
        Map<Object,Moyen> re = (Map<Object, Moyen>) s.getAll(Moyen.class);
        Moyen m1=null;
        for( Object key : re.keySet())
        {
            m1 = re.get(key);
            break;
        }

        s.delete(m1);

        Map<Object,Moyen> result = (Map<Object, Moyen>) s.getAll(Moyen.class);
        assertEquals(result.size(),4);
        s.close();
    }






    @Test
    public void test_process() throws PersistenceException, InterruptedException {

        final int iteration = 5000;
        // INSTANCE 1
        Thread t1=  new Thread(new Runnable() {
            @Override
            public void run() {
                IPersistenceSessionFactory factory=null;
                factory = configuration.getPersistenceSessionFactory();
                PersistenceSession s=null;
                try {
                    s = factory.getSession();


                    int i=0;

                    while (i < iteration){

                        TestModel t = new TestModel();
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



        IPersistenceSessionFactory factory=null;
        factory = configuration.getPersistenceSessionFactory();
        PersistenceSession s = factory.getSession();
        Map<Object,TestModel>  result= (Map<Object, TestModel>) s.getAll(TestModel.class);

        assertEquals(result.size(),iteration);
        s.close();

    }


}
