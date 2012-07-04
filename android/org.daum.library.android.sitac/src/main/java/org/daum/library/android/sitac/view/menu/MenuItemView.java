package org.daum.library.android.sitac.view.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuItemView extends LinearLayout {

	// debugging
	private static final String TAG = "MenuItemView";
	
	private Context ctx;
	private IExpandableMenuItem menuItem;
	private ImageView iconView;
	private TextView textView;
	
	public MenuItemView(Context context, IExpandableMenuItem menuItem) {
		super(context);
		this.ctx = context;
		this.menuItem = menuItem;
		initUI();
		configUI();
	}
	
	private void initUI() {
		iconView = new ImageView(ctx);
		textView = new TextView(ctx);
	}
	
	private void configUI() {
		setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setPadding(8, 8, 8, 8);
		
		// configuring the iconView
		if (menuItem.getIcon() != null) {
			iconView.setImageDrawable(menuItem.getIcon());
			iconView.setPadding(10, 0, 0, 0);
			LayoutParams icParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			addView(iconView, icParams);
		} else {
			iconView = null;
		}
		
		// configuring the textView
		textView.setText(menuItem.getText());
		textView.setTextSize(20);
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		textView.setMaxLines(1);
		textView.setEllipsize(TextUtils.TruncateAt.END);
		// FIXME change the hardcoded value 36 with android:attr/expandableListPreferredItemPaddingLeft
		textView.setPadding(iconView==null?36:10, 0, 0, 0);
		LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		addView(textView, tvParams);
	}
	
	public void setMenuItem(IExpandableMenuItem item) {
		this.menuItem = item;
		removeAllViews();
		configUI();
	}

	public ImageView getIcon() {
		return iconView;
	}

	public TextView getText() {
		return textView;
	}
}
