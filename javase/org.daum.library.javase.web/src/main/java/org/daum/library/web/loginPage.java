package org.daum.library.web;

import org.kevoree.annotation.ComponentType;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/06/12
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
@ComponentType
public class loginPage  extends AbstractPage {
    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse response) {
        response.setContent("LOGIN");
        return response;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
