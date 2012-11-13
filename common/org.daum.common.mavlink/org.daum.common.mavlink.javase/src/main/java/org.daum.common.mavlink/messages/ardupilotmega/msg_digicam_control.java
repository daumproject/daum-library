/**
 * Generated class : msg_digicam_control
 * DO NOT MODIFY!
 **/
package org.daum.common.mavlink.messages.ardupilotmega;

import org.daum.common.mavlink.IMAVLinkCRC;
import org.daum.common.mavlink.MAVLinkCRC;
import org.daum.common.mavlink.io.LittleEndianDataInputStream;
import org.daum.common.mavlink.io.LittleEndianDataOutputStream;
import org.daum.common.mavlink.messages.MAVLinkMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class msg_digicam_control
 * Control on-board Camera Control System to take shots.
 **/
public class msg_digicam_control extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_DIGICAM_CONTROL = 155;
  private static final long serialVersionUID = MAVLINK_MSG_ID_DIGICAM_CONTROL;
  public msg_digicam_control(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_DIGICAM_CONTROL;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 13;
}

  /**
   * Correspondent value to given extra_param
   */
  public float extra_value;
  /**
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
  /**
   * 0: stop, 1: start or keep it up //Session control e.g. show/hide lens
   */
  public int session;
  /**
   * 1 to N //Zoom's absolute position (0 means ignore)
   */
  public int zoom_pos;
  /**
   * -100 to 100 //Zooming step value to offset zoom from the current position
   */
  public int zoom_step;
  /**
   * 0: unlock focus or keep unlocked, 1: lock focus or keep locked, 3: re-lock focus
   */
  public int focus_lock;
  /**
   * 0: ignore, 1: shot or start filming
   */
  public int shot;
  /**
   * Command Identity (incremental loop: 0 to 255)//A command sent multiple times will be executed or pooled just once
   */
  public int command_id;
  /**
   * Extra parameters enumeration (0 means ignore)
   */
  public int extra_param;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  extra_value = (float)dis.readFloat();
  target_system = (int)dis.readUnsignedByte()&0x00FF;
  target_component = (int)dis.readUnsignedByte()&0x00FF;
  session = (int)dis.readUnsignedByte()&0x00FF;
  zoom_pos = (int)dis.readUnsignedByte()&0x00FF;
  zoom_step = (int)dis.readByte();
  focus_lock = (int)dis.readUnsignedByte()&0x00FF;
  shot = (int)dis.readUnsignedByte()&0x00FF;
  command_id = (int)dis.readUnsignedByte()&0x00FF;
  extra_param = (int)dis.readUnsignedByte()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+13];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeFloat(extra_value);
  dos.writeByte(target_system&0x00FF);
  dos.writeByte(target_component&0x00FF);
  dos.writeByte(session&0x00FF);
  dos.writeByte(zoom_pos&0x00FF);
  dos.write(zoom_step&0x00FF);
  dos.writeByte(focus_lock&0x00FF);
  dos.writeByte(shot&0x00FF);
  dos.writeByte(command_id&0x00FF);
  dos.writeByte(extra_param&0x00FF);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 13);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[19] = crcl;
  buffer[20] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_DIGICAM_CONTROL : " +   "  extra_value="+extra_value+  "  target_system="+target_system+  "  target_component="+target_component+  "  session="+session+  "  zoom_pos="+zoom_pos+  "  zoom_step="+zoom_step+  "  focus_lock="+focus_lock+  "  shot="+shot+  "  command_id="+command_id+  "  extra_param="+extra_param;}
}
