package org.daum.library.replica.listener;

import org.daum.library.replica.msg.SyncEvent;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/06/12
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public interface SyncListener extends java.util.EventListener
{
    public void sync(SyncEvent e);
}
