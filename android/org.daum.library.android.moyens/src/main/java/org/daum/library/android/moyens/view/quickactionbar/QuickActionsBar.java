package org.daum.library.android.moyens.view.quickactionbar;

import java.util.ArrayList;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class QuickActionsBar extends TabHost implements OnItemClickListener {
	
	private static final String TAG = "QuickActionsBar";
	
	private Context ctx;
	private ArrayList<Pair<String, ArrayList<String>>> actions;
	
	public QuickActionsBar(Context context, ArrayList<Pair<String, ArrayList<String>>> actions) {
		super(context, null);
		this.ctx = context;
		this.actions = actions;
		configUI();
		initTabs();
	}
	
	private void configUI() {
        LinearLayout linLayout = new LinearLayout(ctx);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        linLayout.setPadding(0, 5, 0, 0);

        TabWidget tabWidget = new TabWidget(ctx);
        tabWidget.setId(android.R.id.tabs);
        tabWidget.setStripEnabled(true);

        FrameLayout frameLayout = new FrameLayout(ctx);
        frameLayout.setId(android.R.id.tabcontent);
        frameLayout.setPadding(0, 5, 0, 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        		LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        linLayout.addView(tabWidget, params);
        linLayout.addView(frameLayout, params);

        addView(linLayout, params);
	}
	
	private void initTabs() {
        setup();
        TabHost.TabSpec spec;

        for (final Pair<String, ArrayList<String>> pair : actions) {
        	spec = newTabSpec(pair.first)
                    .setIndicator(pair.first)
                    .setContent(new TabHost.TabContentFactory() {
                        @Override
                        public View createTabContent(String tag) {
                            HorizontalListView actionsList = new HorizontalListView(ctx, null);
                            actionsList.setOnItemClickListener(QuickActionsBar.this);
                            ActionsAdapter adapter = new ActionsAdapter(ctx, pair.second);
                            actionsList.setAdapter(adapter);
                            return actionsList;
                        }
                    });
            addTab(spec);
        }
	}

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        updateTabsUI();
        super.onLayout(changed, left, top, right, bottom);
    }

    private void changeTabUIToSelected(final ViewGroup parent) {
    	parent.setBackgroundColor(Color.WHITE);
        TextView tv = (TextView) parent.findViewById(android.R.id.title);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(20);
    }

    private void changeTabUIToUnselected(final ViewGroup parent) {
        Rect tmp = new Rect();
        parent.getDrawingRect(tmp);
        final Rect r = tmp;
        
    	ShapeDrawable background = new ShapeDrawable(new Shape() {
			@Override
			public void draw(Canvas canvas, Paint paint) {
				paint.setColor(Color.GRAY);
				canvas.drawRect(r.left, r.top, r.right-3, r.bottom, paint);
			}
		});
    	parent.setBackgroundDrawable(background);
    	
        TextView tv = (TextView) parent.findViewById(android.R.id.title);
        tv.setTextColor(Color.LTGRAY);
        tv.setTextSize(20);
    }

    private void updateTabsUI() {
        TabWidget tw = getTabWidget();
        for (int i=0; i< tw.getTabCount(); i++) {
            ViewGroup vg = (ViewGroup) tw.getChildTabViewAt(i);
            if (i == getCurrentTab()) changeTabUIToSelected(vg);
            else changeTabUIToUnselected(vg);
        }
    }

	@Override
	public void onItemClick(AdapterView<?> listView, View v, int i, long l) {
		Log.d(TAG, "item clicked: "+v.toString());
	}
}
