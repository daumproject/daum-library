package org.daum.library.web;


import org.daum.common.genmodel.*;

import org.kevoree.extra.marshalling.RichJSONObject;

import scala.Option;
import scala.Some;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 06/07/12
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class Tester {

    public static void main(String argvv[]){

        Intervention intervention = SitacFactory.createIntervention();


        Personne requerant = SitacFactory.createPersonne();
        requerant.setNom("Nom requerant");
        requerant.setPrenom("Prenom requerant");

        Personne vitc1 = SitacFactory.createPersonne();
        vitc1.setNom("Nom victime1");
        vitc1.setPrenom("Prenom victime1");

        Personne vitc2 = SitacFactory.createPersonne();
        vitc2.setNom("Nom victime2");
        vitc2.setPrenom("Prenom victime2");


       // intervention.setRequerant(new Some(requerant));
        //RichJSONObject c = new RichJSONObject(new Demand(VehicleType.CAEM));

    //    System.out.println(c.toJSON());
    }
}
