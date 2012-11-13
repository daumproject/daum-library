/**
 * Generated class : MAV_CMD_ACK
 * DO NOT MODIFY!
 **/
package org.daum.common.mavlink.messages;
/**
 * Interface MAV_CMD_ACK
 * ACK / NACK / ERROR values as a result of MAV_CMDs and for mission item transmission.
 **/
public interface MAV_CMD_ACK {
    /**
    /* Command / mission item is ok.
    */
    public final static int MAV_CMD_ACK_OK = -1;
    /**
    /* Generic error message if none of the other reasons fails or if no detailed error reporting is implemented.
    */
    public final static int MAV_CMD_ACK_ERR_FAIL = -1;
    /**
    /* The system is refusing to accept this command from this source / communication partner.
    */
    public final static int MAV_CMD_ACK_ERR_ACCESS_DENIED = -1;
    /**
    /* Command or mission item is not supported, other commands would be accepted.
    */
    public final static int MAV_CMD_ACK_ERR_NOT_SUPPORTED = -1;
    /**
    /* The coordinate frame of this command / mission item is not supported.
    */
    public final static int MAV_CMD_ACK_ERR_COORDINATE_FRAME_NOT_SUPPORTED = -1;
    /**
    /* The coordinate frame of this command is ok, but he coordinate values exceed the safety limits of this system. This is a generic error, please use the more specific error messages below if possible.
    */
    public final static int MAV_CMD_ACK_ERR_COORDINATES_OUT_OF_RANGE = -1;
    /**
    /* The X or latitude value is out of range.
    */
    public final static int MAV_CMD_ACK_ERR_X_LAT_OUT_OF_RANGE = -1;
    /**
    /* The Y or longitude value is out of range.
    */
    public final static int MAV_CMD_ACK_ERR_Y_LON_OUT_OF_RANGE = -1;
    /**
    /* The Z or altitude value is out of range.
    */
    public final static int MAV_CMD_ACK_ERR_Z_ALT_OUT_OF_RANGE = -1;
}
