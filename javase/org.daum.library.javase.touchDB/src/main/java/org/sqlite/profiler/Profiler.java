package org.sqlite.profiler;

import org.sqlite.callback.Callback;
import static org.sqlite.swig.SQLite3.sqlite3_clear_profile;
import static org.sqlite.swig.SQLite3.sqlite3_profile;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;

/**
 * SQL statement profiling class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/profile.html">Tracing And Profiling Functions</a>
 * @see org.sqlite.jdbc.JdbcConnection#setProfiler(org.sqlite.profiler.Profiler)
 * @see org.sqlite.jdbc.JdbcConnection#clearProfiler()
 */
public abstract class Profiler extends Callback {

    /**
     * invoke sqlite3_profile() function and this object is registered in the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @see org.sqlite.Database#setProfiler(org.sqlite.profiler.Profiler)
     */
    @Override
    public final void register(SWIGTYPE_p_sqlite3 db) {
        sqlite3_profile(db, this);
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
     * @see org.sqlite.Database#clearProfiler()
     */
    public static final void clear(SWIGTYPE_p_sqlite3 db) {
        sqlite3_clear_profile(db);
    }

    /**
     * Called from the sqlite3_step() function.
     * @param sql SQL to be evaluated
     * @param elapseTime an estimate of wall-clock time of how long that statement took to execute
     */
    protected abstract void xProfile(String sql, long elapseTime);
}
