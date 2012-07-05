package org.daum.library.web;

import org.kevoree.ContainerRoot;
import org.kevoree.framework.KevoreePropertyHelper;
import org.kevoree.framework.NetworkHelper;
import scala.Option;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 04/07/12
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
public class WebCache {

       // todo add timeout add check memory
    private static HashMap<String,byte[]> cache = new HashMap<String, byte[]>();

    public static byte[] load(String url)
    {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try
            {
                InputStream res =    WebCache.class.getClassLoader().getResourceAsStream(url);
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
                return "404".getBytes();
            }
            return buffer.toByteArray();
        }


    public static String getAddress (ContainerRoot model,String remoteNodeName)
    {
        String ip = "127.0.0.1";
        Option<String> ipOption = NetworkHelper.getAccessibleIP(KevoreePropertyHelper
                .getStringNetworkProperties(model, remoteNodeName, org.kevoree.framework.Constants.KEVOREE_PLATFORM_REMOTE_NODE_IP()));
        if (ipOption.isDefined()) {
            ip = ipOption.get();
        }
        return ip;
    }
}
