package org.daum.library.android.agetac.messages.view;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import org.daum.common.message.api.Message;
import org.daum.library.android.agetac.messages.view.ListItemView.MessageType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 01/06/12
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class MessagesAdapter extends BaseAdapter {

    private static final String TAG = "MessagesAdapter";
    private static boolean D = true;

    private Context ctx;
    private ArrayList<Pair<MessageType, Message>> messages;

    public MessagesAdapter(Context context, ArrayList<Pair<MessageType, Message>> messages) {
        super();
        this.ctx = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            ListItemView item = new ListItemView(ctx, messages.get(i).second, messages.get(i).first);
            return item;
        }
        if (view instanceof ListItemView) {
            ListItemView liv = (ListItemView) view;
            liv.updateData(messages.get(i));
        }
        return view;
    }
}
