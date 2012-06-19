package org.daum.library.replica.msg;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 25/05/12
 * Time: 14:07
 */
public class Updates  extends AMessage {

    private  LinkedBlockingQueue<Update> updates = new LinkedBlockingQueue<Update>();

    public LinkedBlockingQueue<Update> getUpdates() {
        return updates;
    }

    public void setUpdates(LinkedBlockingQueue<Update> updates) {
        this.updates = updates;
    }
}
