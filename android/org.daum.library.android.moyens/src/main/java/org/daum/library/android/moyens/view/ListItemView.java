package org.daum.library.android.moyens.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.daum.library.android.moyens.model.Demand;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/06/12
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
public class ListItemView extends LinearLayout {

    private static final String TAG = "ListItemView";
    private static boolean D = true;

    private Context ctx;
    private Demand demand;
    private TextView tv_agres,
                     tv_cis,
                     tv_ghDemande,
                     tv_ghDepart,
                     tv_ghCrm,
                     tv_ghEngage,
                     tv_ghDesengagement;

    public ListItemView(Context context, Demand demand) {
        super(context);
        this.ctx = context;
        this.demand = demand;
        initUI();
        configUI();
    }

    private void initUI() {
        tv_agres = new TextView(ctx);
        tv_cis = new TextView(ctx);
        tv_ghCrm = new TextView(ctx);
        tv_ghDemande = new TextView(ctx);
        tv_ghDepart = new TextView(ctx);
        tv_ghDesengagement = new TextView(ctx);
        tv_ghEngage = new TextView(ctx);
    }

    private void configUI() {
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        setPadding(5, 5, 5, 5);

        // configuring textViews
        tv_agres.setText(demand.agres);
        tv_cis.setText(demand.cis);
        tv_ghDemande.setText(demand.gh_demande);
        tv_ghDepart.setText(demand.gh_depart);
        tv_ghCrm.setText(demand.gh_crm);
        tv_ghEngage.setText(demand.gh_engage);
        tv_ghDesengagement.setText(demand.gh_desengagement);

        tv_agres.setGravity(Gravity.CENTER);
        tv_cis.setGravity(Gravity.CENTER);
        tv_ghDemande.setGravity(Gravity.CENTER);
        tv_ghDepart.setGravity(Gravity.CENTER);
        tv_ghCrm.setGravity(Gravity.CENTER);
        tv_ghEngage.setGravity(Gravity.CENTER);
        tv_ghDesengagement.setGravity(Gravity.CENTER);

        tv_agres.setTextSize(20);
        tv_cis.setTextSize(20);
        tv_ghDemande.setTextSize(20);
        tv_ghDepart.setTextSize(20);
        tv_ghCrm.setTextSize(20);
        tv_ghEngage.setTextSize(20);
        tv_ghDesengagement.setTextSize(20);

        LinearLayout.LayoutParams tvParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tvParams.weight = 1;

        addView(tv_agres, tvParams);
        addView(tv_cis, tvParams);
        addView(tv_ghDemande, tvParams);
        addView(tv_ghDepart, tvParams);
        addView(tv_ghCrm, tvParams);
        addView(tv_ghEngage, tvParams);
        addView(tv_ghDesengagement, tvParams);
    }

    public void setDemand(Demand demand) {
        tv_agres.setText(demand.agres);
        tv_cis.setText(demand.cis);
        tv_ghDemande.setText(demand.gh_demande);
        tv_ghDepart.setText(demand.gh_depart);
        tv_ghCrm.setText(demand.gh_crm);
        tv_ghEngage.setText(demand.gh_engage);
        tv_ghDesengagement.setText(demand.gh_desengagement);
        // update UI
        postInvalidate();
    }

    public void setTextColor(int color) {
        tv_agres.setTextColor(color);
        tv_cis.setTextColor(color);
        tv_ghDemande.setTextColor(color);
        tv_ghDepart.setTextColor(color);
        tv_ghCrm.setTextColor(color);
        tv_ghEngage.setTextColor(color);
        tv_ghDesengagement.setTextColor(color);
        // update UI
        postInvalidate();
    }
}
