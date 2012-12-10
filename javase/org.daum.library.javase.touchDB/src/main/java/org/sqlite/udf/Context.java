package org.sqlite.udf;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import org.sqlite.jdbc.JdbcSQLException;
import org.sqlite.swig.SQLite3;
import static org.sqlite.swig.SQLite3.get_p_sqlite3_value;
import static org.sqlite.swig.SQLite3.new_p_p_sqlite3_value;
import static org.sqlite.swig.SQLite3.new_p_sqlite3_context;
import static org.sqlite.swig.SQLite3.sqlite3_context_db_handle;
import static org.sqlite.swig.SQLite3.sqlite3_get_auxdata;
import static org.sqlite.swig.SQLite3.sqlite3_result_blob;
import static org.sqlite.swig.SQLite3.sqlite3_result_double;
import static org.sqlite.swig.SQLite3.sqlite3_result_error;
import static org.sqlite.swig.SQLite3.sqlite3_result_error_code;
import static org.sqlite.swig.SQLite3.sqlite3_result_error_nomem;
import static org.sqlite.swig.SQLite3.sqlite3_result_error_toobig;
import static org.sqlite.swig.SQLite3.sqlite3_result_int;
import static org.sqlite.swig.SQLite3.sqlite3_result_int64;
import static org.sqlite.swig.SQLite3.sqlite3_result_null;
import static org.sqlite.swig.SQLite3.sqlite3_result_text;
import static org.sqlite.swig.SQLite3.sqlite3_result_value;
import static org.sqlite.swig.SQLite3.sqlite3_result_zeroblob;
import static org.sqlite.swig.SQLite3.sqlite3_set_auxdata;
import static org.sqlite.swig.SQLite3.sqlite3_value_blob;
import static org.sqlite.swig.SQLite3.sqlite3_value_blob_by_bytes;
import static org.sqlite.swig.SQLite3.sqlite3_value_bytes;
import static org.sqlite.swig.SQLite3.sqlite3_value_double;
import static org.sqlite.swig.SQLite3.sqlite3_value_int;
import static org.sqlite.swig.SQLite3.sqlite3_value_int64;
import static org.sqlite.swig.SQLite3.sqlite3_value_text;
import static org.sqlite.swig.SQLite3.sqlite3_value_type;
import static org.sqlite.swig.SQLite3.sqlite3_value_numeric_type;
import static org.sqlite.swig.SQLite3Constants.SQLITE_RANGE;
import org.sqlite.swig.SWIGTYPE_p_Mem;
import org.sqlite.swig.SWIGTYPE_p_p_Mem;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;
import org.sqlite.swig.SWIGTYPE_p_sqlite3_context;
import org.sqlite.swig.SWIGTYPE_p_void;

/**
 * sqlite3_context and sqlite3_value wrapper class.<br/>
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/context.html">SQL Function Context Object</a>
 * @see <a href="http://sqlite.org/c3ref/value.html">Dynamically Typed Value Object</a>
 */
public class Context {
    private final SWIGTYPE_p_sqlite3_context ctx;
    private final int argc;
    private final SWIGTYPE_p_p_Mem value;

    /**
     * invoke Context(long, 0, 0) constructor.
     * @param ctx the sqlite3_context* value
     */
    public Context(long ctx) {
        this(ctx, 0, 0);
    }
    
    /**
     * invoke Context(SWIGTYPE_p_sqlite3_context, int, SWIGTYPE_p_p_Mem) constructor.
     * @param ctx the sqlite3_context* value
     * @param argc the number of arguments
     * @param value the sqlite3_value** value
     */
    public Context(long ctx, int argc, long value) {
        this(new_p_sqlite3_context(ctx), argc, (value != 0 ? new_p_p_sqlite3_value(value) : null));
    }
    
    /**
     * create Context object.
     * @param ctx the sqlite3_context* wrapper object
     * @param argc the number of arguments
     * @param value the sqlite3_value** wrapper object
     */
    public Context(SWIGTYPE_p_sqlite3_context ctx, int argc, SWIGTYPE_p_p_Mem value) {
        this.ctx = ctx;
        this.argc = argc;
        this.value = value;
    }
    
    /**
     * Returns the number of arguments.
     * @return the number of arguments
     */
    public int getArgumentCount() {
        return argc;
    }

    /**
     * true if the getArgumentCount() is greater than 0, and the sqlite3_value** wrapper object is not null.
     * @return true if the getArgumentCount() is greater than 0, and the sqlite3_value** wrapper object is not null.
     */
    public boolean hasValues() {
        return (argc > 0 && value != null);
    }
    
