package org.daum.library.android.daumauth.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import org.daum.library.android.daumauth.DaumAuthEngine;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/07/12
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionTask extends Thread implements Timer.OnTimeExpiredListener {

    private Context ctx;
    private DaumAuthEngine engine;
    private String matricule;
    private int delay;
    private String password;
    private OnEventListener listener;
    private boolean stop = false;
    private boolean synced;

    public ConnectionTask(DaumAuthEngine engine, String matricule, String password, int delay, boolean isSynced) {
        this.engine = engine;
        this.matricule = matricule;
        this.password = password;
        this.delay = delay;
        this.synced = isSynced;
    }

    @Override
    public void run() {
        Timer timer = new Timer(delay, this);
        timer.start();

        // wait until replica is synced
        while (!synced) {
            if (stop) return;
        }

        if (engine.authenticate(matricule, password)) {
            // matricule & password are ok
            if (listener != null) listener.onConnectionSucceeded(matricule);

        } else {
            // wrong matricule and/or password
            if (listener != null) listener.onConnectionFailed(matricule);
        }

        // discard timer because we don't need it anymore
        timer.discard();
    }

    @Override
    public void onTimeExpired() {
        // connection timeout
        if (listener != null) listener.onConnectionTimedOut();
        stop = true;
    }

    public void setSynced(boolean isSynced) {
        this.synced = isSynced;
    }

    public void setOnEventListener(OnEventListener listener) {
        this.listener = listener;
    }

    public interface OnEventListener {
        void onConnectionTimedOut();

        void onConnectionSucceeded(String matricule);

        void onConnectionFailed(String matricule);
    }
}
