package org.daum.library.javase.copterManager.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 04/07/12
 * Time: 11:18
 */
public class MemCache
{
    private static Logger logger = LoggerFactory.getLogger(MemCache.class);
    private static String no_found_message ="404";
    private static int size_cache =   5000;
    private static LRUMap<String,byte[]> cache = new LRUMap<String, byte[]>(size_cache);

    public static byte[] getRessource(String url)
    {
        if(cache.containsKey(url))
        {
            // found in memory cache
            logger.debug("Ressource found in memory cache: " + url);
            return  cache.get(url);
        }  else
        {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try
            {
                InputStream res =    MemCache.class.getClassLoader().getResourceAsStream(url);
                if(res != null)
                {
                    // found in disk
                    logger.debug("Ressource found in disk : " + url);
                    int nRead;
                    byte[] data = new byte[16384];
                    while ((nRead = res.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                    cache.put(url,buffer.toByteArray());
                } else
                {
                    return "404".getBytes();
                }

            } catch (IOException e) {
                return "404".getBytes();
            }
            return buffer.toByteArray();
        }

    }



    public static int getSize_cache() {
        return size_cache;
    }

    public static void setSize_cache(int size_cache) {
        MemCache.size_cache = size_cache;
    }


}
