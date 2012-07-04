package org.daum.library.web.pages;

import org.kevoree.annotation.ComponentType;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/06/12
 * Time: 09:17
 * To change this template use File | Settings | File Templates.
 */

@ComponentType
public class ResourcesPage extends AbstractPage {

    private HashMap<String,byte[]> cache = new HashMap<String, byte[]>();

    public byte[] load(String url)
    {
        logger.debug("loading "+url);
        // todo secure
        if(cache.containsKey(url))
        {
            return cache.get(url);
        } else {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try
            {
                InputStream res =    getClass().getClassLoader().getResourceAsStream(url);
                if(res != null){
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
                logger.error("",e);
                return "404".getBytes();
            }
            return buffer.toByteArray();
        }

    }
    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {
        String url = kevoreeHttpRequest.getUrl().replace(getDictionary().get("urlpattern").toString().replace("*",""),"");
        kevoreeHttpResponse.setRawContent(      load(url));
        return kevoreeHttpResponse;
    }
}
