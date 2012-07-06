package org.daum.library.web.pages;

import org.daum.library.web.utils.WebCache;
import org.kevoree.annotation.ComponentType;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/06/12
 * Time: 09:17
 * To change this template use File | Settings | File Templates.
 */

@ComponentType
public class ResourcesPage extends AbstractPage {

    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse)
    {
        String url = kevoreeHttpRequest.getUrl().replace(getDictionary().get("urlpattern").toString().replace("*",""),"");
        kevoreeHttpResponse.setRawContent(WebCache.load(url));
        return kevoreeHttpResponse;
    }
}
