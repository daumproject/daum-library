package org.daum.library.web.pages;

import org.daum.library.web.utils.WebCache;
import org.daum.library.web.ws.WebSocketChannel;
import org.daum.library.web.ws.WsHandler;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 27/07/12
 * Time: 17:25
 * To change this template use File | Settings | File Templates.
 */

@Requires({
        @RequiredPort(name = "ws", type = PortType.SERVICE, className = WsHandler.class, optional = false)
})
@Provides({
        @ProvidedPort(name = "input", type = PortType.MESSAGE)
})
@ComponentType
public class FlotPage extends AbstractPage
{

    private WebSocketChannel webSocketChannel = new WebSocketChannel();

    @Start
    public void  start()
    {
        super.startPage();

        getModelService().registerModelListener(new ModelListener() {

            @Override
            public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public void modelUpdated() {
                logger.debug("Request Ws Demand");
                getPortByName("ws", WsHandler.class).addHandler("/flotpage", webSocketChannel);
            }
        });
    }
    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {

        String page = new String(WebCache.load("pages/flotpage.html"));
        kevoreeHttpResponse.setContent(WebCache.apply(getModelService().getLastModel(),getNodeName(),page));
        return kevoreeHttpResponse;
    }


    @Port(name = "input")
    public void appendIncoming(Object msg) {

        //  System.out.println(msg);

        try {
            String[] values = msg.toString().split(",");
            //System.out.println(values.length);;
            for (int i = 0; i < values.length; i++) {
                String[] lvl = values[i].split("=");
                //System.out.println(values[i]);
                //System.out.println(lvl.length);
                if (lvl.length >= 2) {
                    Double value = Double.parseDouble(lvl[1]);
                    Long time = System.currentTimeMillis();

                    //System.out.println(lvl[0]);
                    // System.out.println(value);
                    // System.out.println(time);
                    webSocketChannel.broadcast(value.toString());

                }
            }
        } catch (Exception e) {
            logger.warn("Grapher bad message => " + e.getMessage());
        }

    }
}
