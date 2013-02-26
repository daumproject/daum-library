package org.daum.library.javase.jtouchDB;


import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.listener.TDListener;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Document;
import org.lightcouch.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 23/11/12
 * Time: 15:31
 */
@Library(name = "JavaSE", names = {"Android"})
@DictionaryType({
        @DictionaryAttribute(name = "port_db", defaultValue = "8888", optional = false),
        @DictionaryAttribute(name = "path", defaultValue ="",optional = false)
})
@Provides({
        @ProvidedPort(name = "cluster", type = PortType.MESSAGE,theadStrategy = ThreadStrategy.NONE),
        @ProvidedPort(name = "service", type = PortType.SERVICE, className = TouchDBService.class)
})
@ComponentType
public class JTouchDB extends AbstractComponentType  implements  TouchDBService
{
    private TDListener listener;
    private  TDServer server = null;
    private Integer port=8888;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private HashMap<String,CouchDbClient> conn = new HashMap<String, CouchDbClient>();

    @Start
    public void start()
    {
        try
        {
            String filesDir =getDictionary().get("path").toString();
            if(filesDir.length() == 0)
            {
                filesDir =System.getProperty("java.io.tmpdir")+ File.separator+ "jtouchdb.default";
                logger.warn("You have to choose a path to store sqlitedb default ="+filesDir);
            }else
            {
                server = new TDServer(filesDir);
                port = Integer.parseInt(getDictionary().get("port_db").toString());
                listener = new TDListener(server, port);
                listener.start();
            }

        } catch (Exception e) {
            logger.error("Unable to create JTouchDB", e);
        }
    }


    @Stop
    public void stop()
    {
        try
        {
            if(server != null){
                server.close();
            }
            if(listener !=null){
                listener.stop();
            }
        } catch (Exception e) {
            logger.error("Unable to stop CTouchDB", e);
        }
    }


    @Update
    public void update()
    {
        stop();
        start();

    }

    @Port(name = "cluster")
    public void enterthevoid(Object e){

    }


    public CouchDbClient getDb(String document)
    {
        if(conn.containsKey(document))
        {
                   return conn.get(document);
        }  else
        {
            boolean ok = false;
            CouchDbClient t=null;
            do
            {
                try
                {
                     t  = new CouchDbClient(document,true,"http","localhost",port,"","");
                    ok = true;
                } catch (Exception e){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    e.printStackTrace();
                }
            }   while (ok == false && t !=null);
            conn.put(document,t);
            return t;

        }
    }
    @Port(name = "service", method = "save")
    @Override
    public Response save(String document, Object t) {
        return   getDb(document).save(t);
    }
    @Port(name = "service", method = "update")
    @Override
    public Response update(String document, Object t) {
        return   getDb(document).update(t);
    }
    @Port(name = "service", method = "remove")
    @Override
    public Response remove(String document, Object t) {
        return   getDb(document).remove(t);
    }
    /*   */
    @Port(name = "service", method = "find")
    @Override
    public <T> T find(String document, Class<T> classType, String id) {
        return   getDb(document).find(classType, id);
    }

    @Port(name = "service", method = "findrev")
    @Override
    public <T> T findrev(String document, Class<T> classType, String id, String rev) {
        return   getDb(document).find(classType, id,rev);
    }
}
