package org.sqlite.profiler;

import org.sqlite.callback.Callback;
import static org.sqlite.swig.SQLite3.sqlite3_clear_trace;
import static org.sqlite.swig.SQLite3.sqlite3_trace;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;

/**
 * SQL statement tracing class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/profile.html">Tracing And Profiling Functions</a>
 * @see org.sqlite.jdbc.JdbcConnection#setTracer(org.sqlite.profiler.Tracer)
 * @see org.sqlite.jdbc.JdbcConnection#clearTracer()
 */
public abstract class Tracer extends Callback {

    /**
     * invoke sqlite3_trace() function and this object is registered in the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @see org.sqlite.Database#setTracer(org.sqlite.profiler.Tracer)
     */
    @Override
    public final void register(SWIGTYPE_p_sqlite3 db) {
        sqlite3_trace(db, this);
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
     * @see org.sqlite.Database#clearTracer()
     */
    public static final void clear(SWIGTYPE_p_sqlite3 db) {
        sqlite3_clear_trace(db);
    }

    /**
     * Called from the sqlite3_step() function.
     * @param sql SQL to be evaluated
     */
    protected abstract void xTrace(String sql);
}
