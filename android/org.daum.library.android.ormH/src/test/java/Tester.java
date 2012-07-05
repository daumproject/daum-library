import model.sitac.Moyen;
import model.sitac.MoyenType;
import model.sitac.TestModel;
import org.daum.library.ormH.api.IPersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistenceConfiguration;
import org.daum.library.ormH.persistence.PersistentClass;
import org.daum.library.ormH.store.LocalStore;
import org.daum.library.ormH.utils.PersistenceException;
import org.junit.Before;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 04/07/12
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */
public class Tester {



    public static void main(String argv[]) throws PersistenceException {



         LocalStore localStore = new LocalStore();
        IPersistenceConfiguration configuration=null;



            configuration = new PersistenceConfiguration("node0");
            configuration.setStore(localStore);
            configuration.addPersistentClass(MoyenType.class);







    }
}
