package org.daum.library.android.agetac.messages.view;

import android.R;
import android.content.Context;
import android.util.Log;
import android.widget.*;
import org.daum.library.android.agetac.messages.listener.MessagesEvent;
import org.daum.library.android.agetac.messages.listener.MessagesEvent.MessagesEventType;
import org.daum.common.message.api.SimpleMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public class SimpleMessagesView extends AbstractMessagesView {

    private static final String TAG = "SimpleMessagesView";
    private static final boolean D = true;

    private static final int ID_MESSAGE = 1;
    private static final String TEXT_MESSAGE = "Tapez votre message ici";

    private static final String KEY_GROUPE_H = "groupeHoraire";
    private static final String KEY_MESSAGE = "message";

    private static final String[] FROM = {KEY_GROUPE_H, KEY_MESSAGE};
    private static final int[] TO = {R.id.text1, R.id.text2};

    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    private ListView listView;
    private SimpleAdapter adapter;
    private EditText editText;

    public SimpleMessagesView(Context context) {
        super(context);
        initUI();
        configUI();
    }

    private void initUI() {
        listView = new ListView(ctx);
        adapter = new SimpleAdapter(ctx, data, R.layout.two_line_list_item, FROM, TO);
        editText = new EditText(ctx);
    }

    private void configUI() {
        listView.setAdapter(adapter);

        editText.setHint(TEXT_MESSAGE);
        editText.setId(ID_MESSAGE);

        RelativeLayout.LayoutParams etParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        etParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        addViewToLeftLayout(editText, etParams);

        RelativeLayout.LayoutParams listParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        listParams.addRule(RelativeLayout.BELOW, editText.getId());

        addViewToLeftLayout(listView, listParams);
    }

    @Override
    protected void onSendButtonClicked() {
        if (D) Log.i(TAG, "onSendButtonClicked()");
        String text = editText.getText().toString().trim();
        if (!text.isEmpty()) {
            SimpleMessage msg = new SimpleMessage(text);
            fireEventToListener(new MessagesEvent(msg, MessagesEventType.SEND));
            editText.setText("");
        }
    }

    public void addMessage(SimpleMessage msg) {
        if (D) Log.i(TAG, "addMessage(SimpleMessage msg)");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(KEY_GROUPE_H, msg.groupeHoraire);
        map.put(KEY_MESSAGE, msg.text);
        data.add(map);
        adapter.notifyDataSetChanged();
    }

}
