package org.daum.library.android.daumauth.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import org.daum.library.android.daumauth.listener.OnConnectionListener;
import org.daum.library.android.daumauth.listener.OnInterventionSelectedListener;
import org.daum.library.android.daumauth.util.ConnectionTask;
import org.daum.library.android.daumauth.view.DaumAuthView;
import org.daum.common.genmodel.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/07/12
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
public class Controller implements IController, IControllerListener {

    private static final String TAG = "DaumAuthController";

    private static final String TEXT_LOADING = "Tentative de connexion, veuillez patienter...";

    private static final int SHOW_INTERVENTION = 0;
    private static final int UPDATE_INTERVENTION = 1;
    private static final int SHOW_DIALOG = 2;

    private static final String PROGRESS_DIALOG = "progress_dialog";

    private Context ctx;
    private DaumAuthView authView;
    private OnConnectionListener connListener;
    private OnInterventionSelectedListener interListener;
    private ConnectionTask connTask;
    private Handler uiHandler;
    private int connTimeout;
    private IConnectionEngine connEngine;
    private IInterventionEngine interventionEngine;
    private ArrayList<Intervention> interventions;
    private final ProgressDialog pDialog;
    private boolean dialogWasDisplayed = false;

    public Controller(Context ctx, DaumAuthView authView) {
        this.ctx = ctx;
        this.authView = authView;

        // creating uiHandler into the UI Thread (because controller must be called in it)
        this.uiHandler = new Handler(callback);

        // creating loading dialog
        pDialog = new ProgressDialog(ctx);
        pDialog.setIndeterminate(true);
        pDialog.setMessage(TEXT_LOADING);
        pDialog.setOnCancelListener(cancelListener);
        pDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onConnectionButtonClicked(String matricule, String password) {
        showDialog(TEXT_LOADING, true);
        this.connTask = new ConnectionTask(connEngine, matricule, password, connTimeout);
        connTask.setOnEventListener(new ConnectionTask.OnEventListener() {
            @Override
            public void onConnectionTimedOut() {
                dismissDialog();
                if (connListener != null) connListener.onConnectionTimedout();
            }

            @Override
            public void onConnectionSucceeded() {
                dismissDialog();
                if (connListener != null) connListener.onConnected();

                // update UI
                uiHandler.obtainMessage(
                        SHOW_INTERVENTION, retrieveInterventionsItemized()).sendToTarget();
            }

            @Override
            public void onConnectionFailed() {
                dismissDialog();
                if (connListener != null) connListener.onConnectionRefused();
            }
        });

        Thread connThread = new Thread(connTask);
        connThread.start();
    }

    private ArrayList<String> retrieveInterventionsItemized() {
        ArrayList<String> items = new ArrayList<String>();
        interventions = interventionEngine.getInterventions();
        for (Intervention i : interventions) {
            Personne req = i.getRequerant().get();
            items.add(i.getPosition().get().toString()+" "+req.getNom()+" - "+req.getPrenom());
        }
        return items;
    }

    public void discardDialog() {
        dialogWasDisplayed = pDialog.isShowing();
        pDialog.dismiss();
    }

    public void reattachDialog() {
        if (dialogWasDisplayed) pDialog.show();
    }

    @Override
    public void dismissDialog() {
        pDialog.dismiss();
        dialogWasDisplayed = false;
    }

    @Override
    public void showDialog(final String msg, final boolean cancelable) {
        pDialog.setCancelable(cancelable);
        pDialog.setMessage(msg);
        uiHandler.obtainMessage(SHOW_DIALOG).sendToTarget();
        dialogWasDisplayed = true;
    }

    private void showDialog() {
        pDialog.show();
    }

    @Override
    public void onItemSelected(int position) {
        interListener.onInterventionSelected(interventions.get(position));
    }

    @Override
    public void setTimeout(int delay) {
        this.connTimeout = delay;
    }

    @Override
    public void setConnectionEngine(IConnectionEngine engine) {
        this.connEngine = engine;
    }

    @Override
    public void setInterventionEngine(IInterventionEngine engine) {
        this.interventionEngine = engine;
    }

    public void setConnectionUICallback(OnConnectionListener listener) {
        this.connListener = listener;
    }

    public void setInterventionUICallback(OnInterventionSelectedListener listener) {
        this.interListener = listener;
    }

    @Override
    public void updateUI() {
        // update UI
        uiHandler.obtainMessage(
                UPDATE_INTERVENTION, retrieveInterventionsItemized()).sendToTarget();
    }

    /*
     * Will process things on UI Thread, pretty convenient for UI managing
     */
    private final Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_INTERVENTION:
                    ArrayList<String> items = (ArrayList<String>) msg.obj;
                    authView.showInterventions(items);
                    return true;

                case UPDATE_INTERVENTION:
                    authView.updateInterventions((ArrayList<String>) msg.obj);
                    return true;

                case SHOW_DIALOG:
                    showDialog();
                    return true;
            }
            return false;
        }
    };

    private final DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dismissDialog();
            if (connTask != null) connTask.cancel();
        }
    };
}
