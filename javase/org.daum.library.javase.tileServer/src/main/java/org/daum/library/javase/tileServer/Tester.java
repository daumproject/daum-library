package org.daum.library.javase.tileServer;
/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/11/12
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class Tester {

    public static void main(String[] args) {
        String url = "http://tile.openstreetmap.org/13/4159/2833.png";
        String urlModifier = url.substring("http://tile.opentreetmap.org/".length());
        System.out.print(urlModifier);

       /* mapnikCache = new MapnikCache();
        tileCache = new AbstractTileCache();
        Tile tile = mapnikCache.parseTile("13/4159/2831.png");
        System.out.println("URL img = > "+getTileUrl(tile));
        try {
            downloadTile(tile,new URL(getTileUrl(tile)));
            tileCache.storeTile(tile);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.print("Size : "+tileCache.getTileMap().size());  */
    }
}
