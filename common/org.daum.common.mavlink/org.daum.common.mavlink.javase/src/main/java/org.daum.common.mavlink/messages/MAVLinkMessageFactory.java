/**
 * Generated class : MAVLinkMessageFactory
 * DO NOT MODIFY!
 **/
package org.daum.common.mavlink.messages;

import org.daum.common.mavlink.IMAVLinkMessage;
import org.daum.common.mavlink.io.LittleEndianDataInputStream;
import org.daum.common.mavlink.messages.ardupilotmega.*;
import org.daum.common.mavlink.messages.common.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class MAVLinkMessageFactory implements IMAVLinkMessage, IMAVLinkMessageID
{
    public static MAVLinkMessage getMessage(int msgid, int sysId, int componentId, byte[] rawData) throws IOException
    {
        MAVLinkMessage msg=null;
        LittleEndianDataInputStream dis = new LittleEndianDataInputStream(new ByteArrayInputStream(rawData));
        switch(msgid) {
            case MAVLINK_MSG_ID_FENCE_FETCH_POINT:
                msg = new msg_fence_fetch_point(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_RADIO:
                msg = new msg_radio(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SAFETY_SET_ALLOWED_AREA:
                msg = new msg_safety_set_allowed_area(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_LOCAL_POSITION_NED:
                msg = new msg_local_position_ned(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_ATTITUDE:
                msg = new msg_attitude(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_DATA32:
                msg = new msg_data32(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MISSION_REQUEST:
                msg = new msg_mission_request(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_HEARTBEAT:
                msg = new msg_heartbeat(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT:
                msg = new msg_nav_controller_output(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_PARAM_SET:
                msg = new msg_param_set(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SET_QUAD_SWARM_LED_ROLL_PITCH_YAW_THRUST:
                msg = new msg_set_quad_swarm_led_roll_pitch_yaw_thrust(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_HIGHRES_IMU:
                msg = new msg_highres_imu(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MEMINFO:
                msg = new msg_meminfo(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SCALED_IMU:
                msg = new msg_scaled_imu(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_RC_CHANNELS_RAW:
                msg = new msg_rc_channels_raw(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_PARAM_REQUEST_LIST:
                msg = new msg_param_request_list(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_CHANGE_OPERATOR_CONTROL_ACK:
                msg = new msg_change_operator_control_ack(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_PARAM_VALUE:
                msg = new msg_param_value(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_HIL_CONTROLS:
                msg = new msg_hil_controls(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_LOCAL_POSITION_NED_SYSTEM_GLOBAL_OFFSET:
                msg = new msg_local_position_ned_system_global_offset(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_GPS_RAW_INT:
                msg = new msg_gps_raw_int(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SET_QUAD_MOTORS_SETPOINT:
                msg = new msg_set_quad_motors_setpoint(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SERVO_OUTPUT_RAW:
                msg = new msg_servo_output_raw(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SCALED_PRESSURE:
                msg = new msg_scaled_pressure(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_DIGICAM_CONTROL:
                msg = new msg_digicam_control(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_DEBUG_VECT:
                msg = new msg_debug_vect(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_CHANGE_OPERATOR_CONTROL:
                msg = new msg_change_operator_control(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MISSION_WRITE_PARTIAL_LIST:
                msg = new msg_mission_write_partial_list(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_AHRS:
                msg = new msg_ahrs(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SAFETY_ALLOWED_AREA:
                msg = new msg_safety_allowed_area(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_GPS_GLOBAL_ORIGIN:
                msg = new msg_gps_global_origin(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_RAW_PRESSURE:
                msg = new msg_raw_pressure(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SET_MAG_OFFSETS:
                msg = new msg_set_mag_offsets(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SET_MODE:
                msg = new msg_set_mode(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MISSION_ITEM_REACHED:
                msg = new msg_mission_item_reached(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_PING:
                msg = new msg_ping(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_LIMITS_STATUS:
                msg = new msg_limits_status(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_RC_CHANNELS_SCALED:
                msg = new msg_rc_channels_scaled(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_RC_CHANNELS_OVERRIDE:
                msg = new msg_rc_channels_override(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MISSION_REQUEST_LIST:
                msg = new msg_mission_request_list(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MISSION_SET_CURRENT:
                msg = new msg_mission_set_current(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_DATA64:
                msg = new msg_data64(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_RAW_IMU:
                msg = new msg_raw_imu(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_NAMED_VALUE_INT:
                msg = new msg_named_value_int(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_STATE_CORRECTION:
                msg = new msg_state_correction(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_WIND:
                msg = new msg_wind(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_GLOBAL_VISION_POSITION_ESTIMATE:
                msg = new msg_global_vision_position_estimate(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_OPTICAL_FLOW:
                msg = new msg_optical_flow(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MISSION_REQUEST_PARTIAL_LIST:
                msg = new msg_mission_request_partial_list(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_COMMAND_LONG:
                msg = new msg_command_long(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_AP_ADC:
                msg = new msg_ap_adc(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MANUAL_CONTROL:
                msg = new msg_manual_control(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_FENCE_STATUS:
                msg = new msg_fence_status(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SIMSTATE:
                msg = new msg_simstate(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MOUNT_STATUS:
                msg = new msg_mount_status(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_GLOBAL_POSITION_INT:
                msg = new msg_global_position_int(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MOUNT_CONTROL:
                msg = new msg_mount_control(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_FENCE_POINT:
                msg = new msg_fence_point(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MISSION_ACK:
                msg = new msg_mission_ack(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_DEBUG:
                msg = new msg_debug(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_PARAM_REQUEST_READ:
                msg = new msg_param_request_read(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_ATTITUDE_QUATERNION:
                msg = new msg_attitude_quaternion(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_DATA96:
                msg = new msg_data96(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_GLOBAL_POSITION_SETPOINT_INT:
                msg = new msg_global_position_setpoint_int(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_DIGICAM_CONFIGURE:
                msg = new msg_digicam_configure(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MOUNT_CONFIGURE:
                msg = new msg_mount_configure(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_HIL_RC_INPUTS_RAW:
                msg = new msg_hil_rc_inputs_raw(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SET_GLOBAL_POSITION_SETPOINT_INT:
                msg = new msg_set_global_position_setpoint_int(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MISSION_CLEAR_ALL:
                msg = new msg_mission_clear_all(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_ROLL_PITCH_YAW_SPEED_THRUST_SETPOINT:
                msg = new msg_roll_pitch_yaw_speed_thrust_setpoint(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MISSION_ITEM:
                msg = new msg_mission_item(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_NAMED_VALUE_FLOAT:
                msg = new msg_named_value_float(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_LOCAL_POSITION_SETPOINT:
                msg = new msg_local_position_setpoint(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SET_GPS_GLOBAL_ORIGIN:
                msg = new msg_set_gps_global_origin(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_HWSTATUS:
                msg = new msg_hwstatus(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SET_ROLL_PITCH_YAW_SPEED_THRUST:
                msg = new msg_set_roll_pitch_yaw_speed_thrust(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_VISION_SPEED_ESTIMATE:
                msg = new msg_vision_speed_estimate(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_STATUSTEXT:
                msg = new msg_statustext(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SYS_STATUS:
                msg = new msg_sys_status(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_VFR_HUD:
                msg = new msg_vfr_hud(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_VISION_POSITION_ESTIMATE:
                msg = new msg_vision_position_estimate(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SYSTEM_TIME:
                msg = new msg_system_time(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_DATA16:
                msg = new msg_data16(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MISSION_CURRENT:
                msg = new msg_mission_current(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SET_ROLL_PITCH_YAW_THRUST:
                msg = new msg_set_roll_pitch_yaw_thrust(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MISSION_COUNT:
                msg = new msg_mission_count(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SET_LOCAL_POSITION_SETPOINT:
                msg = new msg_set_local_position_setpoint(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_HIL_STATE:
                msg = new msg_hil_state(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_GPS_STATUS:
                msg = new msg_gps_status(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_DATA_STREAM:
                msg = new msg_data_stream(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_COMMAND_ACK:
                msg = new msg_command_ack(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SENSOR_OFFSETS:
                msg = new msg_sensor_offsets(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_SET_QUAD_SWARM_ROLL_PITCH_YAW_THRUST:
                msg = new msg_set_quad_swarm_roll_pitch_yaw_thrust(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_AUTH_KEY:
                msg = new msg_auth_key(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_MEMORY_VECT:
                msg = new msg_memory_vect(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_ROLL_PITCH_YAW_THRUST_SETPOINT:
                msg = new msg_roll_pitch_yaw_thrust_setpoint(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_REQUEST_DATA_STREAM:
                msg = new msg_request_data_stream(sysId, componentId);
                msg.decode(dis);
                break;
            case MAVLINK_MSG_ID_VICON_POSITION_ESTIMATE:
                msg = new msg_vicon_position_estimate(sysId, componentId);
                msg.decode(dis);
                break;
            default:
                System.out.println("Mavlink Factory Error : unknown MsgId : " + msgid);
        }
        return msg;
    }
}
