package org.daum.library.javase.tileServer;
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Library;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/11/12
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */


@Library(name = "JavaSE")
@ComponentType
public class TileServer extends AbstractPage {

    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse)
    {
        AbstractTileCache abstractTileCache = new AbstractTileCache();
        Tile tile = abstractTileCache.getTile(transformeUrlIntoTileUrl(kevoreeHttpRequest.getUrl()));
        kevoreeHttpResponse.setRawContent(tile.getImage());
        return kevoreeHttpResponse;
    }

    private String transformeUrlIntoTileUrl(String url){
       return  url.substring("http://tile.opentreetmap.org/".length());
    }
}