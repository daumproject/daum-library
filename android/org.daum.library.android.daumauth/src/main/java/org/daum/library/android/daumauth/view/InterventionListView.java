package org.daum.library.android.daumauth.view;

import android.R;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.omg.IOP.TAG_ALTERNATE_IIOP_ADDRESS;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 09/07/12
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class InterventionListView extends RelativeLayout {

    private static final String TAG = "InterventionListView";
    private static final String TEXT_EMPTY_LIST = "Aucune intervention";

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
        RelativeLayout.LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        // configuring the empty list view
        TextView tv_emptylist = new TextView(ctx);
        tv_emptylist.setText(TEXT_EMPTY_LIST);
        tv_emptylist.setGravity(Gravity.CENTER);
        tv_emptylist.setTextSize(25);
        RelativeLayout.LayoutParams tvParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        listView.setEmptyView(tv_emptylist);

        addView(listView, params);
        addView(tv_emptylist, tvParams);
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
     * @param data
     */
    public void setListItems(ArrayList<String> data) {
        items.clear();
        items.addAll(data);
        adapter.notifyDataSetChanged();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
