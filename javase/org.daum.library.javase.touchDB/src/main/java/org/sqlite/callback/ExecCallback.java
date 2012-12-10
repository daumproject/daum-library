package org.sqlite.callback;

import org.sqlite.swig.SWIGTYPE_p_sqlite3;

/**
 * Callback function class for sqlite3_exec() function.
 * @author calico
 * @see <a href="http://www.sqlite.org/c3ref/exec.html">One-Step Query Execution Interface</a>
 * @see org.sqlite.Database#execute(String, org.sqlite.callback.ExecCallback, SWIGTYPE_p_p_char)
 */
public abstract class ExecCallback extends Callback {

    /**
     * Not supported.
     * @param db the database handle.
     */
    @Override
    public final void register(SWIGTYPE_p_sqlite3 db) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supported.
     * @param db the database handle.
     */
    @Override
    public final void unregister(SWIGTYPE_p_sqlite3 db) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * invoked once for each row of any query results produced by the SQL statements.
     * @param values an array of strings holding the values for each column.
     * @param columnNames an array of strings holding the names of each column.
     * @return 0 if the operation is continued. non-zero if the operation is aborted.
     */
    protected abstract int xCallback(String[] values, String[] columnNames);
}
