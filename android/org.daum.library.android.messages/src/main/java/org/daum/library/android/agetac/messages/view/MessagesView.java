package org.daum.library.android.agetac.messages.view;

import android.R;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.daum.common.message.api.Message;
import org.daum.library.android.agetac.messages.listener.IMessagesListener;
import org.daum.library.android.agetac.messages.view.ListItemView.MessageType;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 09:41
 * To change this template use File | Settings | File Templates.
 */
public class MessagesView extends TabHost implements IMessagesListener, TabHost.OnTabChangeListener {

    private static final String TAG = "MessagesView";

    private static final String TAB_NEW_MSG     = "Saisi d'un nouveau message";
    private static final String TAB_MSG_LIST    = "Historique des messages";
    private static final String TEXT_NEW_MSG    = "Nouveau message reçu";

    private enum ViewState {
        DISPLAYING_LIST,
        DISPLAYING_NEW_MSG
    }

    private Context ctx;
    private String senderName;
    private IMessagesListener listener;
    private NewMessageView newMsgView;
    private MessagesListView msgListView;
    private LinearLayout newMsgTabLayout, msgListTabLayout;
    private ViewState state;

    public MessagesView(Context context, String senderName) {
        super(context, null); // http://stackoverflow.com/questions/5408452/honeycomb-and-tabhost-specs
        this.ctx = context;
        this.senderName = senderName;
        initUI();
        configUI();

        initTabs();
        configTabs();
    }

    private void initUI() {
        newMsgView = new NewMessageView(ctx, senderName);
        msgListView = new MessagesListView(ctx);
    }

    private void configUI() {
        newMsgView.addEventListener(this);

        LinearLayout linLayout = new LinearLayout(ctx);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        linLayout.setPadding(0, 5, 0, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        TabWidget tabWidget = new TabWidget(ctx);
        tabWidget.setId(R.id.tabs);

        FrameLayout frameLayout = new FrameLayout(ctx);
        frameLayout.setId(R.id.tabcontent);
        frameLayout.setPadding(0, 5, 0, 0);

        LinearLayout.LayoutParams tabParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams frameParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        linLayout.addView(tabWidget, tabParams);
        linLayout.addView(frameLayout, frameParams);

        addView(linLayout, layoutParams);
    }

    private void initTabs() {
        LocalActivityManager lam = new LocalActivityManager((Activity) ctx, false);
        setup(lam);
        TabHost.TabSpec spec;

        spec = newTabSpec(TAB_NEW_MSG).setIndicator(TAB_NEW_MSG).setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                return newMsgView;
            }
        });
        addTab(spec);
        spec = newTabSpec(TAB_MSG_LIST).setIndicator(TAB_MSG_LIST).setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                return msgListView;
            }
        });
        addTab(spec);

        // register a listener when tabs change
        setOnTabChangedListener(this);
    }

    private void configTabs() {
        TabWidget tw = getTabWidget();
        tw.setStripEnabled(true);

        updateTabsUI();
    }

    public void addEventListener(IMessagesListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    /**
     * You should call onTabChanged(String) on your MessagesView instance
     * if you define another listener otherwise tabs won't update their UI
     * when changed
     * @param l
     */
    @Override
    public void setOnTabChangedListener(OnTabChangeListener l) {
        super.setOnTabChangedListener(l);
    }

    /**
     * Adds a message to the MessagesListView tab
     * @param msg the message to add
     * @param type either MessageType.IN (received) or MessageType.OUT (emitted)
     */
    public void addMessage(Message msg, MessageType type) {
        msgListView.addMessage(msg, type);

        if (state == ViewState.DISPLAYING_NEW_MSG && type == MessageType.IN) {
            Toast.makeText(ctx, TEXT_NEW_MSG, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTabChanged(String tabID) {
        if (tabID.equals(TAB_MSG_LIST)) {
            state = ViewState.DISPLAYING_LIST;
        } else {
            state = ViewState.DISPLAYING_NEW_MSG;
        }

        // update tabs UI
        updateTabsUI();
    }

    @Override
    public void onSend(Message msg) {
        if (listener != null) listener.onSend(msg);
    }

    private void changeTabUIToSelected(final LinearLayout layout) {
        layout.setBackgroundColor(Color.WHITE);
        TextView tv = (TextView) layout.findViewById(R.id.title);
        tv.setTextColor(Color.BLACK);

    }

    private void changeTabUIToUnselected(final LinearLayout layout) {
        layout.setBackgroundColor(Color.GRAY);
        TextView tv = (TextView) layout.findViewById(R.id.title);
        tv.setTextColor(Color.WHITE);
    }

    private void updateTabsUI() {
        TabWidget tw = getTabWidget();
        for (int i=0; i< tw.getTabCount(); i++) {
            LinearLayout layout = (LinearLayout) tw.getChildTabViewAt(i);
            if (i == getCurrentTab()) changeTabUIToSelected(layout);
            else changeTabUIToUnselected(layout);
        }
    }
}
