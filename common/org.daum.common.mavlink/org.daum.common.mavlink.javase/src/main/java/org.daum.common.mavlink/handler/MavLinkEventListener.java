package org.daum.common.mavlink.handler;

import org.daum.common.mavlink.messages.MAVLinkMessage;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 13/11/12
 * Time: 10:44
 * To change this template use File | Settings | File Templates.
 */
public interface MavLinkEventListener  extends java.util.EventListener  {
    void incomingMSG(MavLinkEvent evt);
}
