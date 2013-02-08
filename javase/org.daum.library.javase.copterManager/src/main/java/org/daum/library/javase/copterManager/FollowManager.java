package org.daum.library.javase.copterManager;

import org.kevoree.annotation.*;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 08/02/13
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
@Provides({
       @ProvidedPort(name = "followmeuser", type = PortType.MESSAGE)
})
@ComponentType
public class FollowManager extends AbstractPage {


    @Start
    public void start() {


    }

    @Stop
    public void stop() {

    }

    @Update
    public void update() {

    }


    @Port(name = "followmeuser")
    public void followme_users(Object m)
    {
        System.out.println(m);
    }


    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {
        String page = new String(WebCache.load("pages/followers.html"));
        kevoreeHttpResponse.setContent(WebCache.apply(getModelService().getLastModel(),getNodeName(),page));
        return kevoreeHttpResponse;
    }
}
