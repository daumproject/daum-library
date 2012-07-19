package eu.powet.fota.events;


import eu.powet.fota.Fota;
import eu.powet.fota.Nativelib;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 29/02/12
 * Time: 10:28
 */
public class UploadedFotaEvent extends Nativelib {
    public UploadedFotaEvent(Fota src) {
        super(src);
    }
}
