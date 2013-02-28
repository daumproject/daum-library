package org.daum.library.javase.jtouchDB;


import com.couchbase.touchdb.TDServer;
import com.couchbase.touchdb.listener.TDListener;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.KevoreeMessage;
import org.kevoree.framework.MessagePort;
import org.lightcouch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
@Requires({
        @RequiredPort(name = "notification", type = PortType.MESSAGE,optional = true,theadStrategy = ThreadStrategy.SHARED_THREAD)
})
@Provides({
        @ProvidedPort(name = "cluster", type = PortType.MESSAGE,theadStrategy = ThreadStrategy.NONE),
        @ProvidedPort(name = "service", type = PortType.SERVICE, className = TouchDBService.class)
})
@ComponentType
public class JTouchDB extends AbstractComponentType  implements  TouchDBService,Runnable
{
    private TDListener listener;
    private  TDServer server = null;
    private Integer port=8888;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private HashMap<String,CouchDbClient> conn = new HashMap<String, CouchDbClient>();
    private List<String> changelistener = new ArrayList<String>();
    private HashMap<String,String> changeslogs = new HashMap<String, String>();
    private Thread t;
    private boolean alive = false;

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
            alive = true;
            t = new Thread(this);
            t.start();
        } catch (Exception e) {
            logger.error("Unable to create JTouchDB", e);
        }
    }


    @Stop
    public void stop()
    {
        try
        {
            alive=false;
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
            CouchDbClient r=null;
            try
            {
               r = conn.get(document);
            }   catch (Exception e){
                conn.remove(document);
                return getDb(document);
            }
            return r;
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
                    }
                    e.printStackTrace();
                }
            }   while (ok == false && t !=null);

        //    addChangeListener(t);

            conn.put(document,t);
            return t;

        }
    }

    @Port(name = "service", method = "getDbClient")
    @Override
    public CouchDbClient getDbClient(String document){
        return   getDb(document);
    }
    @Port(name = "service", method = "addChangeListener")
    @Override
    public void addChangeListener(String document) {
        changelistener.add(document);
    }
    @Port(name = "service", method = "removeChangeListener")
    @Override
    public void removeChangeListener(String document) {
        changelistener.remove(document);
    }

    @Override
    public void run()
    {
        while(alive)
        {
            for(String key :changelistener)
            {
                try {
                    ChangesResult changes = getDbClient(key).changes().getChanges();
                    String seq = getDbClient(key).changes().getChanges().getLastSeq();

                    for(ChangesResult.Row r : changes.getResults() )
                    {

                        if(r.getSeq().equals( getDbClient(key).changes().getChanges().getLastSeq()))
                        {
                            if(changeslogs.containsKey(key))
                            {
                                if(!changeslogs.get(key).equals(seq))
                                {
                                    getPortByName("notification", MessagePort.class).process(r);
                                }
                            } else
                            {
                                getPortByName("notification", MessagePort.class).process(r);

                            }

                        }
                    }
                    changeslogs.put(key,seq);

                }   catch (Exception e){

                }

            }
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
