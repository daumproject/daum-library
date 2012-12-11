package org.daum.library.javase.apacheCouchDB;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/12/12
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public class Tester {


    public static void  main(String argv[]) throws IOException {


            System.out.println(ApacheCouchDB.class.getClassLoader().getResource("ubuntu32/apache-couchdb-binary.jar").getPath());


    }
}
