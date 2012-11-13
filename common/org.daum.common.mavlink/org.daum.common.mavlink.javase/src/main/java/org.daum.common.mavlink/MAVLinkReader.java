/**
 * $Id$
 * $Date$
 *
 * ======================================================
 * Copyright (C) 2012 Guillaume Helle.
 * Project : MAVLINK Java
 * Module : org.mavlink.library
 * File : org.mavlink.messages.MAVLinkReader.java
 * Author : ghelle
 *
 * ======================================================
 * HISTORY
 * Who       yyyy/mm/dd   Action
 * --------  ----------   ------
 * ghelle	24 ao�t 2012		Create
 * 
 * ====================================================================
 * Licence: ${licence}
 * ====================================================================
 */

package org.daum.common.mavlink;



import org.daum.common.mavlink.messages.MAVLinkMessage;
import org.daum.common.mavlink.messages.MAVLinkMessageFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * @author ghelle
 * @version $Rev$
 *
 */
public class MAVLinkReader {

    private boolean debug = true;

    /**
     * Input stream
     */
    private DataInputStream dis = null;

    /**
     * 
     */
    public final static int RECEIVED_BUFFER_SIZE = 512;

    /**
     * 
     */
    public final static int MAX_TM_SIZE = 263;

    /**
     * 
     */
    private final byte[] receivedBuffer = new byte[RECEIVED_BUFFER_SIZE];

    /**
     * Ne bytes received
     */
    private int nbReceived = 0;

    /**
     * Read MAVLink V1.0 packets by default;
     */
    private byte start = IMAVLinkMessage.MAVPROT_PACKET_START_V10;

    /**
     * MAVLink messages received
     */
    private Vector packets = new Vector();

    /**
     * Constructor with MAVLink 1.0 by default
     * @param dis Data input stream
     */
    public MAVLinkReader(DataInputStream dis) {
        this.dis = dis;
    }

    /**
     * Constructor
     * @param dis Data input stream
     * @param start Start byte for MAVLink version
     */
    public MAVLinkReader(DataInputStream dis, byte start) {
        this.dis = dis;
        this.start = start;
    }

    /**
     * Return next message.
     * If bytes available, try to read it.
     * @return MAVLink message or null
     */
    public MAVLinkMessage getNextMessage() {
        MAVLinkMessage msg = null;
        if (packets.isEmpty()) {
            readNextMessage();
        }
        if (!packets.isEmpty()) {
            msg = (MAVLinkMessage) packets.firstElement();
            packets.removeElementAt(0);
        }
        return msg;
    }

    /**
     * Read next message
     */
    protected void readNextMessage() {
        boolean invalidData = false;
        char myChar;
        int length;
        int sequence;
        int sysId;
        int componentId;
        int msgId;
        byte crcLow;
        byte crcHigh;
        byte[] rawData = null;
         MAVLinkMessage msg = null;

        try {
            if (dis.available() == 0)
                return;
            receivedBuffer[nbReceived] = dis.readByte();
            if (receivedBuffer[nbReceived++] == start) {
                invalidData = false;

                length = receivedBuffer[nbReceived++] = dis.readByte();
                length &= 0X00FF;

                sequence = receivedBuffer[nbReceived++] = dis.readByte();
                sequence &= 0X00FF;

                sysId = receivedBuffer[nbReceived++] = dis.readByte();
                sysId &= 0X00FF;

                componentId = receivedBuffer[nbReceived++] = dis.readByte();
                componentId &= 0X00FF;

                msgId = receivedBuffer[nbReceived++] = dis.readByte();
                msgId &= 0X00FF;

                rawData = readRawData(length);

                crcLow = receivedBuffer[nbReceived++] = dis.readByte();
                crcHigh = receivedBuffer[nbReceived++] = dis.readByte();
                int crc = MAVLinkCRC.crc_calculate_decode(receivedBuffer, length);
               /* TODO
                if (IMAVLinkCRC.MAVLINK_EXTRA_CRC) {
                    // CRC-EXTRA for Mavlink 1.0
                    crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[msgId], crc);
                }*/

                byte crcl = (byte) (crc & 0x00FF);
                byte crch = (byte) ((crc >> 8) & 0x00FF);
                if ((crcl == crcLow) && (crch == crcHigh)) {
                     msg = MAVLinkMessageFactory.getMessage(msgId, sysId, componentId, rawData);
                    if (msg != null) {
                        msg.sequence = sequence;
                        packets.addElement(msg);
                        //if (debug)
                        //    System.out.println("MESSAGE = " + msg);
                    }
                    else {
                        System.err.println("ERROR creating message  Id=" + msgId);
                    }
                }
                else {
                    System.err.println("ERROR mavlink CRC16-CCITT compute= " + Integer.toHexString(crc) + "  expected : "
                                       + Integer.toHexString(crcHigh & 0x00FF) + Integer.toHexString(crcLow & 0x00FF) + " Id=" + msgId);
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

    /**
     * @param nb
     * @return
     * @throws java.io.IOException
     */
    protected byte[] readRawData(int nb) throws IOException {
        byte[] buffer = new byte[nb];
        int index = 0;
        for (int i = 0; i < nb; i++) {
            while (dis.available() == 0) {
                ;
            }
            receivedBuffer[nbReceived] = dis.readByte();
            buffer[index++] = receivedBuffer[nbReceived++];
        }
        return buffer;
    }

}
