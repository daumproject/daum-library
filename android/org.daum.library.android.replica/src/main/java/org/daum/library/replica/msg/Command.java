package org.daum.library.replica.msg;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 25/05/12
 * Time: 13:15
 */
public class Command extends AMessage {
    private static final long serialVersionUID = 1517L;


    public String toString(){
        return "Command "+event+" "+getSourceNode();
    }
}
