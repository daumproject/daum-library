package org.sqlite;

import java.sql.SQLException;
import java.util.logging.Logger;
import org.sqlite.io.Closeable;
import org.sqlite.jdbc.JdbcSQLException;
import org.sqlite.swig.SWIGTYPE_p_Mem;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;
import org.sqlite.swig.SWIGTYPE_p_sqlite3_stmt;
import org.sqlite.swig.SWIGTYPE_p_void;
import static org.sqlite.swig.SQLite3.*;

/**
 * sqlite3_stmt wrapper class.<br/>
 * @see Database#prepare(String)
 * @see Database#prepare(String, org.sqlite.swig.SQLite3.SQLite3StmtPtrPtr)
 * @see Database#prepareMultiple(String)
 * @author calico
 */
public class Statement implements Closeable {
    private final Database db;
    private final SWIGTYPE_p_sqlite3_stmt stmt;
    /** NULL if the MANAGED Statement */
    private final SQLite3StmtPtrPtr ppStmt;
    private boolean isClosed;
    
    Statement(Database db, SWIGTYPE_p_sqlite3_stmt stmt) throws SQLException {
        this(db, stmt, null);
    }
    
    Statement(Database db, SQLite3StmtPtrPtr ppStmt) throws SQLException {
        this(db, ppStmt.getSQLite3StmtPtr(), ppStmt);
    }
    
    private Statement(Database db, SWIGTYPE_p_sqlite3_stmt stmt, SQLite3StmtPtrPtr ppStmt) throws SQLException {
        this.db = db;
        this.stmt = stmt;
        this.ppStmt = ppStmt;
        db.add(this);
    }
    
    /**
     * True is returned when generated with PreparedStatement. 
     * @return True is returned when generated with PreparedStatement. 
     */
    public boolean isManaged() {
        return (ppStmt == null);
    }
    
    /**
     * invoke sqlite3_bind_parameter_count() function.
     * @return parameter count
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/bind_parameter_count.html">Number Of SQL Parameters</a>
     */
    public int getParameterCount() throws SQLException {
        validateStatementOpen();

        return sqlite3_bind_parameter_count(stmt);
    }
    
    /**
     * invoke sqlite3_bind_parameter_index() function.
     * @param parameterName parameter name
     * @return parameter index
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/bind_parameter_index.html">Index Of A Parameter With A Given Name</a>
     */
    public int getParameterIndex(String parameterName) throws SQLException {
        validateStatementOpen();

        return sqlite3_bind_parameter_index(stmt, parameterName);
    }
    
    /**
     * invoke sqlite3_bind_parameter_name() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return parameter name
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/bind_parameter_name.html">Name Of A Host Parameter</a>
     */
    public String getParameterName(int parameterIndex) throws SQLException {
        validateStatementOpen();

        final String x = sqlite3_bind_parameter_name(stmt, parameterIndex);
        checkErrorOccurred();
        return x;
    }
    
    /**
     * invoke sqlite3_column_count() function.
     * @return column count
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_count.html">Number Of Columns In A Result Set</a>
     */
    public int getColumnCount() throws SQLException {
        validateStatementOpen();

        return sqlite3_column_count(stmt);
    }
    
    /**
     * invoke sqlite3_data_count() function.<br/>
     * NOTE: Required to invoke the step() method beforehand.
     * @return data count
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see #step()
     * @see <a href="http://sqlite.org/c3ref/data_count.html">Number of columns in a result set</a>
     */
    public int getDataCount() throws SQLException {
        validateStatementOpen();

        return sqlite3_data_count(stmt);
    }
    
    /**
     * invoke sqlite3_column_database_name() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return database name
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_database_name.html">Source Of Data In A Query Result</a>
     */
    public String getColumnDatabaseName(int columnIndex) throws SQLException {
        validateStatementOpen();

        final String x = sqlite3_column_database_name(stmt, columnIndex - 1);
        checkErrorOccurred();
        return x;
    }
    
    /**
     * invoke sqlite3_column_name() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return column label
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_name.html">Column Names In A Result Set</a>
     */
    public String getColumnLabel(int columnIndex) throws SQLException {
        validateColumnIndexRange(columnIndex);
        
        return sqlite3_column_name(stmt, columnIndex - 1);
    }
    
    /**
     * invoke sqlite3_column_origin_name() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return column name
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_database_name.html">Source Of Data In A Query Result</a>
     */
    public String getColumnName(int columnIndex) throws SQLException {
        validateColumnIndexRange(columnIndex);
        
        return sqlite3_column_origin_name(stmt, columnIndex - 1);
    }
    
