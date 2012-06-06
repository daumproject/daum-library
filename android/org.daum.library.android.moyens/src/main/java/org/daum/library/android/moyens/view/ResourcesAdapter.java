package org.daum.library.android.moyens.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import org.daum.library.android.moyens.model.ResourcesList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
public class ResourcesAdapter extends BaseAdapter {

    private Context ctx;
    private ResourcesList resources;

    public ResourcesAdapter(Context ctx, ResourcesList resources) {
        super();
        this.ctx = ctx;
        this.resources = resources;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // create a new view
            // TODO
            TextView tv = new TextView(ctx);
            tv.setText(resources.get(i).toString());
            tv.setTextSize(20);
            convertView = tv;
        } else {
            // update old view with data if possible
            // TODO
            if (convertView instanceof TextView) {
                ((TextView) convertView).setText(resources.get(i).toString());
            }

        }
        return convertView;
    }

    @Override
    public Object getItem(int i) {
        return resources.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return resources.size();
    }
}
