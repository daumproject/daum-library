package org.daum.library.android.sitac.drone;
  /*
import android.app.Activity;
import eu.powet.android.serialUSB.*;
import org.daum.common.mavlink.handler.ByteFIFO;
import org.daum.common.mavlink.handler.JMAVLinkReader;
import org.daum.common.mavlink.messages.MAVLinkMessage;

import java.io.IOException;
*/
/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 13/11/12
 * Time: 10:11
 * To change this template use File | Settings | File Templates.
 */
public class SerialMavLinkHandler implements IMavLink
{
    /*
    private ByteFIFO fifo = new ByteFIFO();
    private JMAVLinkReader parser = null;
    private ISerial usb_serial=null;

    public SerialMavLinkHandler(Activity ctx)
    {
        parser = new JMAVLinkReader(fifo);
        usb_serial = new UsbSerial(UsbDeviceID.FT232RL,57600,ctx);
        usb_serial.setBaudrate(57600);
        usb_serial.open();

        usb_serial.addEventListener(new SerialListener() {
            @Override
            public void incomingDataEvent(SerialEvent serialEvent) {
                try
                {
                    fifo.add(serialEvent.read());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void write(MAVLinkMessage msg) throws IOException
    {
        usb_serial.write(msg.encode());
    }



    public void shutdown()
    {
        try
        {
            if(usb_serial != null)
                usb_serial.close();
            if(parser != null);
              parser.shutdown();

        }   catch (Exception e){
         // ignore
        }
    }

    @Override
    public JMAVLinkReader getMavLinkReader()
    {
        return parser;
    }  */

}


