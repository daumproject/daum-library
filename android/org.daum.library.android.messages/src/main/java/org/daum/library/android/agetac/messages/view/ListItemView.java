package org.daum.library.android.agetac.messages.view;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import org.daum.common.message.api.Message;
import android.content.Context;
import android.widget.LinearLayout;
import org.daum.library.android.agetac.messages.BitmapHolder;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 01/06/12
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class ListItemView extends LinearLayout {

    public enum MessageType {
        IN, // messages received from one another
        OUT // messages emitted by the user
    }

    private Context ctx;
    private Message msg;
    private MessageType type;
    private ImageView ioIcon;
    private TextView tv_header, tv_message;

    public ListItemView(Context context, Message message, MessageType type) {
        super(context);
        this.ctx = context;
        this.msg = message;
        this.type = type;
        initUI();
        configUI();
    }

    private void initUI() {
        ioIcon = new ImageView(ctx);
        tv_header = new TextView(ctx);
        tv_message = new TextView(ctx);
    }

    private void configUI() {
        // configuring this view layout
        setOrientation(LinearLayout.HORIZONTAL);

        // defining witch image to create
        switch (type) {
            case IN:
                ioIcon.setImageBitmap(BitmapHolder.getInstance(ctx).getInBitmap());
                break;

            case OUT:
                ioIcon.setImageBitmap(BitmapHolder.getInstance(ctx).getOutBitmap());
                break;
        }

        addView(ioIcon);

        LinearLayout textLayout = new LinearLayout(ctx);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(10, 0, 0, 0);

        tv_header.setText(msg.groupeHoraire+" - "+msg.sender);
        tv_header.setTextSize(25f);
        tv_header.setTextColor(Color.RED);

        tv_message.setText(msg.getText());
        tv_message.setTextSize(18f);

        textLayout.addView(tv_header);
        textLayout.addView(tv_message);

        addView(textLayout);

    }
}