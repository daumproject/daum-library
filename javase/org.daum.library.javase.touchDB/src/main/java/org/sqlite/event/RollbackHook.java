package org.sqlite.event;

import org.sqlite.callback.Callback;
import static org.sqlite.swig.SQLite3.sqlite3_clear_rollback_hook;
import static org.sqlite.swig.SQLite3.sqlite3_rollback_hook;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;

/**
 * ROLLBACK TRANSACTION hook class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/commit_hook.html">Commit And Rollback Notification Callbacks</a>
 * @see org.sqlite.jdbc.JdbcConnection#setRollbackHook(org.sqlite.event.RollbackHook)
 * @see org.sqlite.jdbc.JdbcConnection#clearRollbackHook()
 */
public abstract class RollbackHook extends Callback {

    /**
     * invoke sqlite3_rollback_hook() function and this object is registered in the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @see org.sqlite.Database#setRollbackHook(org.sqlite.event.RollbackHook)
     */
    @Override
    public final void register(SWIGTYPE_p_sqlite3 db) {
        sqlite3_rollback_hook(db, this);
    }

    /**
     * Unregister this object from the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @see #clear(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    @Override
    public final void unregister(SWIGTYPE_p_sqlite3 db) {
        clear(db);
    }

    /**
     * Unregister this object from the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @see org.sqlite.Database#clearRollbackHook()
     */
    public static final void clear(SWIGTYPE_p_sqlite3 db) {
        sqlite3_clear_rollback_hook(db);
    }

    /**
     * invoked whenever a transaction is rollbacked.
     */
    protected abstract void xRollback();
}
