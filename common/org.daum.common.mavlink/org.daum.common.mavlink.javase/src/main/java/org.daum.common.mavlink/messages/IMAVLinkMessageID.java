/**
 * Generated class : IMAVLinkMessageID
 * DO NOT MODIFY!
 **/
package org.daum.common.mavlink.messages;
/**
 * Interface IMAVLinkMessageId
 * Generate al MAVLink message Id in an interface
 **/
public interface IMAVLinkMessageID {
  public static int MAVLINK_MSG_ID_FENCE_FETCH_POINT = 161;
  public static int MAVLINK_MSG_ID_RADIO = 166;
  public static int MAVLINK_MSG_ID_SAFETY_SET_ALLOWED_AREA = 54;
  public static int MAVLINK_MSG_ID_LOCAL_POSITION_NED = 32;
  public static int MAVLINK_MSG_ID_ATTITUDE = 30;
  public static int MAVLINK_MSG_ID_DATA32 = 170;
  public static int MAVLINK_MSG_ID_MISSION_REQUEST = 40;
  public static int MAVLINK_MSG_ID_HEARTBEAT = 0;
  public static int MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT = 62;
  public static int MAVLINK_MSG_ID_PARAM_SET = 23;
  public static int MAVLINK_MSG_ID_SET_QUAD_SWARM_LED_ROLL_PITCH_YAW_THRUST = 63;
  public static int MAVLINK_MSG_ID_HIGHRES_IMU = 105;
  public static int MAVLINK_MSG_ID_MEMINFO = 152;
  public static int MAVLINK_MSG_ID_SCALED_IMU = 26;
  public static int MAVLINK_MSG_ID_RC_CHANNELS_RAW = 35;
  public static int MAVLINK_MSG_ID_PARAM_REQUEST_LIST = 21;
  public static int MAVLINK_MSG_ID_CHANGE_OPERATOR_CONTROL_ACK = 6;
  public static int MAVLINK_MSG_ID_PARAM_VALUE = 22;
  public static int MAVLINK_MSG_ID_HIL_CONTROLS = 91;
  public static int MAVLINK_MSG_ID_LOCAL_POSITION_NED_SYSTEM_GLOBAL_OFFSET = 89;
  public static int MAVLINK_MSG_ID_GPS_RAW_INT = 24;
  public static int MAVLINK_MSG_ID_SET_QUAD_MOTORS_SETPOINT = 60;
  public static int MAVLINK_MSG_ID_SERVO_OUTPUT_RAW = 36;
  public static int MAVLINK_MSG_ID_SCALED_PRESSURE = 29;
  public static int MAVLINK_MSG_ID_DIGICAM_CONTROL = 155;
  public static int MAVLINK_MSG_ID_DEBUG_VECT = 250;
  public static int MAVLINK_MSG_ID_CHANGE_OPERATOR_CONTROL = 5;
  public static int MAVLINK_MSG_ID_MISSION_WRITE_PARTIAL_LIST = 38;
  public static int MAVLINK_MSG_ID_AHRS = 163;
  public static int MAVLINK_MSG_ID_SAFETY_ALLOWED_AREA = 55;
  public static int MAVLINK_MSG_ID_GPS_GLOBAL_ORIGIN = 49;
  public static int MAVLINK_MSG_ID_RAW_PRESSURE = 28;
  public static int MAVLINK_MSG_ID_SET_MAG_OFFSETS = 151;
  public static int MAVLINK_MSG_ID_SET_MODE = 11;
  public static int MAVLINK_MSG_ID_MISSION_ITEM_REACHED = 46;
  public static int MAVLINK_MSG_ID_PING = 4;
  public static int MAVLINK_MSG_ID_LIMITS_STATUS = 167;
  public static int MAVLINK_MSG_ID_RC_CHANNELS_SCALED = 34;
  public static int MAVLINK_MSG_ID_RC_CHANNELS_OVERRIDE = 70;
  public static int MAVLINK_MSG_ID_MISSION_REQUEST_LIST = 43;
  public static int MAVLINK_MSG_ID_MISSION_SET_CURRENT = 41;
  public static int MAVLINK_MSG_ID_DATA64 = 171;
  public static int MAVLINK_MSG_ID_RAW_IMU = 27;
  public static int MAVLINK_MSG_ID_NAMED_VALUE_INT = 252;
  public static int MAVLINK_MSG_ID_STATE_CORRECTION = 64;
  public static int MAVLINK_MSG_ID_WIND = 168;
  public static int MAVLINK_MSG_ID_GLOBAL_VISION_POSITION_ESTIMATE = 101;
  public static int MAVLINK_MSG_ID_OPTICAL_FLOW = 100;
  public static int MAVLINK_MSG_ID_MISSION_REQUEST_PARTIAL_LIST = 37;
  public static int MAVLINK_MSG_ID_COMMAND_LONG = 76;
  public static int MAVLINK_MSG_ID_AP_ADC = 153;
  public static int MAVLINK_MSG_ID_MANUAL_CONTROL = 69;
  public static int MAVLINK_MSG_ID_FENCE_STATUS = 162;
  public static int MAVLINK_MSG_ID_SIMSTATE = 164;
  public static int MAVLINK_MSG_ID_MOUNT_STATUS = 158;
  public static int MAVLINK_MSG_ID_GLOBAL_POSITION_INT = 33;
  public static int MAVLINK_MSG_ID_MOUNT_CONTROL = 157;
  public static int MAVLINK_MSG_ID_FENCE_POINT = 160;
  public static int MAVLINK_MSG_ID_MISSION_ACK = 47;
  public static int MAVLINK_MSG_ID_DEBUG = 254;
  public static int MAVLINK_MSG_ID_PARAM_REQUEST_READ = 20;
  public static int MAVLINK_MSG_ID_ATTITUDE_QUATERNION = 31;
  public static int MAVLINK_MSG_ID_DATA96 = 172;
  public static int MAVLINK_MSG_ID_GLOBAL_POSITION_SETPOINT_INT = 52;
  public static int MAVLINK_MSG_ID_DIGICAM_CONFIGURE = 154;
  public static int MAVLINK_MSG_ID_MOUNT_CONFIGURE = 156;
  public static int MAVLINK_MSG_ID_HIL_RC_INPUTS_RAW = 92;
  public static int MAVLINK_MSG_ID_SET_GLOBAL_POSITION_SETPOINT_INT = 53;
  public static int MAVLINK_MSG_ID_MISSION_CLEAR_ALL = 45;
  public static int MAVLINK_MSG_ID_ROLL_PITCH_YAW_SPEED_THRUST_SETPOINT = 59;
  public static int MAVLINK_MSG_ID_MISSION_ITEM = 39;
  public static int MAVLINK_MSG_ID_NAMED_VALUE_FLOAT = 251;
  public static int MAVLINK_MSG_ID_LOCAL_POSITION_SETPOINT = 51;
  public static int MAVLINK_MSG_ID_SET_GPS_GLOBAL_ORIGIN = 48;
  public static int MAVLINK_MSG_ID_HWSTATUS = 165;
  public static int MAVLINK_MSG_ID_SET_ROLL_PITCH_YAW_SPEED_THRUST = 57;
  public static int MAVLINK_MSG_ID_VISION_SPEED_ESTIMATE = 103;
  public static int MAVLINK_MSG_ID_STATUSTEXT = 253;
  public static int MAVLINK_MSG_ID_SYS_STATUS = 1;
  public static int MAVLINK_MSG_ID_VFR_HUD = 74;
  public static int MAVLINK_MSG_ID_VISION_POSITION_ESTIMATE = 102;
  public static int MAVLINK_MSG_ID_SYSTEM_TIME = 2;
  public static int MAVLINK_MSG_ID_DATA16 = 169;
  public static int MAVLINK_MSG_ID_MISSION_CURRENT = 42;
  public static int MAVLINK_MSG_ID_SET_ROLL_PITCH_YAW_THRUST = 56;
  public static int MAVLINK_MSG_ID_MISSION_COUNT = 44;
  public static int MAVLINK_MSG_ID_SET_LOCAL_POSITION_SETPOINT = 50;
  public static int MAVLINK_MSG_ID_HIL_STATE = 90;
  public static int MAVLINK_MSG_ID_GPS_STATUS = 25;
  public static int MAVLINK_MSG_ID_DATA_STREAM = 67;
  public static int MAVLINK_MSG_ID_COMMAND_ACK = 77;
  public static int MAVLINK_MSG_ID_SENSOR_OFFSETS = 150;
  public static int MAVLINK_MSG_ID_SET_QUAD_SWARM_ROLL_PITCH_YAW_THRUST = 61;
  public static int MAVLINK_MSG_ID_AUTH_KEY = 7;
  public static int MAVLINK_MSG_ID_MEMORY_VECT = 249;
  public static int MAVLINK_MSG_ID_ROLL_PITCH_YAW_THRUST_SETPOINT = 58;
  public static int MAVLINK_MSG_ID_REQUEST_DATA_STREAM = 66;
  public static int MAVLINK_MSG_ID_VICON_POSITION_ESTIMATE = 104;
}
