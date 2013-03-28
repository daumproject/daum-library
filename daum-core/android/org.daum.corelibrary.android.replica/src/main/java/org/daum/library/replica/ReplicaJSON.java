package org.daum.library.replica;

import org.daum.library.replica.cache.Cache;
import org.daum.library.replica.cache.ReplicaService;
import org.kevoree.annotation.*;
import org.kevoree.extra.marshalling.JacksonSerializer;
import org.kevoree.extra.marshalling.RichJSONObject;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/03/13
 * Time: 13:41
 * To change this template use File | Settings | File Templates.
 */
@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = false)
})
@Library(name = "JavaSE")
@ComponentType
public class ReplicaJSON extends AbstractPage
{

    private String transformeUrlIntoTileUrl(String url,int indice){
        int compteurSlash = 0;
        for (int i = url.length(); (i = url.lastIndexOf("/", i - 1)) != -1;) {
            if(compteurSlash == indice){
                return (url.substring(i));
            }
            compteurSlash = compteurSlash+1;
        }
        return null;
    }

    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {

        if(!kevoreeHttpRequest.getUrl().equals("/"))
        {
             StringBuilder t = new StringBuilder();
            Cache c =   getPortByName("service",ReplicaService.class).getCache("org.daum.common.genmodel.impl.AgentImpl");

                      for(Object key : c.keySet()){
                          RichJSONObject value =     new RichJSONObject(c.get(key).getValue());
                          t.append(value.toJSON()+" "+c.get(key).clocks);
                      }


            kevoreeHttpResponse.setContent(t.toString());

        } else
        {
            kevoreeHttpResponse.setContent("Replica JSON Server 1.0");
        }

        return kevoreeHttpResponse;
    }
}
