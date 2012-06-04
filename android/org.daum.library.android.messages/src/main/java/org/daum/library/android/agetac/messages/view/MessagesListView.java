package org.daum.library.android.agetac.messages.view;

import android.R;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.*;
import org.daum.common.message.api.Message;
import org.daum.library.android.agetac.messages.view.ListItemView.MessageType;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public class MessagesListView extends AbstractMessagesView {

    private static final String TAG = "MessagesListView";
    private static final boolean D = true;

    // String constants
    private static final String TEXT_GO_UP = "Remonter";
    private static final String TEXT_GO_DOWN = "Descendre";

    private ArrayList<Pair<MessageType, Message>> messages = new ArrayList<Pair<MessageType, Message>>();
    private ListView listView;
    private MessagesAdapter adapter;
    private Button btn_goUp, btn_goDown;

    public MessagesListView(Context context) {
        super(context);
        initUI();
        configUI();
        defineCallbacks();
    }

    private void initUI() {
        listView = new ListView(ctx);
        adapter = new MessagesAdapter(ctx, messages);
        btn_goUp = new Button(ctx);
        btn_goDown = new Button(ctx);
    }

    private void configUI() {
        listView.setAdapter(adapter);

        RelativeLayout.LayoutParams listParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        addViewToLeftLayout(listView, listParams);

        btn_goUp.setText(TEXT_GO_UP);
        btn_goDown.setText(TEXT_GO_DOWN);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        addViewToRightLayout(btn_goUp, btnParams);
        addViewToRightLayout(btn_goDown, btnParams);
    }

    private void defineCallbacks() {
        btn_goUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!messages.isEmpty()) listView.setSelection(0);
            }
        });

        btn_goDown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!messages.isEmpty()) listView.setSelection(messages.size()-1);
            }
        });
    }


    /**
     * Adds a message to the ListView
     * @param msg the message to add
     * @param type either MessageType.IN (received) or MessageType.OUT (emitted)
     */
    public void addMessage(Message msg, MessageType type) {
        if (D) Log.i(TAG, "addMessage(SimpleMessage msg, MessageType type)");
        messages.add(new Pair<MessageType, Message>(type, msg));
        adapter.notifyDataSetChanged();
        listView.setSelection(messages.size() - 1);
    }

}
