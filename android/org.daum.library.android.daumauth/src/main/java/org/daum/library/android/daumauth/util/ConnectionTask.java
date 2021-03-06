package org.daum.library.android.daumauth.util;

import org.daum.library.android.daumauth.controller.IConnectionEngine;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/07/12
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionTask implements Runnable {

    private IConnectionEngine engine;
    private String matricule;
    private int delay;
    private String password;
    private OnEventListener listener;
    private boolean timedOut = false;
    private boolean cancel = false;

    public ConnectionTask(IConnectionEngine engine, String matricule, String password, int delay) {
        this.engine = engine;
        this.matricule = matricule;
        this.password = password;
        this.delay = delay;
    }

    @Override
    public void run() {
        // create & start timeout thread
        Timer timer = new Timer(delay, timerCallback);
        timer.start();

        // wait until replica is synced
        while (engine == null || !engine.isSynced()) {
            if (timedOut) return;
            if (cancel) {
                timer.discard();
                return;
            }
        }

        if (engine.authenticate(matricule, password)) {
            // matricule & password are ok
            if (listener != null) listener.onConnectionSucceeded();

        } else {
            // wrong matricule and/or password
            if (listener != null) listener.onConnectionFailed();
        }

        // discard timer because we don't need it anymore
        timer.discard();
    }

    public void setOnEventListener(OnEventListener listener) {
        this.listener = listener;
    }

    /**
     * Ends task immediately.
     */
    public void cancel() {
        cancel = true;
    }

    private final Timer.OnTimeExpiredListener timerCallback = new Timer.OnTimeExpiredListener() {
        @Override
        public void onTimeExpired() {
            // connection timeout
            if (listener != null) listener.onConnectionTimedOut();
            timedOut = true;
        }
    };

    public interface OnEventListener {
        void onConnectionTimedOut();

        void onConnectionSucceeded();

        void onConnectionFailed();
    }
}
