package org.daum.library.couchDB;

import org.lightcouch.Changes;
import org.lightcouch.CouchDbClient;
import org.lightcouch.ReplicationResult;
import org.lightcouch.Response;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 23/11/12
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class Tester {


    public static void  main(String argv[])
    {
        /*
        //192.168.1.134:48679
        //http://localhost:5984/
        CouchDbClient dbClient = new CouchDbClient("daum",true,"HTTP","localhost",5984,"","");




        Test foo = new Test();

        Response response = dbClient.save(foo);

        foo = dbClient.find(Test.class,         response.getId());


      System.out.println(response.getRev());




        Changes changes = dbClient.changes()
                .includeDocs(true)  .continuousChanges();

        while (changes.hasNext()) {
            // live access to continuous feeds
            org.lightcouch.ChangesResult.Row in   =changes.next();



            foo = dbClient.find(Test.class,         response.getId());

          System.out.println(foo.dddd);
            System.out.println(foo.id);


        }

        dbClient.shutdown();  */

    }
}
