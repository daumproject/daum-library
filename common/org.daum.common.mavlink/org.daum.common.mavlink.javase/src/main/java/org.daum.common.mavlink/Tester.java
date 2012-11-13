package org.daum.common.mavlink;

import org.daum.common.mavlink.handler.ByteFIFO;


import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 13/11/12
 * Time: 11:09
 * To change this template use File | Settings | File Templates.
 */

import org.daum.common.mavlink.handler.JMAVLinkReader;
import org.daum.common.mavlink.handler.MavLinkEvent;
import org.daum.common.mavlink.handler.MavLinkEventListener;
import org.kevoree.extra.kserial.SerialPort.*;

public class Tester {

    static  ByteFIFO fifo = new ByteFIFO();

    // Calculate a divisor from baud rate and base clock for FT232BM, FT2232C
    // and FT232LR
    // thanks to @titoi2
    private static int calcFT232bmBaudBaseToDiv(int baud, int base) {
        int divisor;
        divisor = (base / 16 / baud) | (((base / 2 / baud) & 4) != 0 ? 0x4000 // 0.5
                : ((base / 2 / baud) & 2) != 0 ? 0x8000 // 0.25
                : ((base / 2 / baud) & 1) != 0 ? 0xc000 // 0.125
                : 0);
        return divisor;
    }

    // Calculate a divisor from baud rate and base clock for FT2232H and FT232H
    // thanks to @yakagawa
    private static int calcFT232hBaudBaseToDiv(int baud, int base) {
        int divisor3, divisor;
        divisor = (base / 10 / baud);
        divisor3 = divisor * 8;
        divisor |= ((divisor3 & 4) != 0 ? 0x4000 // 0.5
                : (divisor3 & 2) != 0 ? 0x8000 // 0.25
                : (divisor3 & 1) != 0 ? 0xc000 // 0.125
                : 0);

        // divisor |= 0x00020000;
        divisor &= 0xffff;
        return divisor;
    }

    public static void  main(String argv[]) throws Exception {

                  System.out.println(calcFT232hBaudBaseToDiv(57600,48000000));
        System.out.println(calcFT232hBaudBaseToDiv(57600,120000000));


        JMAVLinkReader m  = new JMAVLinkReader(fifo);

        final SerialPort serial = new SerialPort("*", 57600);
        serial.open();

        serial.addEventListener(new SerialPortEventListener()
        {
            public void incomingDataEvent(SerialPortEvent evt)
            {
                try {
                    fifo.add(evt.read());
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            public void disconnectionEvent(SerialPortDisconnectionEvent evt)
            {
                System.out.println("device " + serial.getPort_name() + " is not connected anymore ");

            }

            @Override
            public void concurrentOpenEvent(SerialConcurrentOpenEvent evt) {

                System.out.println("Concurrent VM open serial port");

            }
        });

        m.addEventListener(new MavLinkEventListener() {
            @Override
            public void incomingMSG(MavLinkEvent evt) {
             System.out.println("EV : "+evt.getMsg());
            }
        });


        Thread.sleep(10000000);

    }
}
