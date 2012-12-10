package org.sqlite.auth;

import java.sql.SQLException;
import org.sqlite.callback.Callback;
import org.sqlite.jdbc.JdbcSQLException;
import static org.sqlite.swig.SQLite3Constants.SQLITE_OK;
import static org.sqlite.swig.SQLite3.sqlite3_clear_authorizer;
import static org.sqlite.swig.SQLite3.sqlite3_set_authorizer;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;

/**
 * Compile-Time Authorization class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/set_authorizer.html">Compile-Time Authorization Callbacks</a>
 * @see org.sqlite.jdbc.JdbcConnection#setAuthorizer(org.sqlite.auth.Authorizer)
 * @see org.sqlite.jdbc.JdbcConnection#clearAuthorizer()
 */
public abstract class Authorizer extends Callback {

    /**
     * invoke sqlite3_set_authorizer() function and this object is registered in the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @throws java.sql.SQLException When the return value of the sqlite3_set_authorizer() function is not SQLITE_OK.
     * @see org.sqlite.Database#setAuthorizer(org.sqlite.auth.Authorizer)
     */
    @Override
    public final void register(SWIGTYPE_p_sqlite3 db) throws SQLException {
        final int ret = sqlite3_set_authorizer(db, this);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }

    /**
     * Unregister this object from the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @throws java.sql.SQLException When the return value of the sqlite3_set_authorizer() function is not SQLITE_OK.
     * @see org.sqlite.Database#clearAuthorizer()
     */
    @Override
    public final void unregister(SWIGTYPE_p_sqlite3 db) throws SQLException {
        final int ret = sqlite3_clear_authorizer(db, this);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }

    /**
     * Called from the sqlite3_prepare(), sqlite3_prepare_v2() function.
     * @param action action code
     * @param str1 see alto 'Authorizer Action Codes' page
     * @param str2 see alto 'Authorizer Action Codes' page
     * @param database database name
     * @param triggerOrView trigger or view name
     * @return SQLITE_OK or SQLITE_DENY or SQLITE_IGNORE
     * @see <a href="http://sqlite.org/c3ref/set_authorizer.html">Compile-Time Authorization Callbacks</a>
     * @see <a href="http://sqlite.org/c3ref/c_alter_table.html">Authorizer Action Codes</a>
     * @see <a href="http://sqlite.org/c3ref/c_deny.html">Authorizer Return Codes</a>
     */
    protected abstract int xAuth(int action, String str1, String str2, String database, String triggerOrView);
}
