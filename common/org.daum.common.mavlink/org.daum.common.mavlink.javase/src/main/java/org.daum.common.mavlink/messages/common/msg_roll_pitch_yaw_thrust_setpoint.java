/**
 * Generated class : msg_roll_pitch_yaw_thrust_setpoint
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
 * Class msg_roll_pitch_yaw_thrust_setpoint
 * Setpoint in roll, pitch, yaw currently active on the system.
 **/
public class msg_roll_pitch_yaw_thrust_setpoint extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_ROLL_PITCH_YAW_THRUST_SETPOINT = 58;
  private static final long serialVersionUID = MAVLINK_MSG_ID_ROLL_PITCH_YAW_THRUST_SETPOINT;
  public msg_roll_pitch_yaw_thrust_setpoint(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_ROLL_PITCH_YAW_THRUST_SETPOINT;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 20;
}

  /**
   * Timestamp in milliseconds since system boot
   */
  public long time_boot_ms;
  /**
   * Desired roll angle in radians
   */
  public float roll;
  /**
   * Desired pitch angle in radians
   */
  public float pitch;
  /**
   * Desired yaw angle in radians
   */
  public float yaw;
  /**
   * Collective thrust, normalized to 0 .. 1
   */
  public float thrust;
/**
 * Decode message with raw data
 */
public void decode(LittleEndianDataInputStream dis) throws IOException {
  time_boot_ms = (int)dis.readInt()&0x00FFFFFFFF;
  roll = (float)dis.readFloat();
  pitch = (float)dis.readFloat();
  yaw = (float)dis.readFloat();
  thrust = (float)dis.readFloat();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+20];
   LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new ByteArrayOutputStream());
  dos.writeByte((byte)0xFE);
  dos.writeByte(length & 0x00FF);
  dos.writeByte(sequence & 0x00FF);
  dos.writeByte(sysId & 0x00FF);
  dos.writeByte(componentId & 0x00FF);
  dos.writeByte(messageType & 0x00FF);
  dos.writeInt((int)(time_boot_ms&0x00FFFFFFFF));
  dos.writeFloat(roll);
  dos.writeFloat(pitch);
  dos.writeFloat(yaw);
  dos.writeFloat(thrust);
  dos.flush();
  byte[] tmp = dos.toByteArray();
  for (int b=0; b<tmp.length; b++) buffer[b]=tmp[b];
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 20);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[26] = crcl;
  buffer[27] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_ROLL_PITCH_YAW_THRUST_SETPOINT : " +   "  time_boot_ms="+time_boot_ms+  "  roll="+roll+  "  pitch="+pitch+  "  yaw="+yaw+  "  thrust="+thrust;}
}
