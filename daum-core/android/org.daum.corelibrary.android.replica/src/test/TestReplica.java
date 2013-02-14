import org.daum.library.replica.cache.StoreEvent;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.daum.library.replica.msg.NotifyUpdate;
/*
import org.junit.Before;
import org.junit.Test;

//import static org.junit.Assert.assertEquals;
*/
public class TestReplica {
  /*
    static  int count=0;
    static  int count2=0;
    static  int count3=0;
    static  int count4=0;

    @Before
    public void testChangeListener(){

        ChangeListener.getInstance("test").addEventListener(TestReplica.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent propertyChangeEvent) {
                switch (propertyChangeEvent.getEvent()){
                    case ADD:
                        count++;
                        break;
                }

            }
        });



        ChangeListener.getInstance("test").addEventListener(TestReplica.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent propertyChangeEvent) {
                switch (propertyChangeEvent.getEvent()){

                    case DELETE:
                        count2++;
                        break;
                }
            }
        });


        ChangeListener.getInstance("test").addEventListener(String.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent propertyChangeEvent) {
                switch (propertyChangeEvent.getEvent()){

                    case UPDATE:
                        count3++;
                        break;
                }


            }
        });


        ChangeListener.getInstance("test").addEventListener(String.class, new PropertyChangeListener() {
            @Override
            public void update(PropertyChangeEvent propertyChangeEvent) {
                switch (propertyChangeEvent.getEvent()){

                    case ADD:
                        count4++;
                        break;
                }


            }
        });



    }


    @Test
    public void testreceive(){

        int iteratenb=1000;

       for(int i=0;i<iteratenb;i++)
       {
           NotifyUpdate test = new NotifyUpdate();
           test.setEvent(StoreEvent.ADD);
           test.setCache(TestReplica.class.getName());
           ChangeListener.getInstance("test").receive(test);

           NotifyUpdate test2 = new NotifyUpdate();
           test2.setEvent(StoreEvent.DELETE);
           test2.setCache(TestReplica.class.getName());
           ChangeListener.getInstance("test").receive(test2);




           NotifyUpdate test3 = new NotifyUpdate();
           test3.setEvent(StoreEvent.UPDATE);
           test3.setCache(String.class.getName());
           ChangeListener.getInstance("test").receive(test3);

           NotifyUpdate test4 = new NotifyUpdate();
           test4.setEvent(StoreEvent.ADD);
           test4.setCache(String.class.getName());
           ChangeListener.getInstance("test").receive(test4);

       }

        assertEquals(count,iteratenb);
        assertEquals(count2,iteratenb);
        assertEquals(count3,iteratenb);
        assertEquals(count4,iteratenb);

    }
     */
}