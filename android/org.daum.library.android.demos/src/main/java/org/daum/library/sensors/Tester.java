package org.daum.library.sensors;






/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 18/07/12
 * Time: 09:26
 * To change this template use File | Settings | File Templates.
 */
public class Tester {

    public static void  main (String argv[])   {
        /*

import org.daum.library.sensors.pojo.Victime;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;
import java.net.MalformedURLException;
import java.util.Date;
        HttpClient httpClient = new StdHttpClient.Builder()
                .url("http://192.168.1.113:8888")
                .build();


        CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
        CouchDbConnector db = new StdCouchDbConnector("mydatabase", dbInstance);

        db.createDatabaseIfNotExists();




      db.create(t);



        long duree,start;

        Victime t = new Victime();
        t.state = Victime.STATES.ALIVE;
        t.date = new Date();
        t.prenom = "jean-emile";
        t.nom = "DARTOIS";


        CouchDbClient dbClient = new CouchDbClient("eifniefjie",true,"http","192.168.1.113",8888,"","");

        CouchDbClient dbClient2 = new CouchDbClient("eifniefjie",true,"http","192.168.1.121",8888,"","");






        int nb = 10;
        int counter = 0;

        start= System.currentTimeMillis();
        for(int i=0;i<nb;i++){

            t.prenom = "jean-emile "+i;

            Response c= dbClient.save(t);

            Victime t2 =  null;

            do {
                try
                {
                    t2 = dbClient2.find(Victime.class, c.getId());
                }catch (Exception e){
                    //ignore

                }
            }  while (t2 == null) ;


            if(t2.prenom.equals(t.prenom)){
                counter++;
            }

        }

        System.out.println(counter);


        duree = (System.currentTimeMillis() - start)  / 1000;
        System.out.println("Duree ="+duree);


    */

    }



}
