package org.daum.library.android.daumauth.view;

import android.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 09/07/12
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class InterventionListView extends RelativeLayout {

    private Context ctx;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private OnItemSelectedListener listener;

    public InterventionListView(Context context) {
        super(context, null);
        this.ctx = context;
        items = new ArrayList<String>();

        initUI();
        configUI();
        defineCallbacks();
    }

    private void initUI() {
        listView = new ListView(ctx);
        adapter = new ArrayAdapter<String>(ctx, R.layout.simple_list_item_1, items);
    }

    private void configUI() {
        setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // configuring listView
        listView.setAdapter(adapter);

        addView(listView);
    }

    private void defineCallbacks() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (listener != null) listener.onItemSelected(position);
            }
        });
    }

    /**
     * This method must be called on UI Thread
     * @param items
     */
    public void addItems(ArrayList<String> items) {
        items.addAll(items);
        adapter.notifyDataSetChanged();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
