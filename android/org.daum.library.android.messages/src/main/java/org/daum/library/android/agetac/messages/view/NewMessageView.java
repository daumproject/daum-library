package org.daum.library.android.agetac.messages.view;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import org.daum.common.message.api.Message;
import org.daum.library.android.agetac.messages.listener.MessagesEvent;
import org.daum.library.android.agetac.messages.listener.MessagesEvent.MessagesEventType;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 30/05/12
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */
public class NewMessageView extends AbstractMessagesView {

    private static final int ID_MESSAGES_VIEW = 1;
    private static final int ID_DEMANDE_ET = 2;
    private static final int ID_PREVOIS_ET = 3;
    private static final int ID_FAIS_ET = 4;
    private static final int ID_SUIS_ET = 5;
    private static final int ID_VOIS_ET = 6;
    private static final int ID_SEND_BTN = 7;

    // String constants
    private static final String TEXT_SEND   = "Envoyer";
    private static final String TEXT_CLEAR  = "Effacer";
    private static final String JE_SUIS     = "Je suis";
    private static final String JE_VOIS     = "Je vois";
    private static final String JE_FAIS  = "Je fais";
    private static final String JE_PREVOIS     = "Je prévois";
    private static final String JE_DEMANDE  = "Je demande";

    private EditText et_jeSuis,
                     et_jeVois,
                     et_jePrevois,
                     et_jeFais,
                     et_jeDemande;
    private Button btn_send, btn_clear;
    private String senderName;

    public NewMessageView(Context context, String senderName) {
        super(context);
        this.senderName = senderName;
        initUI();
        configUI();
        defineCallbacks();
    }

    private void initUI() {
        // EditText
        et_jeDemande = new EditText(ctx);
        et_jePrevois = new EditText(ctx);
        et_jeFais = new EditText(ctx);
        et_jeSuis = new EditText(ctx);
        et_jeVois = new EditText(ctx);

        // Buttons
        btn_send = new Button(ctx);
        btn_clear = new Button(ctx);
    }

    private void configUI() {
        // EditText
        et_jeDemande.setHint(JE_DEMANDE);
        et_jeDemande.setId(ID_DEMANDE_ET);
        et_jeDemande.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_jeDemande.setNextFocusDownId(et_jePrevois.getId());

        et_jePrevois.setHint(JE_PREVOIS);
        et_jePrevois.setId(ID_PREVOIS_ET);
        et_jePrevois.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_jePrevois.setNextFocusDownId(et_jeFais.getId());

        et_jeFais.setHint(JE_FAIS);
        et_jeFais.setId(ID_FAIS_ET);
        et_jeFais.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_jeFais.setNextFocusDownId(et_jeSuis.getId());

        et_jeSuis.setHint(JE_SUIS);
        et_jeSuis.setId(ID_SUIS_ET);
        et_jeSuis.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_jeSuis.setNextFocusDownId(et_jeVois.getId());

        et_jeVois.setHint(JE_VOIS);
        et_jeVois.setId(ID_VOIS_ET);
        et_jeVois.setImeOptions(EditorInfo.IME_ACTION_SEND);
        et_jeVois.setNextFocusDownId(btn_send.getId());

        LinearLayout.LayoutParams etLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        etLayoutParams.weight = 1;

        LinearLayout newMsgLayout = new LinearLayout(ctx);
        newMsgLayout.setOrientation(LinearLayout.VERTICAL);
        // layout params for "message d'ambiance"
        RelativeLayout.LayoutParams newMsgParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        newMsgLayout.addView(et_jeSuis, etLayoutParams);
        newMsgLayout.addView(et_jeVois, etLayoutParams);
        newMsgLayout.addView(et_jePrevois, etLayoutParams);
        newMsgLayout.addView(et_jeFais, etLayoutParams);
        newMsgLayout.addView(et_jeDemande, etLayoutParams);

        // add the linearLayout view to the leftLayout of AbstractMessagesView
        addViewToLeftLayout(newMsgLayout, newMsgParams);

        // configuring buttons
        btn_send.setId(ID_SEND_BTN);
        btn_send.setText(TEXT_SEND);
        btn_clear.setText(TEXT_CLEAR);

        // layout params for buttons
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addViewToRightLayout(btn_clear, btnParams);
        addViewToRightLayout(btn_send, btnParams);
    }

    private void defineCallbacks() {
        btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = processEditText();
                if (msg != null) {
                    fireEventToListener(new MessagesEvent(msg,
                            MessagesEventType.SEND));
                }
            }
        });

        btn_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                et_jeDemande.setText("");
                et_jeFais.setText("");
                et_jePrevois.setText("");
                et_jeSuis.setText("");
                et_jeVois.setText("");
            }
        });
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

    /**
     * Change the content of the EditText with the content
     * of the given Message
     * @param msg the message to display into the EditText
     */
    public void setMessage(Message msg) {
        et_jeDemande.setText(msg.jeDemande);
        et_jePrevois.setText(msg.jePrevois);
        et_jeFais.setText(msg.jeFais);
        et_jeSuis.setText(msg.jeSuis);
        et_jeVois.setText(msg.jeVois);
    }

    private Message processEditText() {
        Message msg = null;
        String jeSuis = et_jeSuis.getText().toString().trim();
        String jeVois = et_jeVois.getText().toString().trim();
        String jePrevois = et_jePrevois.getText().toString().trim();
        String jeFais = et_jeFais.getText().toString().trim();
        String jeDemande = et_jeDemande.getText().toString().trim();

        msg = new Message(senderName, jeSuis, jeVois, jePrevois, jeFais, jeDemande);
        if (!msg.isEmpty()) return msg;
        return null;
    }
}
