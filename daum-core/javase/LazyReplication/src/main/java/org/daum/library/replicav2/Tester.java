package org.daum.library.replicav2;

import org.daum.library.replicav2.cache.Element;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 27/03/13
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class Tester {



    public static  void main(String argv[])
    {
        Node alice = new Node("Alice");
        Node ben = new Node("Ben");
        Node Cathy = new Node("Cathy");
        Node Dave = new Node("Dave");

        Element update = new Element(alice,"value");



        Element update1 = update.clone();

        update1.change(ben,"value1");


        Element update2 = update.clone();

        update2.change(Cathy,"value2");

        Element update3 = update.clone();

        update3.change(Dave,"value3");








      System.out.println(update.compare(update3));




    }
}
