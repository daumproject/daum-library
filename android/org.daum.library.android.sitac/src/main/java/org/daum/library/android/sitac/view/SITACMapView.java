package org.daum.library.android.sitac.view;

import java.util.Observable;
import java.util.Observer;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import org.daum.library.android.sitac.listener.OnOverlayEventListener;
import org.daum.library.android.sitac.view.entity.DemandEntity;
import org.daum.library.android.sitac.view.entity.IEntity;
import org.daum.library.android.sitac.view.map.MapOverlay;
import org.daum.library.android.sitac.view.map.MyMapTileProvider;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.modules.INetworkAvailablityCheck;
import org.osmdroid.tileprovider.modules.NetworkAvailabliltyCheck;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.util.constants.MapViewConstants;

import android.content.Context;
import android.widget.RelativeLayout;

public class SITACMapView extends RelativeLayout implements Observer {

    // Debugging
    private static final String TAG = "SITACView";

    private Context ctx;
    private MapView mapView;
    private MapController mapCtrl;
    private MapOverlay overlay;

    public SITACMapView(Context context) {
        super(context);
        this.ctx = context;
        initUI();
        configUI();
    }

    private void initUI() {
        IRegisterReceiver receiver = new SimpleRegisterReceiver(ctx);
        INetworkAvailablityCheck networkAvailablityCheck = new NetworkAvailabliltyCheck(ctx);
        ResourceProxy resProxy = new DefaultResourceProxyImpl(ctx);
        MyMapTileProvider tileProvider = new MyMapTileProvider(
                receiver, networkAvailablityCheck, TileSourceFactory.DEFAULT_TILE_SOURCE);

        mapView = new MapView(ctx, 256, resProxy, tileProvider);

        mapCtrl = mapView.getController();
        overlay = new MapOverlay(ctx);
    }

    private void configUI() {
        RelativeLayout.LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);
        mapView.getOverlays().add(overlay);

        mapCtrl.setZoom(18);

        GeoPoint gPt = new GeoPoint(48.11534,-1.638336); // default location at IRISA
        mapCtrl.setCenter(gPt);

        addView(mapView, params);
    }

    public void addEntity(IEntity entity) {
        overlay.addEntity(entity);
        entity.addObserver(this);
        mapView.postInvalidate();
    }

    public boolean hasEntity(IEntity entity) {
        return overlay.hasEntity(entity);
    }

    public void deleteEntity(IEntity entity) {
        overlay.deleteEntity(entity);
        mapView.postInvalidate();
    }

    /**
     * Moves the center of the map to the one given in parameter
     * @param p where the map will be centered on
     */
    public void setCenter(IGeoPoint p) {
        if (p != null) {
            mapCtrl.setCenter(p);
            mapView.postInvalidate();
        }
    }

    public void setOnOverlayEventListener(OnOverlayEventListener listener) {
        overlay.setOnOverlayEventListener(listener);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof DemandEntity) {
            DemandEntity d = (DemandEntity) data;
            if (d.isDestroyable()) deleteEntity(d);
        }
        mapView.postInvalidate();
    }

    public void setMapProvider(String url) {
        XYTileSource mapSources = new XYTileSource("MapProvider", null, 0, 18, 256, ".png", url);
        mapView.setTileSource(mapSources);
    }
}