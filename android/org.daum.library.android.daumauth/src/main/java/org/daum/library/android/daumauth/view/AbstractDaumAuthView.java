package org.daum.library.android.daumauth.view;

import android.content.Context;
import android.widget.RelativeLayout;
import org.daum.library.android.daumauth.controller.Controller;
import org.daum.library.android.daumauth.listener.OnConnectionListener;
import org.daum.library.android.daumauth.listener.OnInterventionSelectedListener;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/07/12
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDaumAuthView extends RelativeLayout {

    protected Controller controller;

    protected AbstractDaumAuthView(Context context) {
        super(context, null);
    }

    public void setOnConnectionListener(OnConnectionListener listener) {
        controller.setConnectionUICallback(listener);
    }

    public void setOnInterventionSelectedListener(OnInterventionSelectedListener listener) {
        controller.setInterventionUICallback(listener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        controller.discardDialog();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        controller.reattachDialog();
    }
}
