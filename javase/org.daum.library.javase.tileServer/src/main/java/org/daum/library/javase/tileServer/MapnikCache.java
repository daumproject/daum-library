/*
 * Copyright 20010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.daum.library.javase.tileServer;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * WMS Tile Cache implementation.
 *
 * @author Jose Luis Martin
 */
public class MapnikCache extends  TileCacheImpl
{
    private String tileServer = "http://tile.openstreetmap.org";

    /**
     * @param pathCache
     */
    public  MapnikCache(String pathCache)
    {
        super(pathCache);
    }

    protected Tile parseTile(String uri) {
        Tile tile = null;
        try  {
            String[] splited = uri.split("/");
            int length = splited.length;

            int x = Integer.parseInt(splited[length - 2]);
            int y = Integer.parseInt(splited[length - 1].substring(0, splited[length - 1].lastIndexOf('.')));
            int zoom = Integer.parseInt(splited[length - 3]);

            tile = new Tile(x, y, zoom);
            tile.setMimeType("image/png");
        }
        catch (Exception e) {
            System.out.println(""+e);
        }
        return tile;
    }

    public URL getTileUrl(Tile tile) {
        String url = tileServer + "/" + tile.getZoom() + "/" + tile.getX() +"/" + tile.getY() + ".png";
        try {
            return new URL (url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public String getServerUrl() {
        return tileServer;
    }

    @Override
    public String getServerUrl(String query) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
