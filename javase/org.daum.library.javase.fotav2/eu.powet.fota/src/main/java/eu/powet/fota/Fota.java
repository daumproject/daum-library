package eu.powet.fota;


import eu.powet.fota.api.FotaEventListener;
import eu.powet.fota.api.IFota;
import eu.powet.fota.events.FotaEvent;
import eu.powet.fota.events.UploadedFotaEvent;
import eu.powet.fota.utils.Board;
import eu.powet.fota.utils.Constants;
import eu.powet.fota.utils.FotaException;
import eu.powet.fota.utils.Helpers;

import javax.swing.event.EventListenerList;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 06/02/12
 * Time: 09:56
 */
public class Fota implements IFota {

    protected EventListenerList listenerList = new EventListenerList();
    private String deviceport = "";
    private int devicetype=-1;
    private int program_size=-1;
    private  long start;
    private  long duree;
    private Nativelib nativelib;

    public Fota(String deviceport,Board type) throws FotaException
    {
        if(deviceport.equals("*"))
        {
            if(Helpers.getPortIdentifiers().size() == 0){ throw new FotaException("not board available");   }else
            {
                deviceport = Helpers.getPortIdentifiers().get(0);
            }
        }
        nativelib = new Nativelib(this);
        // register callback
        nativelib.register();
        this.deviceport = deviceport;
        this.devicetype = Integer.parseInt(Constants.boards.get(type.toString()).toString());
    }


    public void dispatchEvent(int evt)
    {
        System.out.println("Nativelib call "+evt);
    }



    public void addEventListener (FotaEventListener listener) {
        listenerList.add(FotaEventListener.class, listener);
    }

    public void removeEventListener (FotaEventListener listener) {
        listenerList.remove(FotaEventListener.class, listener);
    }

    @Override
    public void close()
    {
        nativelib.close_flash();
    }

    public void fireFlashEvent (FotaEvent evt)
    {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2)
        {
            if (evt instanceof UploadedFotaEvent)
            {
                ((FotaEventListener) listeners[i + 1]).completedEvent(evt);
            }
            else
            {
                ((FotaEventListener) listeners[i + 1]).progressEvent(evt);
            }
        }
    }

    @Override
    public void upload(String path_hex_array) throws FotaException
    {
        try
        {

            start= System.currentTimeMillis();

            program_size = nativelib.write_on_the_air_program(deviceport,devicetype,path_hex_array);

            if(program_size < 0)
            {
                throw new FotaException("Empty");
            }
        }catch (Exception e)
        {
            System.out.print("upload "+e);
        }
    }

    public int getProgram_size() {
        return program_size;
    }



    /**
     * durée en seconde
     * @return
     */
    public long getDuree() {
        return       (  duree = System.currentTimeMillis() - start)  / 1000;
    }


}
