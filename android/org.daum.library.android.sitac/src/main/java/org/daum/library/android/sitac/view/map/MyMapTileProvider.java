package org.daum.library.android.sitac.view.map;

import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.modules.INetworkAvailablityCheck;
import org.osmdroid.tileprovider.tilesource.ITileSource;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 05/07/12
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class MyMapTileProvider extends MapTileProviderBasic {

    public MyMapTileProvider(IRegisterReceiver pRegisterReceiver, INetworkAvailablityCheck aNetworkAvailablityCheck, ITileSource pTileSource) {
        super(pRegisterReceiver, aNetworkAvailablityCheck, pTileSource);
    }

    @Override
    public void detach() {
        // XXX do not detach provider because if we do
        // the executor is shutdown and the map will no longer
        // be updated. By doing this, even if you change tab
        // map will still load and no RejectedExecutionException
        // will be thrown
    }
}
