package org.daum.common.mavlink;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 12/11/12
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */

public interface  IMAVLinkMessage   {

    /**
     * Packet start in MAVLink V1.0
     */
    public final static byte MAVPROT_PACKET_START_V10 = (byte) 0xFE;

    /**
     * Packet start in MAVLink V0.9
     */
    public final static byte MAVPROT_PACKET_START_V09 = (byte) 0x55;

    public final static int CRC_LEN = 5;

    public final static int X25_INIT_CRC = 0x0000ffff;

    public final static int X25_VALIDATE_CRC = 0x0000f0b8;

}