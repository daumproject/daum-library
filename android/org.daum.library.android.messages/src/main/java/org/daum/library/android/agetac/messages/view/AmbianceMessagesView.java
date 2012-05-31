package org.daum.library.android.agetac.messages.view;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import org.daum.common.message.api.AmbianceMessage;
import org.daum.library.android.agetac.messages.listener.MessagesEvent;
import org.daum.library.android.agetac.messages.listener.MessagesEvent.MessagesEventType;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 30/05/12
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */
public class AmbianceMessagesView extends AbstractMessagesView {

    private static final int ID_MESSAGES_VIEW = 1;

    private static final String JE_SUIS = "Je suis";
    private static final String JE_VOIS = "Je vois";
    private static final String JE_PROCEDE = "Je procède";
    private static final String JE_PREVOIS = "Je prévois";
    private static final String JE_DEMANDE = "Je demande";

    private EditText et_jeSuis,
                     et_jeVois,
                     et_jePrevois,
                     et_jeProcede,
                     et_jeDemande;

    public AmbianceMessagesView(Context context) {
        super(context);
        initUI();
        configUI();
    }

    private void initUI() {
        // EditText
        et_jeDemande = new EditText(ctx);
        et_jePrevois = new EditText(ctx);
        et_jeProcede = new EditText(ctx);
        et_jeSuis = new EditText(ctx);
        et_jeVois = new EditText(ctx);
    }

    private void configUI() {
        // EditText
        et_jeDemande.setHint(JE_DEMANDE);
        et_jePrevois.setHint(JE_PREVOIS);
        et_jeProcede.setHint(JE_PROCEDE);
        et_jeSuis.setHint(JE_SUIS);
        et_jeVois.setHint(JE_VOIS);

        LinearLayout.LayoutParams etLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        etLayoutParams.weight = 1;

        LinearLayout ambianceLayout = new LinearLayout(ctx);
        ambianceLayout.setOrientation(LinearLayout.VERTICAL);
        // layout params for "message d'ambiance"
        RelativeLayout.LayoutParams ambianceParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ambianceLayout.addView(et_jeSuis, etLayoutParams);
        ambianceLayout.addView(et_jeVois, etLayoutParams);
        ambianceLayout.addView(et_jePrevois, etLayoutParams);
        ambianceLayout.addView(et_jeProcede, etLayoutParams);
        ambianceLayout.addView(et_jeDemande, etLayoutParams);

        // add the linearLayout view to the leftLayout of AbstractMessagesView
        addViewToLeftLayout(ambianceLayout, ambianceParams);
    }

    @Override
    protected void onSendButtonClicked() {
        AmbianceMessage msg = new AmbianceMessage(processText(et_jeSuis),
                processText(et_jeVois), processText(et_jePrevois),
                processText(et_jeProcede), processText(et_jeDemande));

        fireEventToListener(new MessagesEvent(msg,
                MessagesEventType.SEND));
    }

    public void setMessage(AmbianceMessage msg) {
        et_jeDemande.setText(msg.jeDemande);
        et_jePrevois.setText(msg.jePrevois);
        et_jeProcede.setText(msg.jeProcede);
        et_jeSuis.setText(msg.jeSuis);
        et_jeVois.setText(msg.jeVois);
    }

    private String processText(EditText te) {
        return te.getText().toString().trim();
    }
}
