package org.daum.library.android.launcher.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/07/12
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public class LauncherView extends RelativeLayout {

    private static final String TAG = "LauncherView";

    private static final String TEXT_MATRICULE = "Matricule";
    private static final String TEXT_PASSWORD = "Mot de passe";
    private static final String TEXT_CONNECTION = "Connexion";

    private Context ctx;
    private EditText et_matricule;
    private EditText et_password;
    private Button btn_connect;

    public LauncherView(Context context) {
        super(context);
        this.ctx = context;
        initUI();
        configUI();
        defineCallbacks();
    }

    private void initUI() {
        et_matricule = new EditText(ctx);
        et_password = new EditText(ctx);
        btn_connect = new Button(ctx);
    }

    private void configUI() {
        setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // creating a new filter for edittext
        InputFilter[] filters = new InputFilter[] {
                new InputFilter.LengthFilter(15)
        };

        // getting new min width in dips
        Resources r = getResources();
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 285, r.getDisplayMetrics());

        // configuring et_matricule
        LinearLayout.LayoutParams matriculeParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        et_matricule.setHint(TEXT_MATRICULE);
        et_matricule.setLines(1);
        et_matricule.setWidth(width);
        et_matricule.setFilters(filters);
        et_matricule.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS); // no spell checking

        // configuring et_password
        LinearLayout.LayoutParams passwordParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        et_password.setHint(TEXT_PASSWORD);
        et_password.setLines(1);
        et_password.setFilters(filters);
        et_password.setImeOptions(EditorInfo.IME_ACTION_GO);
        et_password.setInputType(
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // do not display characters

        // configuring btn_connect
        btn_connect.setText(TEXT_CONNECTION);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // configuring connectionLayout that contains every sub layouts
        LinearLayout connectionLayout = new LinearLayout(ctx);
        connectionLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams connectionParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        connectionParams.addRule(CENTER_IN_PARENT);

        // add views to their layouts
        connectionLayout.addView(et_matricule, matriculeParams);
        connectionLayout.addView(et_password, passwordParams);
        connectionLayout.addView(btn_connect, btnParams);
        addView(connectionLayout, connectionParams);
    }

    private void defineCallbacks() {
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    btn_connect.performClick();
                    InputMethodManager imm = (InputMethodManager) ctx.getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        btn_connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "button clicked");
            }
        });
    }
}
