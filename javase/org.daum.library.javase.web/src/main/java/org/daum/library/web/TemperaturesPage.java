package org.daum.library.web;

import org.daum.library.fakeDemo.pojos.TemperatureMonitor;
import org.daum.library.replica.cache.ReplicaService;
import org.daum.library.replica.listener.ChangeListener;
import org.daum.library.replica.listener.PropertyChangeEvent;
import org.daum.library.replica.listener.PropertyChangeListener;
import org.kevoree.annotation.*;
import org.kevoree.framework.KevoreeElementHelper;
import org.kevoree.framework.KevoreePropertyHelper;
import org.kevoree.framework.NetworkHelper;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import scala.Option;


import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/06/12
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */

@Requires({
        @RequiredPort(name = "service", type = PortType.SERVICE, className = ReplicaService.class, optional = true) })
@ComponentType
public class TemperaturesPage extends AbstractPage {

    public String getAddress (String remoteNodeName)
    {
        String ip = "127.0.0.1";
        Option<String> ipOption = NetworkHelper.getAccessibleIP(KevoreePropertyHelper
                .getStringNetworkProperties(this.getModelService().getLastModel(), remoteNodeName, org.kevoree.framework.Constants.KEVOREE_PLATFORM_REMOTE_NODE_IP()));
        if (ipOption.isDefined()) {
            ip = ipOption.get();
        }
        return ip;
    }

    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {

                kevoreeHttpResponse.setContent("<html>\n" +
                        "<body>\n" +
                        "\n" +
                        "\n" +
                        "<!-- Results\n" +
                        "<input id=\"userInput\" type=\"text\">\n" +
                        "<button onclick=\"ws.send(document.getElementById('userInput').value)\">Send</button>\n" +
                        "-->\n" +
                        "\n" +
                        "\n" +
                        "<div id=\"image\">\n" +
                        "    </div>\n" +
                        "\n" +
                        "\n" +
                        "    <div id=\"text\">\n" +
                        "\n" +
                        "</div>\n" +
                        "\n" +
                        "<script>\n" +
                        "\n" +
                        "    function showMessage(text)\n" +
                        "    {\n" +
                        "    document.getElementById('image').innerHTML = 'updating';\n" +
                        "    document.getElementById('image').innerHTML = '<img src=http://"+getAddress(getNodeName())+":8080/image></img>';\n" +
                        "    }\n" +
                        "\n" +
                        "    var ws = new WebSocket('ws://"+ getAddress(getNodeName())+":8082/jed');\n" +
                        "    showMessage('Connecting...');\n" +
                        "    ws.onopen = function() { showMessage('Connected!'); };\n" +
                        "    ws.onclose = function() { showMessage('Lost connection'); };\n" +
                        "    ws.onmessage = function(msg) { showMessage(msg.data); };\n" +
                        "</script>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>");

        return kevoreeHttpResponse;  //To change body of implemented methods use File | Settings | File Templates.
    }




}