    /**
     * invoke sqlite3_column_type() function.<br/>
     * NOTE: Required to invoke the step() method beforehand.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return column type
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see #step()
     * @see <a href="http://sqlite.org/c3ref/column_blob.html">Results Values From A Query</a>
     */
    public int getColumnType(int columnIndex) throws SQLException {
        validateStatementOpen();
        
        final int x = sqlite3_column_type(stmt, columnIndex - 1);
        checkErrorOccurred();
        return x;
    }
    
    /**
     * invoke sqlite3_column_table_name() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return table name
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_database_name.html">Source Of Data In A Query Result</a>
     */
    public String getColumnTableName(int columnIndex) throws SQLException {
        validateColumnIndexRange(columnIndex);

        return sqlite3_column_table_name(stmt, columnIndex - 1);
    }
    
    /**
     * invoke sqlite3_column_decltype() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return column declare type name
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_decltype.html">Declared Datatype Of A Query Result</a>
     */
    public String getColumnTypeName(int columnIndex) throws SQLException {
        validateColumnIndexRange(columnIndex);
        
        return sqlite3_column_decltype(stmt, columnIndex - 1);
    }

    /**
     * invoke sqlite3_column_text() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return value of string
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_blob.html">Results Values From A Query</a>
     */
    public String getString(int columnIndex) throws SQLException {
        validateStatementOpen();
        
        final String x = sqlite3_column_text(stmt, columnIndex - 1);
        checkErrorOccurred();
        return x;
    }
    
    /**
     * invoke sqlite3_column_int() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return value of integer
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_blob.html">Results Values From A Query</a>
     */
    public int getInt(int columnIndex) throws SQLException {
        validateStatementOpen();
        
        final int x = sqlite3_column_int(stmt, columnIndex - 1);
        checkErrorOccurred();
        return x;
    }
    
    /**
     * invoke sqlite3_column_int64() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return value of long
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_blob.html">Results Values From A Query</a>
     */
    public long getLong(int columnIndex) throws SQLException {
        validateStatementOpen();
        
        final long x = sqlite3_column_int64(stmt, columnIndex - 1);
        checkErrorOccurred();
        return x;
    }
    
    /**
     * invoke sqlite3_column_double() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return value of double
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_blob.html">Results Values From A Query</a>
     */
    public double getDouble(int columnIndex) throws SQLException {
        validateStatementOpen();
        
        final double x = sqlite3_column_double(stmt, columnIndex - 1);
        checkErrorOccurred();
        return x;
    }
    
    /**
     * invoke sqlite3_column_blob() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return value of byte arrays
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_blob.html">Results Values From A Query</a>
     */
    public byte[] getBytes(int columnIndex) throws SQLException {
        validateStatementOpen();
        
        final byte[] x = sqlite3_column_blob_by_bytes(stmt, columnIndex - 1);
        checkErrorOccurred();
        return x;
    }
    
    /**
     * invoke sqlite3_column_blob() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return pointer of BLOB object
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_blob.html">Results Values From A Query</a>
     */
    public SWIGTYPE_p_void getBlob(int columnIndex) throws SQLException {
        validateStatementOpen();
        
        final SWIGTYPE_p_void x = sqlite3_column_blob(stmt, columnIndex - 1);
        checkErrorOccurred();
        return x;
    }
    
    /**
     * invoke sqlite3_column_bytes() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the number of bytes
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_blob.html">Results Values From A Query</a>
     */
    public int getByteLength(int columnIndex) throws SQLException {
        validateStatementOpen();
        
        final int x = sqlite3_column_bytes(stmt, columnIndex - 1);
        checkErrorOccurred();
        return x;
    }
    
    /**
     * invoke sqlite3_column_value() function.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the sqlite3_value* object
     * @throws java.sql.SQLException When the columnIndex is not valid. When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/column_blob.html">Results Values From A Query</a>
     */
    public SWIGTYPE_p_Mem getValue(int columnIndex) throws SQLException {
        validateStatementOpen();
        
        SWIGTYPE_p_Mem x = sqlite3_column_value(stmt, columnIndex - 1);        
        checkErrorOccurred();
        return x;
    }
    
