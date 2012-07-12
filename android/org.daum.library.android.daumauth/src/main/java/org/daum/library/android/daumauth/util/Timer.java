package org.daum.library.android.daumauth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/07/12
 * Time: 15:51
 *
 */
public class Timer extends Thread {

    private static final String TAG = "Timer";
    private static final Logger logger = LoggerFactory.getLogger(Timer.class);

    private int delay;
    private boolean shutUp = false;
    private OnTimeExpiredListener callback;

    public Timer(int delay, OnTimeExpiredListener callback) {
        this.delay = delay;
        if (callback == null) {
            throw new IllegalArgumentException("OnTimeExpiredListener callback can't be null");
        } else {
            this.callback = callback;
        }
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                wait(delay);
            }
            if (shutUp) return;
            callback.onTimeExpired();

        } catch (InterruptedException e) {
            logger.error(TAG, "Timer error", e);
        }
    }

    /**
     * This will immediately stop the timer
     */
    public void discard() {
        shutUp = true;
        synchronized (this) {
            notify();
        }
    }

    public interface OnTimeExpiredListener {

        /**
         * Called when time expired
         */
        void onTimeExpired();
    }
}
