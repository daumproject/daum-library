package org.daum.library.javase.tileServer;
import org.kevoree.annotation.*;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/11/12
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */

@DictionaryType({
        @DictionaryAttribute(name = "pathCache",defaultValue = "/tmp/tila", optional =false)   ,
        @DictionaryAttribute(name = "provider", defaultValue = "OpenStreetMap", optional = true,vals={"OpenStreetMap","Google"})
})
@Library(name = "JavaSE")
@ComponentType
public class TileServer extends AbstractPage {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ITileCache tileCacheImpl = null;

    @Start
    public void start(){
        super.startPage();
        logger.debug("Tile Server starting...");
        tileCacheImpl   = new MapnikCache(getpathCache());
    }

    @Stop
    public void stop()
    {
        logger.debug("Tile Server stoping...");
        super.stopPage();
        tileCacheImpl= null;
    }


    @Update
    public void update()
    {
        tileCacheImpl.setPathCache(getpathCache());
    }

    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse)
    {
        if(!kevoreeHttpRequest.getUrl().equals("/"))
        {
            Tile tile = tileCacheImpl.getTile(transformeUrlIntoTileUrl(kevoreeHttpRequest.getUrl()));
            if(tile != null && tile.getImage() != null)
            {
                kevoreeHttpResponse.setRawContent(tile.getImage());
            }else
            {
                // todo
                logger.error("WTF");
                kevoreeHttpResponse.setRawContent("404".getBytes());
            }
        } else
        {
            kevoreeHttpResponse.setContent("You've reached the "+getDictionary().get("provider").toString()+"tile server.");
        }

        return kevoreeHttpResponse;
    }

    private String transformeUrlIntoTileUrl(String url){
        int compteurSlash = 0;
        for (int i = url.length(); (i = url.lastIndexOf("/", i - 1)) != -1;) {
            if(compteurSlash == 2){
                return (url.substring(i));
            }
            compteurSlash = compteurSlash+1;
        }
        return null;
    }


    public String  getpathCache()
    {
        String path = getDictionary().get("pathCache").toString();
        if(path == null)
        {
            logger.debug("Getting temporary directory");
            path = System.getProperty("java.io.tmpdir")+"/tila";
        }
        return  path;
    }
}
