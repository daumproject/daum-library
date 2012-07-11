package org.daum.library.android.daumauth.listener;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/07/12
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */
public interface OnConnectionListener {

    void onConnected();

    void onConnectionRefused();

    void onConnectionTimedout();
}
