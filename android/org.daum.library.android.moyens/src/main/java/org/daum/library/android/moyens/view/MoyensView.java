package org.daum.library.android.moyens.view;

import android.content.Context;
import android.util.Pair;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import org.daum.library.android.moyens.model.Demand;
import org.daum.library.android.moyens.model.IResource;
import org.daum.library.android.moyens.model.ResourcesList;
import org.daum.library.android.moyens.model.Vehicle;
import org.daum.library.android.moyens.view.quickactionbar.QuickActionsBar;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
public class MoyensView extends RelativeLayout {

    private static final String TAG = "MoyensView";
    private static final boolean D = true;

    private static final int ID_LIST_VIEW = 1;
    private static final int ID_QA_BAR = 2;

    private Context ctx;
    private ResourcesList resources;
    private ListView listView;
    private ResourcesAdapter adapter;
    private QuickActionsBar qActionsBar;
    private ArrayList<Pair<String, ArrayList<String>>> qaBarData;

    public MoyensView(Context context) {
        super(context);
        this.ctx = context;
        initUI();
        configUI();
        defineCallbacks();
    }

    private void initUI() {
        resources = new ResourcesList();
        // TODO no dummy data
        for (int i=0; i<30; i++) {
            resources.add(new Vehicle());
            resources.add(new Demand());
        }
        listView = new ListView(ctx);
        adapter = new ResourcesAdapter(ctx, resources);
        qaBarData = new ArrayList<Pair<String, ArrayList<String>>>();
        // TODO no dummy data
        for (int i=0; i<6; i++) {
            ArrayList<String> actions = new ArrayList<String>();
            for (int j=0; j<15; j++) actions.add("YOUPi "+i+"_"+j);
            qaBarData.add(new Pair<String, ArrayList<String>>("Dre "+i, actions));
        }
        qActionsBar = new QuickActionsBar(ctx, qaBarData);
    }

    private void configUI() {
        // configuring quickActions bar
        qActionsBar.setId(ID_QA_BAR);
        RelativeLayout.LayoutParams barParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        barParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        // configuring list view
        listView.setId(ID_LIST_VIEW);
        listView.setAdapter(adapter);
        RelativeLayout.LayoutParams listParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        listParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        listParams.addRule(RelativeLayout.ABOVE, qActionsBar.getId());

        addView(listView, listParams);
        addView(qActionsBar, barParams);
    }

    private void defineCallbacks() {

    }

    /**
     * Change the actual content of the resourcesList to the one given
     * in parameter
     * @param res the new resourcesList of the view
     */
    public void setResources(ResourcesList res) {
        this.resources = res;
    }

    /**
     * Adds a resource to the resourcesList
     * @param res the resource to add
     */
    public void addResource(IResource res) {
        this.resources.add(res);
    }

    /**
     * Adds a list of resources to the actual resourcesList
     * @param res the resourcesList to add
     */
    public void addResources(ResourcesList res) {
        this.resources.addAll(res);
    }

    /**
     * Removes a resource from the resourcesList
     * @param res the resource to remove
     * @return true if the resource has been removed; false otherwise
     */
    public boolean removeResource(IResource res) {
        return this.resources.remove(res);
    }
}
