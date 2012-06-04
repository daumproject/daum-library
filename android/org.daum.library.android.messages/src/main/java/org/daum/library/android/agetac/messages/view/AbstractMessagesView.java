package org.daum.library.android.agetac.messages.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import org.daum.library.android.agetac.messages.listener.IMessagesListener;
import org.daum.library.android.agetac.messages.listener.MessagesEvent;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractMessagesView extends RelativeLayout {

    // View ids
    private static final int ID_RIGHT_LAYOUT = 1;

    protected Context ctx;
    protected IMessagesListener listener;

    private RelativeLayout leftLayout;
    private LinearLayout rightLayout;

    public AbstractMessagesView(Context ctx) {
        super(ctx);
        this.ctx = ctx;
        initUI();
        configUI();
    }

    private void initUI() {
        leftLayout = new RelativeLayout(ctx);
        rightLayout = new LinearLayout(ctx);
    }

    private void configUI() {
        // this component layout params
        setLayoutParams(new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // hack to avoid keyboard to pop when messagesView is displayed
        setFocusableInTouchMode(true);
        setFocusable(true);

        // configuring right layout
        rightLayout.setId(ID_RIGHT_LAYOUT);
        rightLayout.setOrientation(LinearLayout.VERTICAL);

        // layout params for right layout
        RelativeLayout.LayoutParams rightLayoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        // layout params for left layout
        RelativeLayout.LayoutParams leftLayoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        leftLayoutParams.addRule(RelativeLayout.LEFT_OF, rightLayout.getId());


        // add leftLayout to NewMessageView layout
        addView(leftLayout, leftLayoutParams);

        // add rightLayout to NewMessageView layout
        addView(rightLayout, rightLayoutParams);
    }

    protected void addViewToLeftLayout(View view, LayoutParams params) {
        leftLayout.addView(view, params);
        requestLayout();
    }

    protected void addViewToRightLayout(View view, LinearLayout.LayoutParams params) {
        rightLayout.addView(view, params);
        requestLayout();
    }

    public void addEventListener(IMessagesListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }
}
