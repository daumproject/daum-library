package org.daum.library.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/06/12
 * Time: 11:52
 * To change this template use File | Settings | File Templates.
 */
public class Courbe {

    private List<Double> values = new ArrayList<Double>();

    public double getVal(int i){
        return values.get(i);
    }
    public int length(){
        return values.size();
    }

    public void add(double val){
        values.add(val);
    }

}