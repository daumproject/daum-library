package org.daum.library.android.moyens.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import org.sitac.Vehicule;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
public class DemandsAdapter extends BaseAdapter {

    private static final String TAG = "DemandsAdapter";

    private Context ctx;
    private ArrayList<Vehicule> demands;

    public DemandsAdapter(Context ctx, ArrayList<Vehicule> demands) {
        super();
        this.ctx = ctx;
        this.demands = demands;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // create a new view
            ListItemView item = new ListItemView(ctx, demands.get(i));
            convertView = item;
        } else {
            // update old view with data if possible
            ((ListItemView) convertView).setDemand(demands.get(i));

        }
        return convertView;
    }

    @Override
    public Object getItem(int i) {
        return demands.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return demands.size();
    }
}
