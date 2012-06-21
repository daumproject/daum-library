package org.daum.library.android.sitac.view;

import org.daum.library.android.sitac.listener.OnSelectedEntityEventListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SITACSelectedEntityView extends LinearLayout {
	
	private static final String TAG = SITACSelectedEntityView.class.getSimpleName();
	
	public enum State {
		SELECTION,
		CONFIRM,
		DELETION
	}
	
	private static final String TEXT_DELETE = "Supprimer";
	private static final String TEXT_TITLE = "Sélection:";
	private static final String TEXT_CONFIRM = "Terminer";
	
	private Context ctx;
	private String msg;
	private Drawable icon;
	private TextView titleView;
	private ImageView iconView;
	private TextView msgView;
	private LinearLayout subLayout;
	private Button actionBtn;
	private State state;
	private OnSelectedEntityEventListener listener;
	

	public SITACSelectedEntityView(Context context) {
		super(context);
		this.ctx = context;
		this.state = State.SELECTION;
		initUI();
	}

	private void initUI() {
		titleView = new TextView(ctx);
		iconView = new ImageView(ctx);
		msgView = new TextView(ctx);
		subLayout = new LinearLayout(ctx);
		actionBtn = new Button(ctx);
	}
	
	private void configUI() {
		setOrientation(LinearLayout.VERTICAL);
		setPadding(8, 8, 8, 8);
		
		titleView.setText(TEXT_TITLE);
        titleView.setTextColor(Color.LTGRAY);
		addView(titleView);
		
		subLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams subLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		// configuring the iconView
		iconView.setImageDrawable(icon);
		LayoutParams icParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		icParams.gravity = Gravity.CENTER_VERTICAL;
		
		// configuring the msgView
		msgView.setText(msg);
		msgView.setPadding(10, 0, 0, 0);
		msgView.setTextSize(20);
		msgView.setTextColor(Color.WHITE);
		msgView.setGravity(Gravity.CENTER_VERTICAL);
		msgView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		msgView.setMaxLines(1);
		LayoutParams tvParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		subLayout.addView(iconView, icParams);
		subLayout.addView(msgView, tvParams);
		
		addView(subLayout, subLayoutParams);
		

		
		if (state != State.SELECTION) {
			switch (state) {
				case DELETION:
					actionBtn.setText(TEXT_DELETE);
					break;
					
				case CONFIRM:
					actionBtn.setText(TEXT_CONFIRM);
					break;
			}
			LayoutParams btnParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	        actionBtn.setTextColor(Color.WHITE);
			addView(actionBtn, btnParams);
		}
		
		measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		final Rect r = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
		ShapeDrawable roundedBackground = new ShapeDrawable(new Shape() {
			@Override
			public void draw(Canvas canvas, Paint paint) {
				paint.setColor(Color.argb(180, 0, 0, 0));
				paint.setStyle(Style.FILL);
				paint.setPathEffect(new CornerPathEffect(7));
				canvas.drawRect(r, paint);
			}
		});
		setBackgroundDrawable(roundedBackground);
	}
	
	private void defineCallbacks() {
		if (state == State.DELETION || state == State.CONFIRM) {
			actionBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (listener != null) listener.onSelectedEntityButtonClicked();
				}
			});
		
		}
		
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// return true to prevent the underlying views to get touchEvents
				return true;
			}
		});
	}
	
	public void updateView(Drawable icon, String message) {
		this.icon = icon;
		this.msg = message;
	}
	
	public void hide() {
		setVisibility(View.GONE);
	}

	public void show() {
		subLayout.removeAllViews();
		removeAllViews();
		configUI();
		defineCallbacks();
		requestLayout();
		setVisibility(View.VISIBLE);
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}
	
	public void setOnSelectedEntityEventListener(OnSelectedEntityEventListener listener) {
		this.listener = listener;
	}
}
