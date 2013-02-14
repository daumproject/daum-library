package org.daum.library.replica.channel;

import org.daum.library.replica.msg.Message;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 18:18
 */
public interface Channel {
    public void write(Message e);
   // public Update read();
}
