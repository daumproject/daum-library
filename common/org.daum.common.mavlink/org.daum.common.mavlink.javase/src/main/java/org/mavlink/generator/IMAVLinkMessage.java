/**
 * $Id$
 * $Date$
 *
 * ======================================================
 * Copyright (C) 2012 Guillaume Helle.
 * Project : MAVLINK Java
 * Module : org.mavlink.library
 * File : org.mavlink.messages.IMAVLinkMessage.java
 * Author : ghelle
 *
 * ======================================================
 * HISTORY
 * Who       yyyy/mm/dd   Action
 * --------  ----------   ------
 * ghelle	13 ao�t 2012		Create
 * 
 * ====================================================================
 * Licence: ${licence}
 * ====================================================================
 */

package org.mavlink.generator;

/**
 * Some constants for MAVLink
 * @author ghelle
 * @version $Rev$
 *
 */
public interface IMAVLinkMessage {

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
