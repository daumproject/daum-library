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

    // String constants
    private static final String TEXT_SEND = "Envoyer";

    // View ids
    private static final int ID_SEND_BTN = 1;

    protected Context ctx;
    protected IMessagesListener listener;

    private Button btn_send;
    private RelativeLayout leftLayout;

    public AbstractMessagesView(Context ctx) {
        super(ctx);
        this.ctx = ctx;
        initUI();
        configUI();
        defineCallbacks();
    }

    private void initUI() {
        btn_send = new Button(ctx);
        leftLayout = new RelativeLayout(ctx);
    }

    private void configUI() {
        // this component layout params
        setLayoutParams(new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // hack to avoid keyboard to pop when messagesView is displayed
        setFocusableInTouchMode(true);
        setFocusable(true);

        // buttons
        btn_send.setId(ID_SEND_BTN);
        btn_send.setText(TEXT_SEND);
        // layout params for send button
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        btnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        // layout params for left layout
        RelativeLayout.LayoutParams leftLayoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        leftLayoutParams.addRule(RelativeLayout.LEFT_OF, btn_send.getId());


        // add ambianceLayout to AmbianceMessagesView layout
        addView(leftLayout, leftLayoutParams);

        // add btn_send to AmbianceMessagesView layout
        addView(btn_send, btnParams);
    }

    private void defineCallbacks() {
        btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendButtonClicked();
            }
        });
    }

    protected void addViewToLeftLayout(View view, LayoutParams params) {
        leftLayout.addView(view, params);
        requestLayout();
    }

    protected void addViewToRightLayout(View view, LinearLayout.LayoutParams params) {
        //rightLayout.addView(view, params);
        requestLayout();
    }

    protected void fireEventToListener(MessagesEvent evt) {
        if (listener != null) {
            switch (evt.getType()) {
                case SEND:
                    listener.onSend(evt.getContent());
                    break;
            }
        }
    }

    public void addEventListener(IMessagesListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    protected abstract void onSendButtonClicked();
}
