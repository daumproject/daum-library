package org.daum.common.mavlink.handler;

import org.daum.common.mavlink.messages.MAVLinkMessage;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 13/11/12
 * Time: 09:57
 * To change this template use File | Settings | File Templates.
 */
public interface IJMAVLinkReader {
    public void shutdown();
    public void addEventListener (MavLinkEventListener listener);
    public void removeEventListener (MavLinkEventListener listener);
}
