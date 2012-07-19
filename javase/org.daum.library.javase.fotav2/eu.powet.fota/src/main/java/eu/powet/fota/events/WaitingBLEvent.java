package  eu.powet.fota.events;



import java.util.EventObject;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 10/07/12
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */
public class WaitingBLEvent extends FotaEvent {
    public WaitingBLEvent(eu.powet.fota.Fota src) {
        super(src);
    }
}
