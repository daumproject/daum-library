package org.daum.library.android.agetac.messages.view;

import android.R;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 09:41
 * To change this template use File | Settings | File Templates.
 */
public class MyTabHost extends TabHost {

    private Context ctx;

    public MyTabHost(Context context) {
        this(context, null);
    }

    public MyTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        configUI();
    }

    private void configUI() {
        LinearLayout linLayout = new LinearLayout(ctx);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        linLayout.setPadding(0, 5, 0, 0);

        TabWidget tabWidget = new TabWidget(ctx);
        tabWidget.setId(R.id.tabs);

        FrameLayout frameLayout = new FrameLayout(ctx);
        frameLayout.setId(R.id.tabcontent);
        frameLayout.setPadding(0, 5, 0, 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        linLayout.addView(tabWidget, params);
        linLayout.addView(frameLayout, params);

        addView(linLayout, params);
    }
}
