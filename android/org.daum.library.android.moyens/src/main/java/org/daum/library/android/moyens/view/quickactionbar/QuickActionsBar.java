package org.daum.library.android.moyens.view.quickactionbar;

import java.util.ArrayList;
import java.util.Hashtable;

import android.util.Log;
import android.view.*;
import android.widget.*;
import org.daum.library.android.moyens.view.quickactionbar.listener.OnActionClickedListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.Pair;
import android.widget.AdapterView.OnItemClickListener;

public class QuickActionsBar extends TabHost implements View.OnClickListener {
	
	private static final String TAG = "QuickActionsBar";
	
	private Context ctx;
	private ArrayList<Pair<String, ArrayList<String>>> actions;
    private OnActionClickedListener listener;
	
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
                            LinearLayout viewContainer = new LinearLayout(ctx);
                            viewContainer.setGravity(Gravity.CENTER_HORIZONTAL);

                            HorizontalScrollView actionsView = new HorizontalScrollView(ctx);
                            actionsView.setLayoutParams(new AbsListView.LayoutParams(
                                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            actionsView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);

                            LinearLayout actionsList = new LinearLayout(ctx);
                            actionsList.setOrientation(LinearLayout.HORIZONTAL);
                            actionsList.setLayoutParams(new LinearLayout.LayoutParams(
                                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                            Button b;
                            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
                            );

                            for (int i=0; i< pair.second.size(); i++) {
                                b = new Button(ctx);
                                b.setId(i);
                                b.setText(pair.second.get(i));
                                b.setOnClickListener(QuickActionsBar.this);
                                b.setLayoutParams(btnParams);
                                actionsList.addView(b, b.getLayoutParams());
                            }

                            actionsView.addView(actionsList, actionsList.getLayoutParams());
                            viewContainer.addView(actionsView, actionsView.getLayoutParams());
                            return viewContainer;
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

        // adds a little right padding to the background
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

    public void setOnActionClickedListener(OnActionClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            String tab = actions.get(getCurrentTab()).first;
            String action = actions.get(getCurrentTab()).second.get(view.getId());
            listener.onActionClicked(tab, action);
        }
    }
}
