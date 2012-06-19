package org.daum.library.android.moyens.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.daum.common.model.api.Demand;
import org.daum.common.util.api.TimeFormatter;

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
    private String[] data = new String[7];
    private TextView tv_agres,
                     tv_cis,
                     tv_ghDemande,
                     tv_ghDepart,
                     tv_ghCrm,
                     tv_ghEngage,
                     tv_ghDesengagement;

    public ListItemView(Context context, String[] data) {
        super(context);
        this.ctx = context;
        processData(data);
        initUI();
        configUI();
    }

    public ListItemView(Context context, Demand demand) {
        super(context);
        this.ctx = context;
        processDemandData(demand);
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

    private void processData(String[] data) {
        // default values
        for (String s : this.data) s = "";

        for (int i=0; i<data.length; i++) {
            if (i>=this.data.length) return;
            this.data[i] = data[i];
        }
    }

    private void processDemandData(Demand demand) {
        // filling data[] with the values from Demand
        data[0] = demand.getType().name()+((demand.getNumber() == null) ? "": demand.getNumber());
        data[1] = (demand.getCis() == null)
                ? "" : demand.getCis();
        data[2] = (demand.getGh_demande() == null)
                ? "" : TimeFormatter.getGroupeHoraire(demand.getGh_demande());
        data[3] = (demand.getGh_depart() == null)
                ? "" : TimeFormatter.getGroupeHoraire(demand.getGh_depart());
        data[4] = (demand.getGh_crm() == null)
                ? "" : TimeFormatter.getGroupeHoraire(demand.getGh_crm());
        data[5] = (demand.getGh_engage() == null)
                ? "" : TimeFormatter.getGroupeHoraire(demand.getGh_engage());
        data[6] = (demand.getGh_desengagement() == null)
                ? "" : TimeFormatter.getGroupeHoraire(demand.getGh_desengagement());
    }

    private void configUI() {
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        setPadding(5, 5, 5, 5);

        // configuring textViews
        tv_agres.setText(data[0]);
        tv_cis.setText(data[1]);
        tv_ghDemande.setText(data[2]);
        tv_ghDepart.setText(data[3]);
        tv_ghCrm.setText(data[4]);
        tv_ghEngage.setText(data[5]);
        tv_ghDesengagement.setText(data[6]);

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
        removeAllViews();
        processDemandData(demand);
        configUI();
        requestLayout();
    }

    public void setData(String[] data) {
        removeAllViews();
        processData(data);
        configUI();
        requestLayout();
    }

    public void setTextColor(int color) {
        tv_agres.setTextColor(color);
        tv_cis.setTextColor(color);
        tv_ghDemande.setTextColor(color);
        tv_ghDepart.setTextColor(color);
        tv_ghCrm.setTextColor(color);
        tv_ghEngage.setTextColor(color);
        tv_ghDesengagement.setTextColor(color);
    }
}
