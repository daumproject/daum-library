package org.daum.library.web.pages;

import org.daum.library.web.utils.WebCache;
import org.kevoree.annotation.ComponentType;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 04/07/12
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */

@ComponentType
public class AgentPage extends AbstractPage
{
    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {
        String page = new String(WebCache.load("pages/agent.html"));
        kevoreeHttpResponse.setContent(WebCache.apply(getModelService().getLastModel(),getNodeName(),page));
        return kevoreeHttpResponse;
    }
}