    /**
     * invoke sqlite3_bind_null() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_bind_null() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/bind_blob.html">Binding Values To Prepared Statements</a>
     */
    public void bindNull(int parameterIndex) throws SQLException {
        validateStatementOpen();
        
        final int ret = sqlite3_bind_null(stmt, parameterIndex);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, stmt);
        }
    }
    
    /**
     * invoke sqlite3_bind_int() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param val the parameter value
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_bind_int() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/bind_blob.html">Binding Values To Prepared Statements</a>
     */
    public void bindInt(int parameterIndex, int val) throws SQLException {
        validateStatementOpen();
        
        final int ret = sqlite3_bind_int(stmt, parameterIndex, val);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, stmt);
        }
    }
    
    /**
     * invoke sqlite3_bind_int64() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param val the parameter value
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_bind_int64() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/bind_blob.html">Binding Values To Prepared Statements</a>
     */
    public void bindLong(int parameterIndex, long val) throws SQLException {
        validateStatementOpen();
        
        final int ret = sqlite3_bind_int64(stmt, parameterIndex, val);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, stmt);
        }
    }
    
    /**
     * invoke sqlite3_bind_double() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param val the parameter value
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_bind_double() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/bind_blob.html">Binding Values To Prepared Statements</a>
     */
    public void bindDouble(int parameterIndex, double val) throws SQLException {
        validateStatementOpen();
        
        final int ret = sqlite3_bind_double(stmt, parameterIndex, val);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, stmt);
        }
    }
    
    /**
     * invoke sqlite3_bind_text() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param val the parameter value
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_bind_text() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/bind_blob.html">Binding Values To Prepared Statements</a>
     */
    public void bindText(int parameterIndex, String val) throws SQLException {
        validateStatementOpen();
        
        final int ret = sqlite3_bind_text(stmt, parameterIndex, val);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, stmt);
        }
    }
    
    /**
     * invoke sqlite3_bind_blob() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param val the parameter value
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_bind_blob() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/bind_blob.html">Binding Values To Prepared Statements</a>
     * @see #bindBytes(int, byte[], int)
     */
    public void bindBytes(int parameterIndex, byte[] val) throws SQLException {
        bindBytes(parameterIndex, val, val.length);
    }
    
    /**
     * invoke sqlite3_bind_blob() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param val the parameter value
     * @param len the number of bytes
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_bind_blob() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/bind_blob.html">Binding Values To Prepared Statements</a>
     */
    public void bindBytes(int parameterIndex, byte[] val, int len) throws SQLException {
        validateStatementOpen();
        
        final int ret = sqlite3_bind_blob(stmt, parameterIndex, val, len);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, stmt);
        }
    }
    
    /**
     * invoke sqlite3_bind_value() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param val the sqlite3_value* object
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_bind_value() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/bind_blob.html">Binding Values To Prepared Statements</a>
     */
    public void bindValue(int parameterIndex, SWIGTYPE_p_Mem val) throws SQLException {
        validateStatementOpen();
        
        final int ret = sqlite3_bind_value(stmt, parameterIndex, val);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, stmt);
        }
    }
    
    /**
     * invoke sqlite3_bind_blob() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param val the parameter value
     * @param len the number of bytes
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_bind_blob() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/bind_blob.html">Binding Values To Prepared Statements</a>
     */
    public void bindBlob(int parameterIndex, SWIGTYPE_p_void val, int len) throws SQLException {
        validateStatementOpen();
        
        final int ret = sqlite3_bind_blob(stmt, parameterIndex, val, len);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, stmt);
        }
    }
    
    /**
     * invoke sqlite3_bind_zeroblob() function.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param bytes the number of bytes
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_bind_zeroblob() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/bind_blob.html">Binding Values To Prepared Statements</a>
     */
    public void bindZeroBlob(int parameterIndex, int bytes) throws SQLException {
        validateStatementOpen();
        
        final int ret = sqlite3_bind_zeroblob(stmt, parameterIndex, bytes);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, stmt);
        }
    }
    
    /**
     * invoke sqlite3_clear_bindings() function.
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_clear_bindings() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/clear_bindings.html">Reset All Bindings On A Prepared Statement</a>
     */
    public void clearBinding() throws SQLException {
        validateStatementOpen();
        
        final int ret = sqlite3_clear_bindings(stmt);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, stmt);
        }
    }

    /**
     * invoke sqlite3_reset() function.
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_reset() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/reset.html">Reset A Prepared Statement Object</a>
     */
    public void reset() throws SQLException {
        validateStatementOpen();

        final int ret = sqlite3_reset(stmt);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, stmt);
        }
    }

    /**
     * invoke sqlite3_step() function.
     * @return SQLITE_DONE or SQLITE_ROW
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_step() function is neither SQLITE_DONE nor SQLITE_ROW.
     * @see <a href="http://sqlite.org/c3ref/step.html">Evaluate An SQL Statement</a>
     */
    public int step() throws SQLException {
        validateStatementOpen();
        
        int ret = 0;
        while (((ret = sqlite3_step(stmt)) == SQLITE_BUSY)
                && (db.getBusyTimeout() == 0)) {
            // waiting...
        }
        if (ret != SQLITE_DONE && ret != SQLITE_ROW) {
            throw new JdbcSQLException(ret, stmt);
        }
        return ret;
    }

    /**
     * invoke step() method.
     * @return SQLITE_DONE or SQLITE_ROW
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_step() function is neither SQLITE_DONE nor SQLITE_ROW.
     * @see #step()
     */
    public int execute() throws SQLException {
        return step();
    }
    
    /**
     * invoke sqlite3_db_handle() function.
     * @return pointer of database handle
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/db_handle.html">Find The Database Handle Of A Prepared Statement</a>
     */
    public SWIGTYPE_p_sqlite3 getDbHandle() throws SQLException {
        validateStatementOpen();

        return sqlite3_db_handle(stmt);
    }
    
    /**
     * true if the return value of getColumnCount() is not 0.
     * @return true if the return value of getColumnCount() is not 0.
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see #getColumnCount()
     */
    public boolean producedResultSet() throws SQLException {
        return (getColumnCount() != 0);
    }
    
    /**
     * Returns the statement object.
     * @return the statement object
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see <a href="http://sqlite.org/c3ref/stmt.html">SQL Statement Object</a>
     */
    SWIGTYPE_p_sqlite3_stmt getHandle() throws SQLException {
        validateStatementOpen();
        
        return stmt;
    }
    
    /**
     * Retrieving Statement SQL.
     * @return retrieve a saved copy of the original SQL text used to create a [prepared statement].
     * @throws java.sql.SQLException When this method is called on a closed connection.
     */
    public String getSQL() throws SQLException {
        validateStatementOpen();
        
        return sqlite3_sql(stmt);
    }
    
    /**
     * Retrieves whether this Statement object has been closed.
     * @return true if this Statement object is closed; false if it is still open
     */
    public boolean isClosed() {
        return (isClosed || db.isClosed());
    }
    
    /**
     * invoke sqlite3_finalize() function.
     * @throws java.sql.SQLException When the return value of the sqlite3_finalize() function is neither SQLITE_OK nor SQLITE_ABORT.
     * @see <a href="http://sqlite.org/c3ref/finalize.html">Destroy A Prepared Statement Object</a>
     */
    public void close() throws SQLException {
        if (!isClosed) {
            if (!db.isClosed()) {
                int ret = sqlite3_finalize(stmt);
                if (ret != SQLITE_OK && db.getLastError() != SQLITE_OK) {
                    if (ret == SQLITE_ABORT) {
                        // TODO SQLITE_INTERRUPTの場合もログに出力するようにすべき？
                        Logger.getLogger(Statement.class.getName()).info("Transaction aborted.");
                    } else {
                        throw new JdbcSQLException(ret, db.getHandle());
                    }
                }
            } else {
                // abnormal status
                Logger.getLogger(Statement.class.getName()).warning("Database connection is already closed.");
            }
            
            isClosed = true;
            db.remove(this);
            if (ppStmt != null) {
                // UNMANAGED Statement
                ppStmt.delete();
            }
        }
    }
    
    /**
     * Close statement if statement is not closed yet.
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        if (!isClosed) {
            Logger.getLogger(Statement.class.getName()).info("Statement is not closed.");
            close();
        }
        super.finalize();
    }
    
    /**
     * 
     * @throws java.sql.SQLException When this method is called on a closed connection.
     */
    protected void validateStatementOpen() throws SQLException {
        if (isClosed()) {
            throw new SQLException("Statement is already closed.", "90007");
        }
    }

    /**
     * 
     * @param columnIndex the first column is 1, the second is 2, ...
     * @throws java.sql.SQLException the column index out of range.
     */
    public void validateColumnIndexRange(int columnIndex) throws SQLException {
        if (columnIndex < 1 || columnIndex > getColumnCount()) {
            throw new JdbcSQLException(SQLITE_RANGE);
        }
    }
    
    private void checkErrorOccurred() throws SQLException {
        final int ret = db.getLastError();
        if (ret != SQLITE_OK && ret != SQLITE_ROW && ret != SQLITE_DONE) {
            throw new JdbcSQLException(ret, stmt);
        }
    }
    
    /**
     * Retrieves the Database object that produced this Statement object.
     * @return Database object
     * @throws java.sql.SQLException When this method is called on a closed connection.
     */
    public Database getDatabase() throws SQLException {
        validateStatementOpen();

        return db;
    }

    /**
     * invoke sqlite3_next_stmt() function.
     * @return returns the next prepared statement after this object associated with the database connection
     * @throws java.sql.SQLException
     * @see Database#nextStatement(Statement)
     */
    public Statement nextStatement() throws SQLException {
        return db.nextStatement(this);
    }
    
    /**
     * invoke sqlite3_stmt_status() function.
     * @param op status parameter for prepared statements
     * @param reset true if the counter is reset to zero after this interface call returns
     * @return Retrieves and reset counter values from a prepared statement.
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see <a href="http://www.sqlite.org/c3ref/stmt_status.html">Prepared Statement Status</a>
     * @see <a href="http://www.sqlite.org/c3ref/c_stmtstatus_fullscan_step.html">Status Parameters for prepared statements</a>
     */
    public int status(int op, boolean reset) throws SQLException {
        validateStatementOpen();
        
        return sqlite3_stmt_status(stmt, op, (reset ? 1 : 0));
    }
}
