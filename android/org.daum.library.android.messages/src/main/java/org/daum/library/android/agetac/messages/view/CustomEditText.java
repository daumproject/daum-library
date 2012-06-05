package org.daum.library.android.agetac.messages.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Selection;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 05/06/12
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */

public class CustomEditText extends EditText {

    private static final String TAG = "CustomEditText";

    private SpannableString immuableHint;

    public CustomEditText(Context context) {
        super(context);
    }

    public void setImmuableHint(String str) {
        immuableHint = new SpannableString(str+" ");
        immuableHint.setSpan(new ForegroundColorSpan(Color.GRAY), 0, str.length(), 0);
        setText(immuableHint);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (immuableHint != null) {
            if (!getText().toString().startsWith(immuableHint.toString())) {
                String current = getText().toString();
                if (current.length() <= immuableHint.length()) {
                    setText(immuableHint);
                } else {
                    String tmp = current.substring(immuableHint.length()-1, current.length());
                    setText(immuableHint);
                    append(tmp);
                }
                Selection.setSelection(getText(), getText().toString().length());
            }
        }

    }
}

