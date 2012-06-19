package org.daum.library.replica.utils;

import java.util.Date;
import java.util.Set;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 25/05/12
 * Time: 10:55
 */
public class SystemTime implements Time {

    public static final SystemTime INSTANCE = new SystemTime();

    public Date getCurrentDate() {
        return new Date();
    }

    public long getMilliseconds() {
        return System.currentTimeMillis();
    }

    public long getNanoseconds() {
        return System.nanoTime();
    }

    public int getSeconds() {
        return (int) (getMilliseconds() / MS_PER_SECOND);
    }

    public void sleep(long ms) throws InterruptedException {
        Thread.sleep(ms);
    }

    public Date getDatemin(Set<Object> dates)
    {
        Date min = new Date();
        for(Object date : dates )
        {
            Date   d = (Date)date;
            if(d.before(min)){
                min = d;
            }
        }
        return  min;
    }


    public Date getDatemax(Set<Object> dates)
    {
        if(dates.isEmpty()){

            Date min = (Date) dates.toArray()[0];

            for(Object date : dates )
            {
                Date   d = (Date)date;
                if(d.after(min)){
                    min = d;
                }
            }
            return  min;
        }
        return  null;
    }
}
