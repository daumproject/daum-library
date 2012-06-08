package org.daum.library.android.sitac.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import org.osmdroid.views.MapView;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 08/06/12
 * Time: 10:35
 * To change this template use File | Settings | File Templates.
 */
public class SITACView extends View {

    private static final String TAG = "SITACView";

    private Context ctx;
    private MapView mapView;

    public SITACView(Context context) {
        super(context);
        this.ctx = context;
        initUI();
    }

    private void initUI() {
        mapView = new MapView(ctx, null);
        Log.i(TAG, "END initUI()");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mapView.draw(canvas);
    }
}
