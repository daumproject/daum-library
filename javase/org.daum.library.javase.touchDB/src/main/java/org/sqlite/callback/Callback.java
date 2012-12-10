package org.sqlite.callback;

import java.sql.SQLException;
import static org.sqlite.swig.SQLite3.delete_callback;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;

/**
 * SQLite Callback function class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/busy_handler.html">Register A Callback To Handle SQLITE_BUSY Errors</a>
 * @see <a href="http://sqlite.org/c3ref/commit_hook.html">Commit And Rollback Notification Callbacks</a>
 * @see <a href="http://sqlite.org/c3ref/collation_needed.html">Collation Needed Callbacks</a>
 * @see <a href="http://sqlite.org/c3ref/create_collation.html">Define New Collating Sequences</a>
 * @see <a href="http://sqlite.org/c3ref/create_function.html">Create Or Redefine SQL Functions</a>
 * @see <a href="http://sqlite.org/c3ref/progress_handler.html">Query Progress Callbacks</a>
 * @see <a href="http://sqlite.org/c3ref/set_authorizer.html">Compile-Time Authorization Callbacks</a>
 * @see <a href="http://sqlite.org/c3ref/update_hook.html">Data Change Notification Callbacks</a>
 * @see <a href="http://sqlite.org/c3ref/profile.html">Tracing And Profiling Functions</a>
 */
public abstract class Callback {

    /** pointer of myself */
    private volatile long _this = 0;

    /**
     * true if this object is registered.
     * @return true if the function is registered.
     */
    public boolean isRegistered() {
        return (_this != 0);
    }

    /**
     * Register this object to the database.
     * @param db the database handle.
     * @throws java.sql.SQLException if a database access error occurs.
     */
    public abstract void register(SWIGTYPE_p_sqlite3 db) throws SQLException;

    /**
     * Unregister this object from the database.
     * @param db the database handle.
     * @throws java.sql.SQLException if a database access error occurs.
     */
    public abstract void unregister(SWIGTYPE_p_sqlite3 db) throws SQLException;

    /**
     * delete this object from the heap memory.
     */
    public final void delete() {
        if (isRegistered()) {
            synchronized (this) {
                delete_callback(this);
            }
        }
    }
}
