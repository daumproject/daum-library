package org.daum.library.web.pages;

import org.daum.library.web.utils.WebCache;
import org.kevoree.annotation.ComponentType;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import org.kevoree.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 03/07/12
 * Time: 12:33
 * To change this template use File | Settings | File Templates.
 */
@ComponentType
public class HomePage extends AbstractPage
{
    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {
        Log.debug("" + kevoreeHttpRequest.getResolvedParams());
        String template = new String(WebCache.load("pages/home.html"));
        kevoreeHttpResponse.setContent(template);
        return kevoreeHttpResponse;
    }
}
