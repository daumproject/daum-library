package org.daum.library.fakeDemo.pojos;


import org.daum.library.ormH.annotations.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 29/05/12
 * Time: 11:01
 */
public class TemperatureMonitor implements Serializable{

    @Id
    public Date date;
    public double  value;
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    
    public String toString(){
        return ""+date+" ="+value;
    }
}