    /**
     * invoke sqlite3_result_blob() function.
     * @param x the result value
     * @param bytes the number of bytes
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(SWIGTYPE_p_void x, int bytes) {
        sqlite3_result_blob(ctx, x, bytes);
    }

    /**
     * invoke sqlite3_result_blob() function.
     * @param x the result value
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(byte[] x) {
        result(x, x.length);
    }

    /**
     * invoke sqlite3_result_blob() function.
     * @param x the result value
     * @param len the number of bytes
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(byte[] x, int len) {
        sqlite3_result_blob(ctx, x, len);
    }
    
    /**
     * invoke sqlite3_result_double() function.
     * @param x the result value
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(double x) {
        sqlite3_result_double(ctx, x);
    }
    
    /**
     * invoke sqlite3_result_int() function.
     * @param x the result value
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(int x) {
        sqlite3_result_int(ctx, x);
    }
    
    /**
     * invoke sqlite3_result_int64() function.
     * @param x the result value
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(long x) {
        sqlite3_result_int64(ctx, x);
    }
    
    /**
     * invoke sqlite3_result_int() function.
     * @param x the result value.
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(boolean x) {
        sqlite3_result_int(ctx, (x ? 1 : 0));
    }
    
    /**
     * invoke sqlite3_result_text() function.
     * @param x the result value
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(String x) {
        sqlite3_result_text(ctx, x);
    }
    
    /**
     * invoke sqlite3_result_value() function.
     * @param x the result value
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(SWIGTYPE_p_Mem x) {
        sqlite3_result_value(ctx, x);
    }
    
    /**
     * invoke sqlite3_result_text() function.
     * @param x the result value
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(Date x) {
        sqlite3_result_text(ctx, SQLite3.format(x));
    }
    
    /**
     * invoke sqlite3_result_text() function.
     * @param x the result value
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(Time x) {
        sqlite3_result_text(ctx, SQLite3.format(x));
    }
    
    /**
     * invoke sqlite3_result_text() function.
     * @param x the result value
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void result(Timestamp x) {
        sqlite3_result_text(ctx, SQLite3.format(x));
    }
    
    /**
     * invoke sqlite3_result_error() function.
     * @param x the result value
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void resultError(String x) {
        sqlite3_result_error(ctx, x);
    }
    
    /**
     * invoke sqlite3_result_null() function.
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void resultNull() {
        sqlite3_result_null(ctx);
    }
    
    /**
     * invoke sqlite3_result_error_code() function.
     * @param x the result error code
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void resultErrorCode(int x) {
        sqlite3_result_error_code(ctx, x);
    }
    
    /**
     * invoke sqlite3_result_error_nomem() function.
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void resultErrorNoMemory() {
        sqlite3_result_error_nomem(ctx);
    }
    
    /**
     * invoke sqlite3_result_error_toobig() function.
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void resultErrorTooBig() {
        sqlite3_result_error_toobig(ctx);
    }
    
    /**
     * invoke sqlite3_result_zeroblob() function.
     * @param bytes the number of bytes
     * @see <a href="http://sqlite.org/c3ref/result_blob.html">Setting The Result Of An SQL Function</a>
     */
    public void resultZeroBlob(int bytes) {
        sqlite3_result_zeroblob(ctx, bytes);
    }
    
    /**
     * invoke sqlite3_value_blob() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return pointer of BLOB object
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     */
    public SWIGTYPE_p_void getBlob(int parameterIndex) throws SQLException {
        return sqlite3_value_blob(getSQLite3ValuePtr(parameterIndex));
    }
    
    /**
     * invoke sqlite3_value_bytes() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return the number of bytes
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     */
    public int getByteLength(int parameterIndex) throws SQLException {
        return sqlite3_value_bytes(getSQLite3ValuePtr(parameterIndex));
    }
    
    /**
     * invoke sqlite3_value_blob() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return value of byte arrays
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     */
    public byte[] getBytes(int parameterIndex) throws SQLException {
        return sqlite3_value_blob_by_bytes(getSQLite3ValuePtr(parameterIndex));
    }
    
    /**
     * invoke sqlite3_value_double() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return value of double
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     */
    public double getDouble(int parameterIndex) throws SQLException {
        return sqlite3_value_double(getSQLite3ValuePtr(parameterIndex));
    }
    
    /**
     * invoke sqlite3_value_int() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return value of integer
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     */
    public int getInt(int parameterIndex) throws SQLException {
        return sqlite3_value_int(getSQLite3ValuePtr(parameterIndex));
    }
    
