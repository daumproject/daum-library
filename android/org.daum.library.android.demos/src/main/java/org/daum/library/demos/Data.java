package org.daum.library.demos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 21/09/12
 * Time: 10:24
 * To change this template use File | Settings | File Templates.
 */
public class Data implements Serializable {

    private int counter;
    private ArrayList<Date> list;


    public Data(){
        counter = 100;
        list= new ArrayList<Date>();

       for(int i=0;i<counter;i++){
           list.add(new Date());
       }

    }

}
