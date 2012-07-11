package org.daum.library.android.daumauth.controller;


import android.content.Context;
import android.os.Handler;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/07/12
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public interface IController {

    void setConnectionEngine(IConnectionEngine engine);

    void setInterventionEngine(IInterventionEngine engine);

    void setTimeout(int delay);
}
