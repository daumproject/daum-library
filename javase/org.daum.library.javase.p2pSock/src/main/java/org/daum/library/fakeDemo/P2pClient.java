package org.daum.library.fakeDemo;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 08/08/12
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */
import org.kevoree.framework.message.Message;

import java.io.*;
import java.net.*;
public class P2pClient
{
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    private String adr;
    private int port;

    public P2pClient(String adr, int port){
        this.adr = adr;
        this.port = port;
    }

    void send(Message msg)
    {
        try
        {
            requestSocket = new Socket(adr, port);
         //   System.out.println("Connected to localhost in port 2004");
            //2. get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());

            out.writeObject(msg);
            out.flush();
            out.reset();
        }
        catch(UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try
            {
               if(requestSocket !=null)
                requestSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }

}