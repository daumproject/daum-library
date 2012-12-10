package org.sqlite.event;

import org.sqlite.callback.Callback;
import static org.sqlite.swig.SQLite3.sqlite3_clear_update_hook;
import static org.sqlite.swig.SQLite3.sqlite3_update_hook;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;

/**
 * INSERT, UPDATE, DELETE hook class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/update_hook.html">Data Change Notification Callbacks</a>
 * @see org.sqlite.jdbc.JdbcConnection#setUpdateHook(org.sqlite.event.UpdateHook)
 * @see org.sqlite.jdbc.JdbcConnection#clearUpdateHook()
 */
public abstract class UpdateHook extends Callback {

    /**
     * invoke sqlite3_update_hook() function and this object is registered in the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @see org.sqlite.Database#setUpdateHook(org.sqlite.event.UpdateHook)
     */
    @Override
    public final void register(SWIGTYPE_p_sqlite3 db) {
        sqlite3_update_hook(db, this);
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
     * @see org.sqlite.Database#clearUpdateHook()
     */
    public static final void clear(SWIGTYPE_p_sqlite3 db) {
        sqlite3_clear_update_hook(db);
    }

    /**
     * invoke when a row is updated, inserted or deleted.
     * @param action SQLITE_INSERT or SQLITE_DELETE or SQLITE_UPDATE
     * @param dbName database name
     * @param tableName table name
     * @param rowId rowid
     * @see <a href="http://sqlite.org/c3ref/c_alter_table.html">Authorizer Action Codes</a>
     */
    protected abstract void xUpdate(int action, String dbName, String tableName, long rowId);
}
