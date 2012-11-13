/**
 * Generated class : MAVLinkMessageCoder
 * DO NOT MODIFY!
 **/
package org.daum.common.mavlink.messages;


import org.daum.common.mavlink.io.LittleEndianDataInputStream;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 12/11/12
 * Time: 18:22
 *
 * Class MAVLinkMessageCoder
 * Use to declarate encode and decode functions
 */
public abstract class MAVLinkMessageCoder {
  /**
   * Decode message with raw data
   */
  public abstract void decode(LittleEndianDataInputStream dis) throws IOException ;
  /**
   * Encode message in raw data
   */
  public abstract byte[] encode() throws IOException ;
}
