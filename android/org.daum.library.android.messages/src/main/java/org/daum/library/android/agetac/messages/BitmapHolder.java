package org.daum.library.android.agetac.messages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 01/06/12
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class BitmapHolder {

    private static final String TAG = "BitmapHolder";
    private static final boolean D = true;

    private static final String IN_MSG_IMG_PATH = "in_msg.png";
    private static final String OUT_MSG_IMG_PATH = "out_msg.png";

    private static BitmapHolder instance;
    private static Bitmap inBitmap;
    private static Bitmap outBitmap;

    private BitmapHolder(Context ctx) {
        try {
            if (D) Log.i(TAG, "BEGIN BitmapHolder()");
            InputStream is_inImg = getClass().getResourceAsStream(IN_MSG_IMG_PATH);
            if (D) Log.i(TAG, "is_inImg = "+is_inImg);
            InputStream is_outImg = getClass().getResourceAsStream(OUT_MSG_IMG_PATH);
            if (D) Log.i(TAG, "is_outImg = "+is_outImg);
            inBitmap = BitmapFactory.decodeStream(is_inImg);
            outBitmap = BitmapFactory.decodeStream(is_outImg);
            if (D) Log.i(TAG, "END BitmapHolder()");
        } catch (Exception e) {
            Log.e(TAG, "dre", e);
        }

    }

    public static BitmapHolder getInstance(Context ctx) {
        if (BitmapHolder.instance == null) {
            BitmapHolder.instance = new BitmapHolder(ctx);
        }
        return BitmapHolder.instance;
    }

    public static Bitmap getInBitmap() {
        return inBitmap;
    }

    public static Bitmap getOutBitmap() {
        return outBitmap;
    }
}
