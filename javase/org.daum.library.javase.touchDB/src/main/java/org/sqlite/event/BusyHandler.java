package org.sqlite.event;

import java.sql.SQLException;
import org.sqlite.callback.Callback;
import org.sqlite.jdbc.JdbcSQLException;
import static org.sqlite.swig.SQLite3Constants.SQLITE_OK;
import static org.sqlite.swig.SQLite3.sqlite3_busy_handler;
import static org.sqlite.swig.SQLite3.sqlite3_clear_busy_handler;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;

/**
 * SQLITE_BUSY Error Handler class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/busy_handler.html">Register A Callback To Handle SQLITE_BUSY Errors</a>
 * @see org.sqlite.jdbc.JdbcConnection#setBusyHandler(org.sqlite.event.BusyHandler)
 * @see org.sqlite.jdbc.JdbcConnection#clearBusyHandler()
 */
public abstract class BusyHandler extends Callback {

    /**
     * invoke sqlite3_busy_handler() function and this object is registered in the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @throws java.sql.SQLException When the return value of the sqlite3_busy_handler() function is not SQLITE_OK.
     * @see org.sqlite.Database#setBusyHandler(org.sqlite.event.BusyHandler)
     */
    @Override
    public final void register(SWIGTYPE_p_sqlite3 db) throws SQLException {
        final int ret = sqlite3_busy_handler(db, this);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }

    /**
     * Unregister this object from the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @throws java.sql.SQLException When the return value of the sqlite3_busy_handler() function is not SQLITE_OK.
     * @see org.sqlite.Database#clearBusyHandler()
     */
    @Override
    public final void unregister(SWIGTYPE_p_sqlite3 db) throws SQLException {
        final int ret = sqlite3_clear_busy_handler(db, this);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }

    /**
     * Called from the sqlite3_step() function.
     * @param count the number of times that the busy handler has been invoked for this locking event.
     * @return non-zero if the operation is continued. 0 if the operation is interrupted.
     */
    protected abstract int xBusy(int count);
}
