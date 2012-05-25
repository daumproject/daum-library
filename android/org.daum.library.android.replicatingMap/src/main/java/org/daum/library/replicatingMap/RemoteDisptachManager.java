package org.daum.library.replicatingMap;

import org.daum.library.replicatingMap.msg.Update;
import org.daum.library.replicatingMap.msg.Updates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 25/05/12
 * Time: 13:49
 */
public class RemoteDisptachManager implements Runnable{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private  Channel channel;
    private  LinkedBlockingQueue<Update> updates = new LinkedBlockingQueue<Update>();
    private  final int secondes =10;
    private  final int trigger =250;
    private String id;
      private Thread thread = new Thread(this);

    public RemoteDisptachManager(String id,Channel channel){
        this.channel=channel;
        thread.start();
        this.id = id;
    }

    public  void addUpdate(Update update)
    {
        try {
            this.updates.add(update);

            if(updates.size() > trigger)
            {
                sendingBlock();
            }
        }catch (Exception e){
            logger.error("The update with key is lost "+update.key);
        }

    }


    public void sendingBlock()
    {
        if(updates.size() > 0 )
        {

            LinkedBlockingQueue<Update> send_updates = new LinkedBlockingQueue<Update>();
            for(int i=0;i<updates.size();i++){
                send_updates.add(updates.poll());
            }
            Updates brodcastUpdates = new Updates();
            brodcastUpdates.setSource(id);
            brodcastUpdates.setUpdates(send_updates);

            logger.warn("Sending block "+send_updates.size());
            channel.write(brodcastUpdates);
        }

    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()){
            sendingBlock();
            try
            {
                Thread.sleep(secondes*1000);
            } catch (InterruptedException e) {

            }

        }


    }
}
