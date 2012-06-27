package org.daum.library.replica.listener;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 20/06/12
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public interface IChangeListener
{
    public void addEventListener (Class zclass,PropertyChangeListener listener);
    public void removeEventListener (Class zclass,PropertyChangeListener listener);
    public void receive(Object msg);
}
