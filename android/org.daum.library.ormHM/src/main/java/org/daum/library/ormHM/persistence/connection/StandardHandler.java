package org.daum.library.ormHM.persistence.connection;

import org.daum.library.ormHM.persistence.OrhmID;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 14:33
 */
public interface StandardHandler {
    public void save(OrhmID id,Object bean);
    public Object get(OrhmID id);
    public Object lock(OrhmID id);
    public void unlock(OrhmID id);

}
