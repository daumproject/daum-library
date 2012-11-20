package org.daum.library.android.sitac.drone;

import org.daum.common.mavlink.handler.JMAVLinkReader;
import org.daum.common.mavlink.messages.MAVLinkMessage;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 13/11/12
 * Time: 10:19
 * To change this template use File | Settings | File Templates.
 */
public interface IMavLink
{
    public void write(MAVLinkMessage msg) throws IOException;
    public void shutdown();
    public JMAVLinkReader getMavLinkReader();

}
