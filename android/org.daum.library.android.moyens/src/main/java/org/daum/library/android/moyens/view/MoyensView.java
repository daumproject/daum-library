package org.daum.library.android.moyens.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Pair;
import android.view.View;
import android.widget.*;
import org.daum.library.android.moyens.view.listener.IMoyensListener;
import org.daum.library.android.moyens.view.quickactionbar.QuickActionsBar;
import org.daum.library.android.moyens.view.quickactionbar.listener.OnActionClickedListener;
import org.sitac.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
public class MoyensView extends RelativeLayout implements OnActionClickedListener {

    // Debugging
    private static final String TAG = "MoyensView";
    private static final boolean D = true;

    // view ID constants
    private static final int ID_LIST_HEADER = 1;
    private static final int ID_QA_BAR = 2;

    // String constants
    private static final String TEXT_EMPTY_LIST = "Aucune demande";
    private static final String TEXT_AGRES = "Agrès";
    private static final String TEXT_CIS = "CIS";
    private static final String TEXT_DEMAND = "Demande";
    private static final String TEXT_CRM = "Arrivé CRM";
    private static final String TEXT_DEPART = "Départ";
    private static final String TEXT_ENGAGE = "Engagé";
    private static final String TEXT_DESENG = "Désengagement";
    private static final String TEXT_DIALOG_TITLE = "Demande de confirmation";
    private static final String TEXT_DIALOG_MESSAGE = " : voulez-vous le désengager ?";
    private static final String TEXT_BTN_DESENG = "Désengager";
    private static final String TEXT_BTN_CANCEL = "Annuler";

    private Context ctx;
    private ArrayList<Vehicule> demands;
    private ListView listView;
    private DemandsAdapter adapter;
    private QuickActionsBar qActionsBar;
    private ArrayList<Pair<String, ArrayList<String>>> qaBarData;
    private IMoyensListener listener;
    private AlertDialog.Builder builder;

    public MoyensView(Context context) {
        super(context);
        this.ctx = context;
        initUI();
        configUI();
        defineCallbacks();
    }

    private void initUI() {
        demands = new ArrayList<Vehicule>();
        listView = new ListView(ctx);
        adapter = new DemandsAdapter(ctx, demands);
        qaBarData = new ArrayList<Pair<String, ArrayList<String>>>();
        for (VehiculeSector sector : VehiculeSector.values()) {
            ArrayList<String> actions = new ArrayList<String>();
            for (VehiculeType type : VehiculeType.getValues(sector)) actions.add(type.name());
            qaBarData.add(new Pair<String, ArrayList<String>>(sector.name(), actions));
        }
        qActionsBar = new QuickActionsBar(ctx, qaBarData);

        builder = new AlertDialog.Builder(ctx);
    }

    private void configUI() {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // configuring quickActions bar
        qActionsBar.setId(ID_QA_BAR);
        RelativeLayout.LayoutParams barParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        barParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        // configuring the textView displayed when there's no data in the listView
        TextView tv_emptyList = new TextView(ctx);
        tv_emptyList.setText(TEXT_EMPTY_LIST);
        tv_emptyList.setTextSize(20);
        RelativeLayout.LayoutParams emptyTvParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        emptyTvParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        // configuring the list header view
        String[] headerData = {TEXT_AGRES, TEXT_CIS, TEXT_DEMAND, TEXT_DEPART, TEXT_CRM, TEXT_ENGAGE, TEXT_DESENG};
        ListItemView headerView = new ListItemView(ctx, headerData);
        headerView.setTextColor(Color.WHITE);
        headerView.setId(ID_LIST_HEADER);
        headerView.setBackgroundColor(Color.BLACK);
        RelativeLayout.LayoutParams headerParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        headerParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // configuring list view
        listView.setAdapter(adapter);
        listView.setEmptyView(tv_emptyList);
        RelativeLayout.LayoutParams listParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        listParams.addRule(RelativeLayout.ABOVE, qActionsBar.getId());
        listParams.addRule(RelativeLayout.BELOW, headerView.getId());

        // configuring the AlertDialog for desengagement
        builder.setTitle(TEXT_DIALOG_TITLE);
        builder.setNegativeButton(TEXT_BTN_CANCEL, null);


        addView(headerView, headerParams);
        addView(listView, listParams);
        addView(tv_emptyList, emptyTvParams);
        addView(qActionsBar, barParams);
    }

    private void defineCallbacks() {
        qActionsBar.setOnActionClickedListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Vehicule d = demands.get(i);
                if (d.getGh_desengagement() == null) {
                    String num = (d.getNumber() == null) ? "" : d.getNumber();
                    String name = d.getVehiculeType()+num;
                    builder.setMessage(name+TEXT_DIALOG_MESSAGE);
                    builder.setPositiveButton(TEXT_BTN_DESENG, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            d.setGh_desengagement(new Date());
                            if (listener != null) listener.onDemandUpdated(d);
                        }
                    });
                    AlertDialog aDialog = builder.create();
                    aDialog.show();
                }
            }
        });
    }

    /**
     * Adds a resource to the demandList
     * @param d the resource to add
     */
    public void addDemand(Vehicule d) {
        updateDemand(d);
        listView.setSelection(demands.size()-1);
    }

    /**
     * Adds a list of demands to the actual demandList
     * @param dlist the demandList to add
     */
    public void addDemands(ArrayList<Vehicule> dlist) {
        for (Vehicule d : dlist) updateDemand(d);
        listView.setSelection(demands.size()-1);
    }

    public void updateDemand(Vehicule d) {
        for (Vehicule de : demands) {
            if (de.getId().equals(d.getId())) {
                demands.remove(de);
                demands.add(d);
                adapter.notifyDataSetChanged();
                return;
            }
        }
        demands.add(d);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActionClicked(String tab, String action) {
        if (listener != null) {
            Vehicule d = SitacFactory.createVehicule();
            d.setVehiculeType(VehiculeType.valueOf(action));
            listener.onDemandAsked(d);
        }
    }

    public void addEventListener(IMoyensListener listener) {
        this.listener = listener;
    }
}
