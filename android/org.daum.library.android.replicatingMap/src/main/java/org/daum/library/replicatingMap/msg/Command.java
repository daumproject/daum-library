package org.daum.library.replicatingMap.msg;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 25/05/12
 * Time: 13:15
 */
public class Command extends AMessage {
    private boolean  replicated;

    public boolean isReplicated() {
        return replicated;
    }

    public void setReplicated(boolean replicated) {
        this.replicated = replicated;
    }
}
