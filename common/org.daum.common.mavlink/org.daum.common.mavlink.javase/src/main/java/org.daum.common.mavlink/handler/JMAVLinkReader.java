package org.daum.common.mavlink.handler;

import org.daum.common.mavlink.IMAVLinkCRC;
import org.daum.common.mavlink.IMAVLinkMessage;
import org.daum.common.mavlink.MAVLinkCRC;
import org.daum.common.mavlink.messages.MAVLinkMessage;
import org.daum.common.mavlink.messages.MAVLinkMessageFactory;


import java.io.IOException;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 12/11/12
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
public class JMAVLinkReader implements IJMAVLinkReader,Runnable {

    private ByteFIFO fifo = null;
    public  static int RECEIVED_BUFFER_SIZE = 512;
    public  static int MAX_TM_SIZE = 263;
    private byte[] receivedBuffer = new byte[RECEIVED_BUFFER_SIZE];
    private byte start = IMAVLinkMessage.MAVPROT_PACKET_START_V10;
    private int nbReceived = 0;
    private Thread cThread = null;
    private boolean first = true;
    private boolean invalidData = false;
    private int length;
    private int sequence;
    private int sysId;
    private int componentId;
    private int msgId;
    private byte crcLow;
    private byte crcHigh;
    private  byte[] rawData = null;
    private MAVLinkMessage msg = null;
    private EventListenerListMavLink listenerList = new EventListenerListMavLink();

    public JMAVLinkReader(ByteFIFO fifo)
    {
        this.fifo = fifo;
        cThread  = new Thread(this);
        cThread.start();
    }


    public void shutdown()
    {
        if(cThread != null)
        {
            cThread.interrupt();
            fifo.free();
        }
    }

    public void addEventListener (MavLinkEventListener listener) {
        if(listenerList != null)
        {
            listenerList.add(MavLinkEventListener.class, listener);
        }
    }

    public void removeEventListener (MavLinkEventListener listener) {
        if(listenerList != null)
        {
            listenerList.remove(MavLinkEventListener.class, listener);
        }
    }

    protected void fireEvent (MavLinkEvent evt)
    {
        if(listenerList !=null )
        {
            Object[] listeners = listenerList.getListenerList();
            if(listeners != null)
            {
                for (int i = 0; i < listeners.length; i += 2) {
                    if (listeners[i] == MavLinkEventListener.class)
                    {
                        ((MavLinkEventListener) listeners[i + 1]).incomingMSG((MavLinkEvent)evt);
                    }
                }
            }
        }
    }


    @Override
    public void run()
    {

        while (Thread.currentThread().isAlive()){

            try
            {

                receivedBuffer[nbReceived] = fifo.remove();
                if (receivedBuffer[nbReceived++] == start) {
                    invalidData = false;

                    length = receivedBuffer[nbReceived++] = fifo.remove();
                    length &= 0X00FF;

                    sequence = receivedBuffer[nbReceived++] = fifo.remove();
                    sequence &= 0X00FF;

                    sysId = receivedBuffer[nbReceived++] = fifo.remove();
                    sysId &= 0X00FF;

                    componentId = receivedBuffer[nbReceived++] =fifo.remove();
                    componentId &= 0X00FF;

                    msgId = receivedBuffer[nbReceived++] = fifo.remove();
                    msgId &= 0X00FF;

                    rawData = readRawData(fifo,length);

                    crcLow = receivedBuffer[nbReceived++] = fifo.remove();
                    crcHigh = receivedBuffer[nbReceived++] =fifo.remove();
                    int crc = MAVLinkCRC.crc_calculate_decode(receivedBuffer, length);

                    if (IMAVLinkCRC.MAVLINK_EXTRA_CRC) {
                        // CRC-EXTRA for Mavlink 1.0
                        crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[msgId], crc);
                    }

                    byte crcl = (byte) (crc & 0x00FF);
                    byte crch = (byte) ((crc >> 8) & 0x00FF);
                    if ((crcl == crcLow) && (crch == crcHigh))
                    {

                        msg = MAVLinkMessageFactory.getMessage(msgId, sysId, componentId, rawData);


                        if (msg != null)
                        {
                            msg.sequence = sequence;
                            MavLinkEvent ev = new MavLinkEvent(this,msg);

                            fireEvent(ev);
                        }
                        else
                        {
                            System.err.println("ERROR creating message  Id=" + msgId);
                        }
                    }
                    else
                    {
                        System.err.println("ERROR mavlink CRC16-CCITT compute= " + Integer.toHexString(crc) + "  expected : " + Integer.toHexString(crcHigh & 0x00FF) + Integer.toHexString(crcLow & 0x00FF) + " Id=" + msgId);
                    }
                    // restart buffer
                    nbReceived = 0;

                }
                else {
                    invalidData = true;
                    //System.out.println("invalid : " + receivedBuffer[nbReceived - 1]);
                    // restart buffer
                    nbReceived = 0;
                }
            }
            catch (java.io.EOFException eof) {
                System.err.println("ERROR EOF : " + eof);
                eof.printStackTrace();
            }
            catch (Exception e) {
                System.err.println("ERROR : " + e);
                // restart buffer
                nbReceived = 0;
            }
        }

    }




    protected byte[] readRawData(ByteFIFO b,int nb) throws IOException, InterruptedException {
        byte[] buffer = new byte[nb];
        int index = 0;
        for (int i = 0; i < nb; i++)
        {
            receivedBuffer[nbReceived] = b.remove();
            buffer[index++] = receivedBuffer[nbReceived++];
        }
        return buffer;
    }
}
