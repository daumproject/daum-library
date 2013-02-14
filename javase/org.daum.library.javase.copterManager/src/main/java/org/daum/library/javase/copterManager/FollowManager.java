package org.daum.library.javase.copterManager;

import org.daum.library.javase.copterManager.pojo.Follower;
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
import org.webbitserver.WebSocketConnection;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 08/02/13
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */

@Requires({
        @RequiredPort(name = "ws", type = PortType.SERVICE, className = WsHandler.class, optional = false)
})
@Provides({
        @ProvidedPort(name = "followmeuser", type = PortType.MESSAGE)
})
@org.kevoree.annotation.DictionaryType({@org.kevoree.annotation.DictionaryAttribute(name = "current", optional = true)})
@ComponentType
public class FollowManager extends AbstractPage  implements Observer {

    private  Follower current;
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
                logger.debug("Request Ws Demand");
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
    public void stop() {

    }

    @Update
    public void update() {

    }


    @Port(name = "followmeuser")
    public void followme_users(Object json)
    {

        Follower f = JacksonSerializer.convFromJSON(json.toString()).fromJSON(Follower.class);


        String current_followerId = getDictionary().get("current").toString();

        if(current_followerId.length() > 0)
        {

            if(f.idfollower.equals(current_followerId))
            {
              // new position

            }

        }
        list.put(f.idfollower,f);
    }


    @Override
    public KevoreeHttpResponse process(KevoreeHttpRequest kevoreeHttpRequest, KevoreeHttpResponse kevoreeHttpResponse) {
        String page = new String(WebCache.load("pages/followers.html"));
        kevoreeHttpResponse.setContent(WebCache.apply(getModelService().getLastModel(),getNodeName(),page));
        return kevoreeHttpResponse;
    }

    @Override
    public void update(Observable observable, Object connection) {

        System.out.println(connection);
        loadAll((WebSocketConnection) connection);
    }


    public String createJsonFollower(String id,double  lat,double lon,int accuracy,int altitude,int safety_distance)   {
        String json = "{"+addJson("followerid",id)+","+addJson("lat",lat)+","+addJson("lon",lon)+","+addJson("accuracy",accuracy)+","+addJson("altitude",altitude)+","+addJson("safety_distance",safety_distance)+"}";

        return  json;

    }
    public String addJson(Object id,Object value){
        return "\""+id+"\""+":"+"\""+value+"\"";
    }

    public Follower parseJsonFollower(String json){
        Follower follower = new Follower();

        return follower;

    }

    public void loadAll(WebSocketConnection connection){

        try
        {

            for(String key : list.keySet()) {

                RichJSONObject t = new RichJSONObject(list.get(key));

                ((WebSocketConnection)connection).send("ADD" + "$" + t.toJSON());

            }



        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
