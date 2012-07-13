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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;


public abstract class TileCacheImpl implements ITileCache {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LRUMap<Integer, Tile> tileMap = new LRUMap<Integer, Tile>(10000);
    private final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
    private int age;
    private String pathCache= "";
    private String tileServer = "http://tile.openstreetmap.org";
    protected abstract Tile parseTile(String uri);

    public TileCacheImpl(String pathCache)
    {
        this.pathCache = pathCache;
    }

    public Tile getTile(String uri)
    {
        Tile tile =null;
        try
        {
            tile= parseTile(uri);
            if (tile == null)
                return null;

            if (tileMap.containsKey(tile.getKey()))
            {   // found in memory cache
                logger.debug("Tile found in memory cache: " + tile.getKey());
                tile = (Tile) tileMap.get(tile.getKey());
            }
            else {
                if (isTileCached(tile))
                {  // found in disk
                    logger.debug("Tile found in dick cache: " + tile.getKey());
                    tile.load(getCachePath(tile));
                    // refresh cache with last tile
                    tileMap.put(tile.getKey(), tile);
                }else
                {
                    logger.debug("Downloading Tile on "+tileServer);
                    downloadTile(tile,getTileUrl(tile));
                    storeTile(tile);
                }
            }
        } catch (Exception e)
        {
            logger.error("getTile ",e);
        }
        return tile;
    }

    private void downloadTile(Tile tile, URL url)
    {
        InputStream is = null;
        try {
            is = getServerStream(url);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(is, baos);
            tile.setImage(baos.toByteArray());
        } catch (IOException e) {
            logger.error("downloadTile ", e);
        }
    }

    public URL getTileUrl(Tile tile) throws MalformedURLException {
        String url = tileServer + "/" + tile.getZoom() + "/" + tile.getX() +"/" + tile.getY() + ".png";
        return new URL(url);
    }

    private InputStream getServerStream(URL url) throws IOException {
        URLConnection conn = getConnection(url);
        InputStream is = conn.getInputStream();
        return is;
    }

    private URLConnection getConnection(URL url) throws IOException {
        URLConnection conn = url.openConnection(Proxy.NO_PROXY);
        conn.setRequestProperty("User-Agent", USER_AGENT);
        return conn;
    }

    /**
     * Write tile to disk cache
     * @param tile tile to store
     * @throws java.io.IOException on write faliure
     */
    public void storeTile(Tile tile) throws IOException
    {
        File file = new File(getCachePath(tile));
        FileUtils.writeByteArrayToFile(file, tile.getImage());
        tileMap.put(tile.getKey(), tile);
    }


    public boolean isTileCached(Tile tile) {
        File file = new File(getCachePath(tile));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -age);
        return file.exists() && file.lastModified()  < calendar.getTimeInMillis();
    }


    /**
     * @return File pathCache of a tile in disk cache
     */
    protected String getCachePath(Tile tile)
    {
        int x = tile.getX();
        int y = tile.getY();
        int zoom = tile.getZoom() + 2;
        String type = tile.getType();

        String path = getPathCache();
        if (!type.isEmpty()) {
            path += File.separator + type;
        }
        path += File.separator + zoom + File.separator + x / 1024 +
                File.separator + x % 1024 + File.separator + y / 1024 +
                File.separator + y % 1024 + ".png";

        return path;
    }


    /**
     * {@inheritDoc}
     * @throws java.io.IOException
     */
    public InputStream parseResponse(InputStream serverStream, String remoteUri, String localUri) throws IOException {
        return serverStream;
    }


    /**
     * @return the pathCache
     */
    public String getPathCache() {
        return pathCache;
    }

    /**
     * @param pathCache the pathCache to set
     */
    public void setPathCache(String pathCache) {
        this.pathCache = pathCache;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

}
