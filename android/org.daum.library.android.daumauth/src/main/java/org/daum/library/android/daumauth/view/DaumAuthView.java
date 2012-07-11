package org.daum.library.android.daumauth.view;

import android.content.Context;
import android.view.View;
import android.util.Log;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import org.daum.library.android.daumauth.controller.Controller;
import org.daum.library.android.daumauth.controller.IController;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/07/12
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public class DaumAuthView extends AbstractDaumAuthView {

    private static final String TAG = "DaumAuthView";

    public enum State {
        NOT_CONNECTED,
        AUTHENTICATED
    }

    private Context ctx;
    private AuthenticationView authView;
    private InterventionListView interventionsView;
    private State state;

    public DaumAuthView(Context context, State state) {
        super(context);
        this.ctx = context;
        this.state = state;
        this.controller = new Controller(ctx, this);
        initUI();
        configUI(state);
        defineCallbacks();
    }

    private void initUI() {
        authView = new AuthenticationView(ctx);
        interventionsView = new InterventionListView(ctx);
    }

    private void configUI(State state) {
        setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        ViewGroup contentView = null;

        switch (state) {
            case NOT_CONNECTED:
                contentView = authView;
                break;

            case AUTHENTICATED:
                contentView = interventionsView;
                break;
        }

        addView(contentView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void defineCallbacks() {
        authView.setOnClickListener(controller);
        interventionsView.setOnItemSelectedListener(controller);
    }

    public IController getController() {
        return controller;
    }

    public void showInterventions(ArrayList<String> items) {
        removeAllViews();
        configUI(State.AUTHENTICATED);
        requestLayout();
        interventionsView.addItems(items);
    }

    public void showAuthentication() {
        removeAllViews();
        configUI(State.NOT_CONNECTED);
        requestLayout();
    }

    public State getState() {
        return state;
    }
}
