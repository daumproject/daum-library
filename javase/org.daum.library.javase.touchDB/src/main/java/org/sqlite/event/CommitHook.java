package org.sqlite.event;

import org.sqlite.callback.Callback;
import static org.sqlite.swig.SQLite3.sqlite3_clear_commit_hook;
import static org.sqlite.swig.SQLite3.sqlite3_commit_hook;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;

/**
 * COMMIT TRANSACTION hook class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/commit_hook.html">Commit And Rollback Notification Callbacks</a>
 * @see org.sqlite.jdbc.JdbcConnection#setCommitHook(org.sqlite.event.CommitHook)
 * @see org.sqlite.jdbc.JdbcConnection#clearCommitHook()
 */
public abstract class CommitHook extends Callback {

    /**
     * invoke sqlite3_commit_hook() function and this object is registered in the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @see org.sqlite.Database#setCommitHook(org.sqlite.event.CommitHook)
     */
    @Override
    public final void register(SWIGTYPE_p_sqlite3 db) {
        sqlite3_commit_hook(db, this);
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
     * @see org.sqlite.Database#clearCommitHook()
     */
    public static final void clear(SWIGTYPE_p_sqlite3 db) {
        sqlite3_clear_commit_hook(db);
    }
    
    /**
     * invoked whenever a transaction is committed.
     * @return 0 if the transaction is commited. non-zero if the transaction is rollbacked.
     */
    protected abstract int xCommit();
}
