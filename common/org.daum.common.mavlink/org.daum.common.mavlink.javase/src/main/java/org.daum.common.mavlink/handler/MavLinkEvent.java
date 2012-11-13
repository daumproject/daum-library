package org.daum.common.mavlink.handler;

import org.daum.common.mavlink.messages.MAVLinkMessage;

import java.util.EventObject;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 13/11/12
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */
public class MavLinkEvent extends EventObject
{

    private MAVLinkMessage msg;
    public MavLinkEvent(Object o,MAVLinkMessage msg)
    {
        super(o);
        this.msg = msg;
    }

    public MAVLinkMessage getMsg()
    {
        return msg;
    }

}
