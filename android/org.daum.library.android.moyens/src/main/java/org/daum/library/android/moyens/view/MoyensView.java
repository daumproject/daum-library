package org.daum.library.android.moyens.view;

import android.content.Context;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import org.daum.library.android.moyens.model.IResource;
import org.daum.library.android.moyens.model.ResourcesList;

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

    private static final String TEXT_RES_DEMAND = "Je demande";

    private Context ctx;
    private Button btn_resDemand;
    private ResourcesList resources;
    private ListView listView;
    private ResourcesAdapter adapter;

    public MoyensView(Context context) {
        super(context);
        this.ctx = context;
        initUI();
        configUI();
        defineCallbacks();
    }

    private void initUI() {
        resources = new ResourcesList();
        listView = new ListView(ctx);
        adapter = new ResourcesAdapter(ctx, resources);
        btn_resDemand = new Button(ctx);
    }

    private void configUI() {

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
