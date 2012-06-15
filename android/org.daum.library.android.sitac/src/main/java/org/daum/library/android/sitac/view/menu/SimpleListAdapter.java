package org.daum.library.android.sitac.view.menu;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 15/06/12
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class SimpleListAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<String> data;

    public SimpleListAdapter(Context ctx, ArrayList<String> data) {
        this.ctx = ctx;
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // create a new view
            convertView = new TextView(ctx);
            convertView.setPadding(8, 8, 0, 8);
            ((TextView) convertView).setTextColor(Color.WHITE);
            ((TextView) convertView).setTextSize(17);
            ((TextView) convertView).setText(data.get(pos));

        } else {
            ((TextView) convertView).setText(data.get(pos));
        }
        return convertView;
    }

}
