package org.daum.library.web.pages;

import org.daum.library.web.WebCache;
import org.kevoree.annotation.ComponentType;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 04/07/12
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */

@ComponentType
public class AdminPage extends AbstractPage
{
    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {
        logger.debug(""+kevoreeHttpRequest.getResolvedParams());
        String template = new String(WebCache.load("pages/admin.html"));
        kevoreeHttpResponse.setContent(template);
        return kevoreeHttpResponse;
    }
}
