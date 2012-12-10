package org.sqlite.udf;

import java.sql.SQLException;
import java.util.logging.Logger;
import org.sqlite.callback.NamedCallback;
import org.sqlite.jdbc.JdbcSQLException;
import static org.sqlite.swig.SQLite3Constants.SQLITE_OK;
import static org.sqlite.swig.SQLite3.sqlite3_create_function;
import static org.sqlite.swig.SQLite3.sqlite3_drop_function;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;

/**
 * User-Defined function class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/create_function.html">Create Or Redefine SQL Functions</a>
 * @see org.sqlite.jdbc.JdbcConnection#createFunction(org.sqlite.udf.Function)
 * @see org.sqlite.jdbc.JdbcConnection#dropFunction(org.sqlite.udf.Function)
 */
public abstract class Function extends NamedCallback {

    /**
     * invoke sqlite3_create_function() function and this object is registered in the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @throws java.sql.SQLException When the return value of the sqlite3_create_function() function is not SQLITE_OK.
     * @see org.sqlite.Database#createFunction(org.sqlite.udf.Function)
     */
    @Override
    public final void register(SWIGTYPE_p_sqlite3 db) throws SQLException {
        final int ret = sqlite3_create_function(db, this);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }

    /**
     * Unregister this object from the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @throws java.sql.SQLException When the return value of the sqlite3_create_function() function is not SQLITE_OK.
     * @see org.sqlite.Database#dropFunction(org.sqlite.udf.Function)
     */
    @Override
    public final void unregister(SWIGTYPE_p_sqlite3 db) throws SQLException {
        final int ret = sqlite3_drop_function(db, this);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }
    
    /** the number of arguments that the SQL function or aggregate takes */
    protected final int argc;
    
    /**
     * invoke Function(String, -1) constructor.
     * @param name the function name
     * @see org.sqlite.udf.Function#Function(String, int)
     */
    protected Function(String name) {
        this(name, -1);
    }
    
    /**
     * create Function object with SQLITE_UTF8.
     * @param name the function name
     * @param argc the number of arguments that the SQL function or aggregate takes.
     * @see org.sqlite.callback.NamedCallback#NamedCallback(String)
     */
    protected Function(String name, int argc) {
        super(name);
        this.argc = argc;
    }
    
    /**
     * create Function object.
     * @param name the function name
     * @param argc the number of arguments that the SQL function or aggregate takes.
     * @param enc the specifies what text encoding this function prefers for its parameters.
     * @see <a href="http://sqlite.org/c3ref/c_any.html">Text Encodings</a>
     * @see org.sqlite.callback.NamedCallback#NamedCallback(String, int)
     */
    protected Function(String name, int argc, int enc) {
        super(name, enc);
        this.argc = argc;
    }
    
    /**
     * Returns the number of arguments that the SQL function or aggregate takes.
     * @return the number of arguments that the SQL function or aggregate takes
     */
    public int getArgumentCount() {
        return argc;
    }
    
    /**
     * Called from the sqlite3_step() function.
     * @param context the sqlite3_context* value
     * @param argc the number of arguments
     * @param value the sqlite3_value** value
     * @see #xFunc(org.sqlite.udf.Context)
     */
    protected final void xFunc(long context, int argc, long value) {
        // TODO Mysaifu JVMのBug#11980が解決したらアクセス修飾子をprivateに戻すこと！
        // @see http://sourceforge.jp/tracker/index.php?func=detail&aid=11980&group_id=1890&atid=7027
        Context ctx = null;
        try {
            ctx = new Context(context, argc, value);
            xFunc(ctx);
            
        } catch (SQLException ex) {
            ctx.resultErrorCode(ex.getErrorCode());
            ctx.resultError(ex.getMessage());
            
        } catch (Throwable th) {
            if (ctx != null) {
                final String msg = th. toString();
                ctx.resultError((msg != null ? msg : "Unknown error."));
                
            } else {
                Logger.getLogger(Function.class.getName()).fine("Exception occurred: " + th.toString());
            }
        }
    }
    
    /**
     * Called from the sqlite3_step() function.
     * @param ctx sqlite3_context wrapper object
     * @throws java.sql.SQLException
     */
    protected abstract void xFunc(Context ctx) throws SQLException;
}
