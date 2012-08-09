package org.daum.library.p2pSock;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 08/08/12
 * Time: 17:23
 * To change this template use File | Settings | File Templates.
 */

import org.kevoree.framework.message.Message;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;


public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;
    private P2pSock p2pSock;
    public WorkerRunnable(P2pSock p2pSock,Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
        this.p2pSock = p2pSock;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            ObjectInputStream  in = new ObjectInputStreamImpl(input,p2pSock);
            Message  msg = (Message)in.readObject();
            if (!msg.getPassedNodes().contains(p2pSock.getNodeName())) {
                msg.getPassedNodes().add(p2pSock.getNodeName());
            }
            p2pSock.remoteDispatch(msg);

            output.close();
            input.close();

        } catch (Exception e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}