package org.daum.library.android.daumauth.controller;

import org.kevoree.library.javase.authentication.Authentication;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/07/12
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
public interface IConnectionEngine extends Authentication {

    boolean isSynced();
}
