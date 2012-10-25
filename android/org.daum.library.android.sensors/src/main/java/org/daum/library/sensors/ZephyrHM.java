package org.daum.library.sensors;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import zephyr.android.HxMBT.BTClient;
import zephyr.android.HxMBT.ZephyrProtocol;

import android.os.Bundle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 25/10/12
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "Android")
@Requires({
        @RequiredPort(name = "heart", type = PortType.MESSAGE,optional = true)
})
@DictionaryType({
        @DictionaryAttribute(name = "MAC", defaultValue = "", optional = false)
})
@ComponentType
public class ZephyrHM  extends AbstractComponentType {

    private BluetoothAdapter adapter = null;
    private  BTClient _bt;
    private ZephyrProtocol _protocol;
    private  NewConnectedListener _NConnListener;
    private final int HEART_RATE = 0x100;
    private final int INSTANT_SPEED = 0x101;

    @Start
    public void start()
    {

        System.out.println(BTClient.class.getName());



        /*
      UIServiceHandler.getUIService().getRootActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {

              ClassLoader ctx = Thread.currentThread().getContextClassLoader();
              Thread.currentThread().setContextClassLoader(ZephyrHM.class.getClassLoader());

              final  Handler Newhandler = new Handler(){
                  public void handleMessage(Message msg)
                  {
                      switch (msg.what)
                      {
                          case HEART_RATE:
                              String HeartRatetext = msg.getData().getString("HeartRate");

                              System.out.println("Heart Rate Info is "+ HeartRatetext);

                              break;

                          case INSTANT_SPEED:
                              String InstantSpeedtext = msg.getData().getString("InstantSpeed");


                              break;

                      }
                  }


              };

              IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
              UIServiceHandler.getUIService().getRootActivity().getApplicationContext().registerReceiver(new BTBroadcastReceiver(), filter);
              // Registering the BTBondReceiver in the application that the status of the receiver has changed to Paired
              IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
              UIServiceHandler.getUIService().getRootActivity().getApplicationContext().registerReceiver(new BTBondReceiver(), filter2);

              String BhMacID = "00:07:80:9D:8A:E8";
              //String BhMacID = "00:07:80:88:F6:BF";
              adapter = BluetoothAdapter.getDefaultAdapter();

              Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

              if (pairedDevices.size() > 0)
              {
                  for (BluetoothDevice device : pairedDevices)
                  {
                      if (device.getName().startsWith("HXM"))
                      {
                          BluetoothDevice btDevice = device;
                          BhMacID = btDevice.getAddress();
                          break;

                      }
                  }
              }

              //BhMacID = btDevice.getAddress();
              BluetoothDevice Device = adapter.getRemoteDevice(BhMacID);
              String DeviceName = Device.getName();
              _bt = new BTClient(adapter, BhMacID);
              _NConnListener = new NewConnectedListener(Newhandler,Newhandler);
              _bt.addConnectedEventListener(_NConnListener);


              if(_bt.IsConnected())
              {
                  _bt.start();

                  String ErrorText  = "Connected to HxM "+DeviceName;
                  System.out.println(ErrorText);
                  //Reset all the values to 0s
              }
              else
              {
                  String ErrorText  = "Unable to Connect !";
                  System.out.println(ErrorText);
              }

              Thread.currentThread().setContextClassLoader(ctx);

          }
      });
            */


    }

    @Stop
    public void stop()
    {
        /*This disconnects listener from acting on received messages*/
        _bt.removeConnectedEventListener(_NConnListener);
        /*Close the communication with the device & throw an exception if failure*/
        _bt.Close();

    }


    @Update
    public void update()
    {

    }


    private class BTBondReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
            Log.d("Bond state", "BOND_STATED = " + device.getBondState());
        }
    }
    private class BTBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BTIntent", intent.getAction());
            Bundle b = intent.getExtras();
            Log.d("BTIntent", b.get("android.bluetooth.device.extra.DEVICE").toString());
            Log.d("BTIntent", b.get("android.bluetooth.device.extra.PAIRING_VARIANT").toString());
            try {
                BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
                Method m = BluetoothDevice.class.getMethod("convertPinToBytes", new Class[] {String.class} );
                byte[] pin = (byte[])m.invoke(device, "1234");
                m = device.getClass().getMethod("setPin", new Class [] {pin.getClass()});
                Object result = m.invoke(device, pin);
                Log.d("BTTest", result.toString());
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



}