    /**
     * invoke sqlite3_value_int64() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return value of long
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     */
    public long getLong(int parameterIndex) throws SQLException {
        return sqlite3_value_int64(getSQLite3ValuePtr(parameterIndex));
    }
    
    /**
     * invoke sqlite3_value_text() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return value of string
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     */
    public String getString(int parameterIndex) throws SQLException {
        return sqlite3_value_text(getSQLite3ValuePtr(parameterIndex));
    }
    
    /**
     * invoke sqlite3_value_text() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return new Date(SQLite3.parseDate(String))
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     * @see org.sqlite.swig.SQLite3#parseDate(String)
     */
    public Date getDate(int parameterIndex) throws SQLException {
        final String val = sqlite3_value_text(getSQLite3ValuePtr(parameterIndex));
        Date ret = null;
        if (val != null) {
            ret = new Date(SQLite3.parseDate(val));
        }
        return ret;
    }
    
    /**
     * invoke sqlite3_value_text() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return new Time(SQLite3.parseTime(String))
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     * @see org.sqlite.swig.SQLite3#parseTime(String)
     */
    public Time getTime(int parameterIndex) throws SQLException {
        final String val = sqlite3_value_text(getSQLite3ValuePtr(parameterIndex));
        Time ret = null;
        if (val != null) {
            ret = new Time(SQLite3.parseTime(val));
        }
        return ret;
    }
    
    /**
     * invoke sqlite3_value_text() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return new Timestamp(SQLite3.parseTimestamp(String))
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     * @see org.sqlite.swig.SQLite3#parseTimestamp(String)
     */
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        final String val = sqlite3_value_text(getSQLite3ValuePtr(parameterIndex));
        Timestamp ret = null;
        if (val != null) {
            ret = new Timestamp(SQLite3.parseTimestamp(val));
        }
        return ret;
    }
    
    /**
     * invoke sqlite3_value_type() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return the type of value
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     */
    public int getValueType(int parameterIndex) throws SQLException {
        return sqlite3_value_type(getSQLite3ValuePtr(parameterIndex));        
    }
    
    /**
     * invoke sqlite3_value_numeric_type() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return attempts to apply numeric affinity to the value.
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/value_blob.html">Obtaining SQL Function Parameter Values</a>
     */
    public int getValueNumericType(int parameterIndex) throws SQLException {
        return sqlite3_value_numeric_type(getSQLite3ValuePtr(parameterIndex));        
    }
    
    /**
     * 
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     */
    protected void validateParamaeterIndexRange(int parameterIndex) throws SQLException {
        if (parameterIndex < 1 || parameterIndex > argc) {
            throw new JdbcSQLException(SQLITE_RANGE);
        }
    }

    /**
     * Returns the value of sqlite3_value*.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return the value of sqlite3_value*.
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     */
    public SWIGTYPE_p_Mem getSQLite3ValuePtr(int parameterIndex) throws SQLException {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        validateParamaeterIndexRange(parameterIndex);
        return get_p_sqlite3_value(value, parameterIndex - 1);
    }
    
    /**
     * invoke sqlite3_get_auxdata() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return the value of sqlite3_get_auxdata().
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/get_auxdata.html">Function Auxiliary Data</a>
     */
    public Object getAuxData(int parameterIndex) throws SQLException {
        validateParamaeterIndexRange(parameterIndex);
        return sqlite3_get_auxdata(ctx, parameterIndex - 1);
    }
    
    /**
     * invoke sqlite3_set_auxdata() function.<br/>
     * The "auxdata" is some auxiliary data that <em>can be associated with a constant argument</em> to a function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param auxdata the meta-data for the N-th argument of the application-defined function.
     * @throws java.sql.SQLException When the parameterIndex is not valid.
     * @see <a href="http://sqlite.org/c3ref/get_auxdata.html">Function Auxiliary Data</a>
     */
    public void setAuxData(int parameterIndex, Object auxdata) throws SQLException {
        validateParamaeterIndexRange(parameterIndex);
        sqlite3_set_auxdata(ctx, parameterIndex - 1, auxdata);
    }
    
    /**
     * invoke sqlite3_context_db_handle() function.
     * @return a copy of the pointer to the database connection (the 1st parameter) of the the sqlite3_create_function() and sqlite3_create_function16() routines that originally registered the application defined function
     * @see <a href="http://www.sqlite.org/c3ref/context_db_handle.html">Database Connection For Functions</a>
     */
    public SWIGTYPE_p_sqlite3 getDbHandle() {
        return sqlite3_context_db_handle(ctx);
    }
}
