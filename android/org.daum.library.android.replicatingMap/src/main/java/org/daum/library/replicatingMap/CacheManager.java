package org.daum.library.replicatingMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 14:52
 */
public class CacheManager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private  KPort send;
    private  String id;
    public CacheManager(KPort send,String id){
        this.send = send;
        this.id =id;
    }

    private ConcurrentHashMap<String, Cache> cacheManager= new ConcurrentHashMap<String,Cache>();

    public  Cache getCache(String name)
    {
        if(!cacheManager.containsKey(name))
        {
            logger.debug("Creating cache "+name);
            Cache cache  =  new Cache(name,this) ;
            cacheManager.put(name,cache);
        }
        return cacheManager.get(name);
    }

    public Set<String> getAllCache(){
        return  cacheManager.keySet();
    }

    public   int getCount(){

        int count=0;
        for( Object key : cacheManager.keySet())
        {
            count += cacheManager.get(key).size();
        }
        count += cacheManager.size();
        return count;
    }

    public synchronized void remoteReceived(Object o){

        logger.debug("Remote Received "+((Event) o).op);

        if(o instanceof Event)
        {
            Event msg = (Event)o;


            if(getCount() != msg.count)
            {
                logger.debug("REQUEST SNAPSHOT "+msg.source);



            }
        }
    }


    public void remoteDisptach(Event e)
    {
        // todo replace with something more brillant
        e.count = getCount();
        send.process(e);
    }


}
