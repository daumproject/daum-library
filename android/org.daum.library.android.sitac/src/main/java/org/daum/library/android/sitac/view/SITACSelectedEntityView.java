package org.daum.library.android.sitac.view;

import android.util.Log;
import android.widget.*;
import org.daum.library.android.sitac.engine.IUndoRedoEngine;
import org.daum.library.android.sitac.engine.UndoRedoEngine;
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
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.entity.IShapedEntity;

import java.util.Observable;
import java.util.Observer;

public class SITACSelectedEntityView extends LinearLayout implements Observer {
	
	private static final String TAG = SITACSelectedEntityView.class.getSimpleName();

    public enum State {
		SELECTION,
		CONFIRM,
		DELETION
	}
	
	private static final String TEXT_DELETE = "Supprimer";
	private static final String TEXT_TITLE = "Sélection:";
	private static final String TEXT_CONFIRM = "Terminer";
    private static final String TEXT_SELECT_ENT_WHEN_DRAWING = "Impossible de sélectionner une autre entité pendant un tracé. Veuillez terminer l'action en cours.";
	
	private Context ctx;
    private IEntity entity;
	private String msg;
	private Drawable icon;
	private ImageView iconView, undoView, redoView;
	private TextView msgView;
	private LinearLayout undoRedoLayout;
	private Button actionBtn;
	private State state;
	private OnSelectedEntityEventListener listener;
	

	public SITACSelectedEntityView(Context context) {
		super(context);
		this.ctx = context;
		this.state = State.SELECTION;
		initUI();
		configUI();
		updateUI();
		defineCallbacks();
	}

	/**
	 * Init views that will be used to update UI
	 */
	private void initUI() {
		iconView = new ImageView(ctx);
		msgView = new TextView(ctx);
		undoRedoLayout = new LinearLayout(ctx);
		actionBtn = new Button(ctx);
		undoView = new ImageView(ctx);
		redoView = new ImageView(ctx);
	}
	
	/**
	 * Config UI just one time
	 */
	private void configUI() {
		setOrientation(LinearLayout.VERTICAL);
		setPadding(8, 8, 8, 8);
		
		TextView titleView = new TextView(ctx);
		titleView.setText(TEXT_TITLE);
        titleView.setTextColor(Color.LTGRAY);
		addView(titleView);
		
		LinearLayout subLayout = new LinearLayout(ctx);
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

		actionBtn.setText(TEXT_DELETE);
		actionBtn.setTextColor(Color.WHITE);
		addView(actionBtn, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		undoRedoLayout.setOrientation(LinearLayout.HORIZONTAL);
		undoView = new ImageView(ctx);
		redoView = new ImageView(ctx);
		undoView.setImageDrawable(DrawableFactory.build(ctx, DrawableFactory.ICON_UNDO));
		redoView.setImageDrawable(DrawableFactory.build(ctx, DrawableFactory.ICON_REDO));
		undoView.setClickable(true);
		redoView.setClickable(true);
		LayoutParams undoRedoParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		undoRedoParams.weight = 1;
		undoRedoLayout.addView(undoView, undoRedoParams);
		undoRedoLayout.addView(redoView, undoRedoParams);
		addView(undoRedoLayout);
		
	}
	
	/**
	 * Update UI with new data
	 */
	private void updateUI() {
		iconView.setImageDrawable(icon);
		msgView.setText(msg);
		
		switch (state) {
			case SELECTION:
				actionBtn.setVisibility(View.GONE);
				undoRedoLayout.setVisibility(View.GONE);
				break;
			
			case DELETION:
				actionBtn.setText(TEXT_DELETE);
				actionBtn.setVisibility(View.VISIBLE);
				undoRedoLayout.setVisibility(View.GONE);
				break;
				
			case CONFIRM:
				actionBtn.setText(TEXT_CONFIRM);
				actionBtn.setVisibility(View.VISIBLE);
				undoRedoLayout.setVisibility(View.VISIBLE);
				break;
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
	
	/**
	 * Define callbacks for the views
	 */
	private void defineCallbacks() {
		actionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					switch (state) {
						case DELETION:
							listener.onDeleteButtonClicked();
                            hide();
							break;
	
						case CONFIRM:
							listener.onConfirmButtonClicked();
                            // putting selectedEntityView's state back to SELECTION
                            // to enable entity selection on map again
                            state = State.SELECTION;
                            hide();
							break;
					}
				}
			}
		});
				
