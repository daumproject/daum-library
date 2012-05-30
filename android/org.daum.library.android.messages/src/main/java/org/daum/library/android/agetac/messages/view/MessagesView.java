package org.daum.library.android.agetac.messages.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import org.daum.library.android.agetac.messages.message.AmbianceMessage;
import org.daum.library.android.agetac.messages.listener.MessagesEvent;
import org.daum.library.android.agetac.messages.listener.MessagesEvent.MessagesEventType;
import org.daum.library.android.agetac.messages.message.SimpleMessage;
import org.daum.library.android.agetac.messages.listener.IMessagesListener;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 30/05/12
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */
public class MessagesView extends RelativeLayout {

    private static final int ID_SEND_BUTTON = 1;

    private Context ctx;
    private EditText 	et_jeSuis,
            et_jeVois,
            et_jePrevois,
            et_jeProcede,
            et_jeDemande;
    private Button btn_send;
    private IMessagesListener listener;

    public MessagesView(Context context) {
        super(context);
        this.ctx = context;
        initUI();
        configUI();
        defineCallbacks();
    }

    private void initUI() {
        // EditText
        et_jeDemande = new EditText(ctx);
        et_jePrevois = new EditText(ctx);
        et_jeProcede = new EditText(ctx);
        et_jeSuis = new EditText(ctx);
        et_jeVois = new EditText(ctx);

        // buttons
        btn_send = new Button(ctx);
    }

    private void configUI() {
        // this component layout params
        setLayoutParams(new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // hack to avoid keyboard to pop when messagesView is displayed
        setFocusableInTouchMode(true);
        setFocusable(true);

        // EditText
        et_jeDemande.setHint("Je demande");
        et_jePrevois.setHint("Je prévois");
        et_jeProcede.setHint("Je procède");
        et_jeSuis.setHint("Je suis");
        et_jeVois.setHint("Je vois");
        LinearLayout.LayoutParams etLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        etLayoutParams.weight = 1;

        // buttons
        btn_send.setId(ID_SEND_BUTTON);
        btn_send.setText("Envoyer");
        // layout params for send button
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        btnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        // "message d'ambiance" layout
        LinearLayout ambianceLayout = new LinearLayout(ctx);
        // layout params for "message d'ambiance"
        RelativeLayout.LayoutParams ambianceParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ambianceParams.addRule(RelativeLayout.LEFT_OF, btn_send.getId());
        ambianceLayout.setOrientation(LinearLayout.VERTICAL);
        ambianceLayout.addView(et_jeSuis, etLayoutParams);
        ambianceLayout.addView(et_jeVois, etLayoutParams);
        ambianceLayout.addView(et_jePrevois, etLayoutParams);
        ambianceLayout.addView(et_jeProcede, etLayoutParams);
        ambianceLayout.addView(et_jeDemande, etLayoutParams);


        // add ambianceLayout to MessagesView layout
        addView(ambianceLayout, ambianceParams);

        // add btn_send to MessagesView layout
        addView(btn_send, btnParams);
    }

    private void defineCallbacks() {
        btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbianceMessage msg = new AmbianceMessage(getText(et_jeSuis),
                        getText(et_jeVois), getText(et_jePrevois),
                        getText(et_jeProcede), getText(et_jeDemande));

                fireEventToListener(new MessagesEvent(msg,
                        MessagesEventType.SEND));
            }
        });
    }

    public void setAmbianceMessage(AmbianceMessage msg) {
        et_jeDemande.setText(msg.jeDemande);
        et_jePrevois.setText(msg.jePrevois);
        et_jeProcede.setText(msg.jeProcede);
        et_jeSuis.setText(msg.jeSuis);
        et_jeVois.setText(msg.jeVois);
    }

    public void setMessage(SimpleMessage msg) {
        // TODO le prendre en charge
    }

    private void fireEventToListener(MessagesEvent evt) {
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

    private String getText(EditText te) {
        return te.getText().toString().trim();
    }
}
