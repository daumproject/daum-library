package org.daum.library.ormH.utils;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 20/06/12
 * Time: 11:26
 * To change this template use File | Settings | File Templates.
 */
public class StoreHelper {

    public static boolean  isUpdated(String cache,Class c){

        if(cache.equals(c.getName()))
        {
            return  true;
        }                  else {
            return false;
        }
    }
}
