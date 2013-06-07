package org.daum.library.nfcdemo;

import android.R;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.android.framework.service.events.IntentListener;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 27/05/13
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "Android")
@ComponentType
public class NfcDemo extends AbstractComponentType {

    private NfcAdapter adapter;
    private Tag tag;
    private  IntentListener intentListener;
    private LinearLayout mLayout;
    private  Context ctx=null;
    @Start
    public void start()
    {
       ctx = UIServiceHandler.getUIService().getRootActivity();

        mLayout = new LinearLayout(ctx);

        TextView headerGenericInfo = new TextView(UIServiceHandler.getUIService().getRootActivity());

        headerGenericInfo.setGravity(Gravity.CENTER_HORIZONTAL);
        final float scale = ctx.getResources().getDisplayMetrics().density;
        int pad = (int) (5 * scale + 0.5f); // 5dp to px.
        headerGenericInfo.setPadding(pad, pad, pad, pad);
        mLayout.addView(headerGenericInfo);
        final TextView genericInfo = new TextView(ctx);
        genericInfo.setPadding(pad, pad, pad, pad);
        genericInfo.setTextAppearance(ctx,android.R.style.TextAppearance_Medium);
        mLayout.addView(genericInfo);

        adapter = NfcAdapter.getDefaultAdapter(UIServiceHandler.getUIService().getRootActivity());
        intentListener =   new IntentListener() {
            @Override
            public void onNewIntent(Intent intent) {
                if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
                    tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


                    int uidLen = tag.getId().length;

                    String uid = Common.byte2HexString(tag.getId());
                    uid += " (" + uidLen + " byte";
                    if (uidLen == 7) {
                        uid += ", CL2";
                    } else if (uidLen == 10) {
                        uid += ", CL3";
                    }
                    uid += ")";

                    Toast.makeText(UIServiceHandler.getUIService().getRootActivity(), uid, Toast.LENGTH_LONG).show();

                    int hc = ctx.getResources().getColor(R.color.holo_red_light);
                    genericInfo.setText(uid);

                }
            }
        };

        UIServiceHandler.getUIService().addIntentListener(intentListener);
         UIServiceHandler.getUIService().addToGroup("NFC Demo",mLayout);
    }



    @Stop
    public void stop() {
        UIServiceHandler.getUIService().removeIntentListener(intentListener);
    }

    @Update
    public void update() {

    }
}
