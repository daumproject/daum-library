package org.daum.library.android.daumauth.controller;

import android.app.ProgressDialog;
import android.content.Context;
import org.daum.library.android.daumauth.listener.OnConnectionListener;
import org.daum.library.android.daumauth.listener.OnInterventionSelectedListener;
import org.daum.library.android.daumauth.util.ConnectionTask;
import org.daum.library.android.daumauth.view.DaumAuthView;
import org.sitac.Intervention;
import org.sitac.Personne;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/07/12
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
public class Controller implements IController, IControllerListener {

    private static final String TEXT_LOADING = "Tentative de connexion, veuillez patienter...";

    private Context ctx;
    private DaumAuthView authView;
    private OnConnectionListener connListener;
    private OnInterventionSelectedListener interListener;
    private ConnectionTask connTask;
    private int connTimeout;
    private IConnectionEngine connEngine;
    private IInterventionEngine interventionEngine;
    private ArrayList<Intervention> interventions;
    private final ProgressDialog pDialog;

    public Controller(Context ctx, DaumAuthView authView) {
        this.ctx = ctx;
        this.authView = authView;
        // creating loading dialog
        pDialog = new ProgressDialog(ctx);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setMessage(TEXT_LOADING);
    }

    @Override
    public void onConnectionButtonClicked(String matricule, String password) {
        this.connTask = new ConnectionTask(connEngine, matricule, password, connTimeout);
        connTask.setOnEventListener(new ConnectionTask.OnEventListener() {
            @Override
            public void onConnectionTimedOut() {
                pDialog.dismiss();
                if (connListener != null) connListener.onConnectionTimedout();
            }

            @Override
            public void onConnectionSucceeded() {
                pDialog.dismiss();
                if (connListener != null) connListener.onConnected();

                // update UI
                ArrayList<String> items = new ArrayList<String>();
                interventions = interventionEngine.getInterventions();
                for (Intervention i : interventions) {
                    Personne req = i.getRequerant().get();
                    items.add("["+i.getNumeroIntervention()+"] "+req.getNom()+" - "+req.getPrenom());
                }
                authView.showInterventions(items);
            }

            @Override
            public void onConnectionFailed() {
                pDialog.dismiss();
                if (connListener != null) connListener.onConnectionRefused();
            }
        });

        Thread connThread = new Thread(connTask);
        connThread.start();
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
}
