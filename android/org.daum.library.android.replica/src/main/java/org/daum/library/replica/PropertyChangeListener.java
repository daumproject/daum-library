package org.daum.library.replica;

import org.daum.library.replica.msg.NotifyUpdate;

import java.util.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 20/06/12
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class PropertyChangeListener extends Observable
{
    private  Class zclass;
    public PropertyChangeListener(Class zclass)
    {
        this.zclass  = zclass;
    }

}
