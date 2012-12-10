package org.sqlite.event;

import org.sqlite.callback.Callback;
import static org.sqlite.swig.SQLite3.sqlite3_clear_progress_handler;
import static org.sqlite.swig.SQLite3.sqlite3_progress_handler;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;

/**
 * Query Progress Callbacks class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/progress_handler.html">Query Progress Callbacks</a>
 * @see org.sqlite.jdbc.JdbcConnection#setProgressHandler(org.sqlite.event.ProgressHandler)
 * @see org.sqlite.jdbc.JdbcConnection#clearProgressHandler()
 */
public abstract class ProgressHandler extends Callback {

    /**
     * invoke sqlite3_progress_handler() function and this object is registered in the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @see org.sqlite.Database#setProgressHandler(org.sqlite.event.ProgressHandler)
     */
    @Override
    public final void register(SWIGTYPE_p_sqlite3 db) {
        sqlite3_progress_handler(db, this);
    }

    /**
     * Unregister this object from the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @see org.sqlite.Database#clearProgressHandler()
     */
    @Override
    public final void unregister(SWIGTYPE_p_sqlite3 db) {
        sqlite3_clear_progress_handler(db, this);
    }

    /** the number of opcodes for progress callback */
    private final int opecodes;
    
    /**
     * default constructor.
     * @param opecodes the number of opcodes for progress callback
     */
    public ProgressHandler(int opecodes) {
        if (opecodes < 1) {
            throw new IllegalArgumentException("Should opecodes is greater than or equal to 1.");
        }
        this.opecodes = opecodes;
    }
    
    /**
     * Returns the number of opcodes for progress callback
     * @return the number of opcodes for progress callback
     */
    public int getOpCodes() {
        return opecodes;
    }
    
    /**
     * Called from the sqlite3_exec(), sqlite3_step(), sqlite3_get_table() function.
     * @return 0 if the operation is continued. non-zero if the operation is interrupted.
     */
    protected abstract int xProgress();
}
