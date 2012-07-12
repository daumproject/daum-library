package org.daum.library.android.messages.view;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.*;
//import org.daum.common.message.api.Message;
import org.daum.library.android.messages.view.ListItemView.MessageType;
import org.sitac.MessageAmbiance;

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

    // String constants
    private static final String TEXT_GO_UP = "Remonter";
    private static final String TEXT_GO_DOWN = "Descendre";

    private ArrayList<Pair<MessageType, MessageAmbiance>> messages = new ArrayList<Pair<MessageType, MessageAmbiance>>();
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
     * Adds a message to the ListView if it has not been already added
     * @param msg the message to add
     * @param type either MessageType.IN (received) or MessageType.OUT (emitted)
     */
    public void addMessage(MessageAmbiance msg, MessageType type) {
        for (Pair<MessageType, MessageAmbiance> pair : messages) {
            if (pair.second.getId().equals(msg.getId())) {
                // message is already in the list, do not add it
                return;
            }
        }
        // the message wasn't in the list, so add it
        messages.add(new Pair<MessageType, MessageAmbiance>(type, msg));

        adapter.notifyDataSetChanged();

        // make the list go all the way down to the last message
        listView.setSelection(messages.size() - 1);
    }
}
