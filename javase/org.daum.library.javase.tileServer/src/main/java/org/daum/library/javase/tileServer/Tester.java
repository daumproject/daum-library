package org.daum.library.javase.tileServer;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/11/12
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class Tester {

    private static MapnikCache mapnikCache;
    private static AbstractTileCache tileCache;
    private static String tileServer = "http://tile.openstreetmap.org";
    private static String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";

    public static void main(String[] args) {
        /*    String url = "/13/4159/2833.png";
       AbstractTileCache abstractTileCache = new AbstractTileCache();
       Tile tile = abstractTileCache.getTile(url);
       System.out.print(tile.getImage().length); */

        /*mapnikCache = new MapnikCache();
        tileCache = new AbstractTileCache();
        Tile tile = mapnikCache.parseTile("13/4159/2831.png");
        System.out.println("URL img = > "+getTileUrl(tile));
        try {
            downloadTile(tile,new URL(getTileUrl(tile)));
            tileCache.storeTile(tile);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }     */
    }

    private static void downloadTile(Tile tile, URL url) {
        InputStream is = null;
        try {
            is = getServerStream(url);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(is, baos);
            tile.setImage(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static InputStream getServerStream(URL url) throws IOException {
        URLConnection conn = getConnection(url);
        InputStream is = conn.getInputStream();
        return is;
    }

    private static URLConnection getConnection(URL url) throws IOException {
        URLConnection conn = url.openConnection(Proxy.NO_PROXY);
        conn.setRequestProperty("User-Agent", USER_AGENT);
        return conn;
    }

    public static String getTileUrl(Tile tile) {
        String url = tileServer + "/" + tile.getZoom() + "/" + tile.getX() +"/" + tile.getY() + ".png";
        return url;
    }

}
