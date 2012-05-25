package org.daum.library.replicatingMap.utils;

import java.util.Date;

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
}