		undoView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						undoView.setImageDrawable(DrawableFactory.build(ctx, DrawableFactory.ICON_UNDO_PRESSED));
						return true;
						
					case MotionEvent.ACTION_UP:
						undoView.setImageDrawable(DrawableFactory.build(ctx, DrawableFactory.ICON_UNDO));
						if (listener != null) listener.onUndoButtonClicked();
						return true;
				}
				return false;
			}
		});
		
		redoView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						redoView.setImageDrawable(DrawableFactory.build(ctx, DrawableFactory.ICON_REDO_PRESSED));
						return true;
						
					case MotionEvent.ACTION_UP:
						redoView.setImageDrawable(DrawableFactory.build(ctx, DrawableFactory.ICON_REDO));
						if (listener != null) listener.onRedoButtonClicked();
						return true;
				}
				return false;
			}
		});

		// prevent underlying views to get touchEvents
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// consume the event
				return true;
			}
		});
	}

    /**
     * Update the view content according to the entity given in parameter
     * if it can handle it in its current state
     * The returned IEntity will be the one currently managed by this view
     *
     * @param e an IEntity
     * @return IEntity that is currently managed by this view
     */
    public IEntity registerEntity(IEntity e) {
        switch (state) {
            case CONFIRM:
                Toast.makeText(ctx, TEXT_SELECT_ENT_WHEN_DRAWING, Toast.LENGTH_LONG).show();
                return this.entity;

            default:
                if (this.entity != null) {
                    this.entity.deleteObserver(this); // do not observe old entity anymore
                }
                this.entity = e; // register the new entity
                this.entity.addObserver(this); // observe the new entity
                this.icon = e.getIcon();
                this.msg = e.getType()+e.getMessage();
                switch (entity.getState()) {
                    case SAVED:
                        state = State.DELETION;
                        break;

                    case NEW:
                        state = State.SELECTION;
                        break;

                    case ON_MAP:
                        state = State.CONFIRM;
                        break;
                }
                show();
                return entity;
        }
    }
	
	public void hide() {
		setVisibility(View.GONE);
	}

	public void show() {
		updateUI();
		requestLayout();
		setVisibility(View.VISIBLE);
	}

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof IEntity) {
            IEntity e = (IEntity) arg;
            switch (e.getState()) {
                case SAVED:
                    hide();
                    break;

                case ON_MAP:
                    if (e instanceof IShapedEntity) {
                        IShapedEntity sEnt = (IShapedEntity) e;
                        if (sEnt.isPersistable()) {
                            actionBtn.setEnabled(true);
                            actionBtn.setTextColor(Color.WHITE);
                        } else {
                            actionBtn.setEnabled(false);
                            actionBtn.setTextColor(Color.GRAY);
                        }
                        state = State.CONFIRM;
                        show();
                    }
                    break;
            }

        } else if (arg instanceof IUndoRedoEngine) {
            IUndoRedoEngine engine = (IUndoRedoEngine) arg;
            if (engine.canRedo()) {
                redoView.setEnabled(true);
                redoView.setAlpha(255);
            }
            else {
                redoView.setEnabled(false);
                redoView.setAlpha(60);
            }

            if (engine.canUndo()) {
                undoView.setEnabled(true);
                undoView.setAlpha(255);
            }
            else {
                undoView.setEnabled(false);
                undoView.setAlpha(60);
            }

        } else {
            Log.w(TAG, "I don't know how to update myself with this object " + arg + ". Just know how with IEntity");
        }
    }
	
	public void setOnSelectedEntityEventListener(OnSelectedEntityEventListener listener) {
		this.listener = listener;
	}
}
