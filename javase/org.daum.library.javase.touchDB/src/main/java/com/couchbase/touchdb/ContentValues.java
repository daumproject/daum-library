package com.couchbase.touchdb;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/12/12
 * Time: 10:30
 * To change this template use File | Settings | File Templates.
 */
public class ContentValues extends HashMap {



    public String toString(){
        StringBuilder t = new StringBuilder();
        StringBuilder values = new StringBuilder();
               t.append("(");
        values.append("(");
                int count=0;
        for(Object key : keySet()){
            t.append(key);
            values.append(get(key));
              if(count >keySet().size()-1){
                  t.append(",");
                  values.append(",");
              }
                  count++;
        }
        t.append(")");
        values.append(")");
        return " VALUES"+values.toString();
    }
}
