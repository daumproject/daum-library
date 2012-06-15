package org.daum.library.android.sitac.view.menu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

public class PopupSideMenu {
	
	private static final String TAG = "PopupSideMenu";

	private Context ctx;
	private View anchor;
	private PopupWindow window;
	private ListView listView;
	private ArrayList<String> data;
	private ArrayAdapter<String> adapter;
	
	public PopupSideMenu(Context context, View anchor) {
		this.ctx = context;
		this.anchor = anchor;
		initUI();
		configUI();
	}

	private void initUI() {
		listView = new ListView(ctx);
		data = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, android.R.id.text1, data);
		listView.setAdapter(adapter);
	}
	
	private void configUI() {
		listView.setBackgroundColor(Color.argb(180, 0, 0, 0));
		listView.setCacheColorHint(Color.argb(180, 0, 0, 0));

	}
	
	public void show() {
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(100, MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(300, MeasureSpec.AT_MOST);
		listView.measure(widthMeasureSpec, heightMeasureSpec);
		window = new PopupWindow(ctx);
		// when a touch even happens outside of the window
		// make the window go away
		window.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					window.dismiss();
					return true;
				}
				return false;
			}
		});
		window.setWidth(listView.getMeasuredWidth());
        window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        BitmapDrawable bm = new BitmapDrawable();
        window.setBackgroundDrawable(bm);
        window.setTouchable(true);
        window.setFocusable(true);
        window.setOutsideTouchable(true);

        window.setContentView(listView);
        window.update();
        int[] anchorLocation = new int[] {0, 0};
        anchor.getLocationOnScreen(anchorLocation);
		window.showAtLocation(anchor, Gravity.NO_GRAVITY, anchor.getWidth()+3, anchorLocation[1]);
	}
	
	public void populateMenu(ArrayList<String> list) {
		if (list != null) {
			for (String s : list) data.add(s);
			adapter.notifyDataSetChanged();
		}
	}
	
	public void populateMenu(String[] list) {
		if (list != null) {
			for (String s : list) data.add(s);
			adapter.notifyDataSetChanged();
		}
	}
	
	public void setOnItemClickListener(OnItemClickListener listener) {
		listView.setOnItemClickListener(listener);
	}

	public void dismiss() {
		window.dismiss();
	}
}
