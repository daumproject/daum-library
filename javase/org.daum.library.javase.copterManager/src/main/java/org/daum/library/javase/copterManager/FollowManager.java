package org.daum.library.javase.copterManager;


import org.daum.common.followermodel.Event;
import org.daum.common.followermodel.Follower;
import org.daum.common.followermodel.Message;
import org.daum.library.javase.copterManager.cache.MemCache;
import org.daum.library.javase.copterManager.ws.WebSocketChannel;
import org.daum.library.javase.copterManager.ws.WsHandler;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.extra.marshalling.JacksonSerializer;
import org.kevoree.extra.marshalling.RichJSONObject;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import org.kevoree.log.Log;
import org.webbitserver.WebSocketConnection;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 08/02/13
 * Time: 11:10
 */

@Requires({
        @RequiredPort(name = "ws", type = PortType.SERVICE, className = WsHandler.class, optional = false, needCheckDependency = true)
})
@Provides({
        @ProvidedPort(name = "followmeuser", type = PortType.MESSAGE)
})
@org.kevoree.annotation.DictionaryType({@org.kevoree.annotation.DictionaryAttribute(name = "current", optional = true)})
@ComponentType
public class FollowManager extends AbstractPage  implements Observer {

    private Follower current;
    private HashMap<String,Follower> list =  new HashMap<String, Follower>();
    private WebSocketChannel webSocketChannel = new WebSocketChannel();

    @Start
    public void  start()
    {
        super.startPage();

        getModelService().registerModelListener(new ModelListener() {
            @Override
            public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public boolean afterLocalUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }


            @Override
            public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;
            }

            @Override
            public void modelUpdated()
            {
               Log.debug("Request Ws Demand");
                getPortByName("ws", WsHandler.class).addHandler("/followmanager",webSocketChannel);
            }

            @Override
            public void preRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void postRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        webSocketChannel.getNotifyConnection().addObserver(this);

    }
    @Stop
    public void stop()
    {
        Log.debug("Remove Ws Demand");
        getPortByName("ws", WsHandler.class).removeHandler("/followmanager");
    }

    @Update
    public void update() {

    }


    @Port(name = "followmeuser")
    public void followme_users(Object json)
    {

        Follower f = JacksonSerializer.convFromJSON(json.toString()).fromJSON(Follower.class);
        f.isfollowed  = false;

        String current_followerId = getDictionary().get("current").toString();

        if(current_followerId != null && current_followerId.length() > 0)
        {

            if(f.id.equals(current_followerId))
            {
                f.isfollowed  = true;
            }
        }


        if(f.event == Event.ADD){
            list.put(f.id,f);

        }else if(f.event == Event.UPDATE){

            if(!list.containsKey(f.id)){
                f.event = Event.ADD;
            }
            list.put(f.id, f);
        } else if(f.event == Event.DELETE){
            list.remove(f.id);
        }

        RichJSONObject t = new RichJSONObject(f);
        webSocketChannel.broadcast(t.toJSON());

    }


    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {
        String page = new String(MemCache.getRessource("pages/followers.html"));

        kevoreeHttpResponse.setContent(page);
        return kevoreeHttpResponse;
    }

    @Override
    public void update(Observable observable, Object connection) {
        loadAll((WebSocketConnection) connection);
    }


    public void loadAll(WebSocketConnection connection){

        try
        {

            for(String key : list.keySet())
            {
                Message data = list.get(key);
                data.event = Event.ADD;
                RichJSONObject t = new RichJSONObject(data);
                ((WebSocketConnection)connection).send(t.toJSON());
            }



        } catch (Exception e) {
            Log.error("", e);
        }
    }
}
