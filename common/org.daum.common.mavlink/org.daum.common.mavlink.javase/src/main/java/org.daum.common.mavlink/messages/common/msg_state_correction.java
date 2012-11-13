/**
 * Generated class : msg_state_correction
 * DO NOT MODIFY!
 **/
package org.daum.common.mavlink.messages.common;

import org.daum.common.mavlink.IMAVLinkCRC;
import org.daum.common.mavlink.MAVLinkCRC;
import org.daum.common.mavlink.io.LittleEndianDataInputStream;
import org.daum.common.mavlink.io.LittleEndianDataOutputStream;
import org.daum.common.mavlink.messages.MAVLinkMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class msg_state_correction
 * Corrects the systems state by adding an error correction term to the position and velocity, and by rotating the attitude by a correction angle.
 **/
public class msg_state_correction extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_STATE_CORRECTION = 64;
  private static final long serialVersionUID = MAVLINK_MSG_ID_STATE_CORRECTION;
  public msg_state_correction(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_STATE_CORRECTION;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 36;
}

  /**
   * x position error
   */
  public float xErr;
  /**
   * y position error
   */
  public float yErr;
  /**
   * z position error
   */
  public float zErr;
  /**
   * roll error (radians)
   */
  public float rollErr;
  /**
   * pitch error (radians)
   */
  public float pitchErr;
  /**
   * yaw error (radians)
   */
  public float yawErr;
  /**
   * x velocity
   */
  public float vxErr;
  /**
   * y velocity
   */
  public float vyErr;
  /**
   * z velocity
   */
  public float vzErr;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  xErr = (float)dis.readFloat();
  yErr = (float)dis.readFloat();
  zErr = (float)dis.readFloat();
  rollErr = (float)dis.readFloat();
  pitchErr = (float)dis.readFloat();
  yawErr = (float)dis.readFloat();
  vxErr = (float)dis.readFloat();
  vyErr = (float)dis.readFloat();
  vzErr = (float)dis.readFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+36];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeFloat(xErr);
  dos.writeFloat(yErr);
  dos.writeFloat(zErr);
  dos.writeFloat(rollErr);
  dos.writeFloat(pitchErr);
  dos.writeFloat(yawErr);
  dos.writeFloat(vxErr);
  dos.writeFloat(vyErr);
  dos.writeFloat(vzErr);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 36);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[42] = crcl;
  buffer[43] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_STATE_CORRECTION : " +   "  xErr="+xErr+  "  yErr="+yErr+  "  zErr="+zErr+  "  rollErr="+rollErr+  "  pitchErr="+pitchErr+  "  yawErr="+yawErr+  "  vxErr="+vxErr+  "  vyErr="+vyErr+  "  vzErr="+vzErr;}
}
